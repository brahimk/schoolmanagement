package com.assalam.school.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.assalam.school.TestUtil;
import com.assalam.school.domain.Discipline;
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
public class DisciplineResourceTest {

    private static final TypeRef<Discipline> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<Discipline>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final Instant DEFAULT_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";


    String adminToken;

    Discipline discipline;

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
    public static Discipline createEntity() {
        var discipline = new Discipline();
        discipline.date = DEFAULT_DATE;
        discipline.description = DEFAULT_DESCRIPTION;
        discipline.comment = DEFAULT_COMMENT;
        return discipline;
    }

    @BeforeEach
    public void initTest() {
        discipline = createEntity();
    }

    @Test
    public void createDiscipline() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Discipline
        discipline = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(discipline)
            .when()
            .post("/api/disciplines")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Discipline in the database
        var disciplineList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(disciplineList).hasSize(databaseSizeBeforeCreate + 1);
        var testDiscipline = disciplineList.stream().filter(it -> discipline.id.equals(it.id)).findFirst().get();
        assertThat(testDiscipline.date).isEqualTo(DEFAULT_DATE);
        assertThat(testDiscipline.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDiscipline.comment).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    public void createDisciplineWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Discipline with an existing ID
        discipline.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(discipline)
            .when()
            .post("/api/disciplines")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Discipline in the database
        var disciplineList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(disciplineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        discipline.date = null;

        // Create the Discipline, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(discipline)
            .when()
            .post("/api/disciplines")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Discipline in the database
        var disciplineList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(disciplineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateDiscipline() {
        // Initialize the database
        discipline = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(discipline)
            .when()
            .post("/api/disciplines")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the discipline
        var updatedDiscipline = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines/{id}", discipline.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the discipline
        updatedDiscipline.date = UPDATED_DATE;
        updatedDiscipline.description = UPDATED_DESCRIPTION;
        updatedDiscipline.comment = UPDATED_COMMENT;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedDiscipline)
            .when()
            .put("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Discipline in the database
        var disciplineList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
        var testDiscipline = disciplineList.stream().filter(it -> updatedDiscipline.id.equals(it.id)).findFirst().get();
        assertThat(testDiscipline.date).isEqualTo(UPDATED_DATE);
        assertThat(testDiscipline.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDiscipline.comment).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    public void updateNonExistingDiscipline() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
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
            .body(discipline)
            .when()
            .put("/api/disciplines")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Discipline in the database
        var disciplineList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteDiscipline() {
        // Initialize the database
        discipline = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(discipline)
            .when()
            .post("/api/disciplines")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the discipline
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/disciplines/{id}", discipline.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var disciplineList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(disciplineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllDisciplines() {
        // Initialize the database
        discipline = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(discipline)
            .when()
            .post("/api/disciplines")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the disciplineList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(discipline.id.intValue()))
            .body("date", hasItem(TestUtil.formatDateTime(DEFAULT_DATE)))            .body("description", hasItem(DEFAULT_DESCRIPTION.toString()))            .body("comment", hasItem(DEFAULT_COMMENT));
    }

    @Test
    public void getDiscipline() {
        // Initialize the database
        discipline = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(discipline)
            .when()
            .post("/api/disciplines")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the discipline
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/disciplines/{id}", discipline.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the discipline
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines/{id}", discipline.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(discipline.id.intValue()))
            
                .body("date", is(TestUtil.formatDateTime(DEFAULT_DATE)))
                .body("description", is(DEFAULT_DESCRIPTION.toString()))
                .body("comment", is(DEFAULT_COMMENT));
    }

    @Test
    public void getNonExistingDiscipline() {
        // Get the discipline
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/disciplines/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
