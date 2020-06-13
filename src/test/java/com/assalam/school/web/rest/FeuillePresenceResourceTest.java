package com.assalam.school.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.assalam.school.TestUtil;
import com.assalam.school.domain.FeuillePresence;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.nio.ByteBuffer;
    import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@QuarkusTest
public class FeuillePresenceResourceTest {

    private static final TypeRef<FeuillePresence> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<FeuillePresence>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final Instant DEFAULT_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Boolean DEFAULT_PRESENT = false;
    private static final Boolean UPDATED_PRESENT = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";


    String adminToken;

    FeuillePresence feuillePresence;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config =
            RestAssured.config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeuillePresence createEntity() {
        var feuillePresence = new FeuillePresence();
        feuillePresence.date = DEFAULT_DATE;
        feuillePresence.present = DEFAULT_PRESENT;
        feuillePresence.description = DEFAULT_DESCRIPTION;
        feuillePresence.comment = DEFAULT_COMMENT;
        return feuillePresence;
    }

    @BeforeEach
    public void initTest() {
        feuillePresence = createEntity();
    }

    @Test
    public void createFeuillePresence() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the FeuillePresence
        feuillePresence = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(feuillePresence)
            .when()
            .post("/api/feuille-presences")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the FeuillePresence in the database
        var feuillePresenceList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(feuillePresenceList).hasSize(databaseSizeBeforeCreate + 1);
        var testFeuillePresence = feuillePresenceList.stream().filter(it -> feuillePresence.id.equals(it.id)).findFirst().get();
        assertThat(testFeuillePresence.date).isEqualTo(DEFAULT_DATE);
        assertThat(testFeuillePresence.present).isEqualTo(DEFAULT_PRESENT);
        assertThat(testFeuillePresence.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFeuillePresence.comment).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    public void createFeuillePresenceWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the FeuillePresence with an existing ID
        feuillePresence.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(feuillePresence)
            .when()
            .post("/api/feuille-presences")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the FeuillePresence in the database
        var feuillePresenceList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(feuillePresenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        feuillePresence.date = null;

        // Create the FeuillePresence, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(feuillePresence)
            .when()
            .post("/api/feuille-presences")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the FeuillePresence in the database
        var feuillePresenceList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(feuillePresenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateFeuillePresence() {
        // Initialize the database
        feuillePresence = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(feuillePresence)
            .when()
            .post("/api/feuille-presences")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the feuillePresence
        var updatedFeuillePresence = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences/{id}", feuillePresence.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the feuillePresence
        updatedFeuillePresence.date = UPDATED_DATE;
        updatedFeuillePresence.present = UPDATED_PRESENT;
        updatedFeuillePresence.description = UPDATED_DESCRIPTION;
        updatedFeuillePresence.comment = UPDATED_COMMENT;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedFeuillePresence)
            .when()
            .put("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the FeuillePresence in the database
        var feuillePresenceList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(feuillePresenceList).hasSize(databaseSizeBeforeUpdate);
        var testFeuillePresence = feuillePresenceList.stream().filter(it -> updatedFeuillePresence.id.equals(it.id)).findFirst().get();
        assertThat(testFeuillePresence.date).isEqualTo(UPDATED_DATE);
        assertThat(testFeuillePresence.present).isEqualTo(UPDATED_PRESENT);
        assertThat(testFeuillePresence.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFeuillePresence.comment).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    public void updateNonExistingFeuillePresence() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(feuillePresence)
            .when()
            .put("/api/feuille-presences")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the FeuillePresence in the database
        var feuillePresenceList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(feuillePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteFeuillePresence() {
        // Initialize the database
        feuillePresence = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(feuillePresence)
            .when()
            .post("/api/feuille-presences")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the feuillePresence
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/feuille-presences/{id}", feuillePresence.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var feuillePresenceList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(feuillePresenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllFeuillePresences() {
        // Initialize the database
        feuillePresence = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(feuillePresence)
            .when()
            .post("/api/feuille-presences")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the feuillePresenceList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(feuillePresence.id.intValue()))
            .body("date", hasItem(TestUtil.formatDateTime(DEFAULT_DATE)))            .body("present", hasItem(DEFAULT_PRESENT.booleanValue()))            .body("description", hasItem(DEFAULT_DESCRIPTION.toString()))            .body("comment", hasItem(DEFAULT_COMMENT));
    }

    @Test
    public void getFeuillePresence() {
        // Initialize the database
        feuillePresence = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(feuillePresence)
            .when()
            .post("/api/feuille-presences")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the feuillePresence
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/feuille-presences/{id}", feuillePresence.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the feuillePresence
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences/{id}", feuillePresence.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(feuillePresence.id.intValue()))
            
                .body("date", is(TestUtil.formatDateTime(DEFAULT_DATE)))
                .body("present", is(DEFAULT_PRESENT.booleanValue()))
                .body("description", is(DEFAULT_DESCRIPTION.toString()))
                .body("comment", is(DEFAULT_COMMENT));
    }

    @Test
    public void getNonExistingFeuillePresence() {
        // Get the feuillePresence
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/feuille-presences/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
