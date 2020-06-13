package com.assalam.school.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.assalam.school.TestUtil;
import com.assalam.school.domain.Eleve;
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
public class EleveResourceTest {

    private static final TypeRef<Eleve> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<Eleve>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_NAISSANCE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_DATE_NAISSANCE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final String DEFAULT_NIVEAU_SCOLAIRE = "AAAAAAAAAA";
    private static final String UPDATED_NIVEAU_SCOLAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_NIVEAU_ARABE = "AAAAAAAAAA";
    private static final String UPDATED_NIVEAU_ARABE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = "0".getBytes(); // TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = "1".getBytes(); // TestUtil.createByteArray(1, "1");
    // private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    // private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";


    String adminToken;

    Eleve eleve;

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
    public static Eleve createEntity() {
        var eleve = new Eleve();
        eleve.nom = DEFAULT_NOM;
        eleve.prenom = DEFAULT_PRENOM;
        eleve.dateNaissance = DEFAULT_DATE_NAISSANCE;
        eleve.niveauScolaire = DEFAULT_NIVEAU_SCOLAIRE;
        eleve.niveauArabe = DEFAULT_NIVEAU_ARABE;
        eleve.comment = DEFAULT_COMMENT;
        eleve.image = DEFAULT_IMAGE;
        return eleve;
    }

    @BeforeEach
    public void initTest() {
        eleve = createEntity();
    }

    @Test
    public void createEleve() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Eleve
        eleve = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(eleve)
            .when()
            .post("/api/eleves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Eleve in the database
        var eleveList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(eleveList).hasSize(databaseSizeBeforeCreate + 1);
        var testEleve = eleveList.stream().filter(it -> eleve.id.equals(it.id)).findFirst().get();
        assertThat(testEleve.nom).isEqualTo(DEFAULT_NOM);
        assertThat(testEleve.prenom).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEleve.dateNaissance).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testEleve.niveauScolaire).isEqualTo(DEFAULT_NIVEAU_SCOLAIRE);
        assertThat(testEleve.niveauArabe).isEqualTo(DEFAULT_NIVEAU_ARABE);
        assertThat(testEleve.comment).isEqualTo(DEFAULT_COMMENT);
        assertThat(testEleve.image).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    public void createEleveWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Eleve with an existing ID
        eleve.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(eleve)
            .when()
            .post("/api/eleves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Eleve in the database
        var eleveList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(eleveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNomIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        eleve.nom = null;

        // Create the Eleve, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(eleve)
            .when()
            .post("/api/eleves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Eleve in the database
        var eleveList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkPrenomIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        eleve.prenom = null;

        // Create the Eleve, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(eleve)
            .when()
            .post("/api/eleves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Eleve in the database
        var eleveList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkDateNaissanceIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        eleve.dateNaissance = null;

        // Create the Eleve, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(eleve)
            .when()
            .post("/api/eleves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Eleve in the database
        var eleveList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateEleve() {
        // Initialize the database
        eleve = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(eleve)
            .when()
            .post("/api/eleves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the eleve
        var updatedEleve = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves/{id}", eleve.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the eleve
        updatedEleve.nom = UPDATED_NOM;
        updatedEleve.prenom = UPDATED_PRENOM;
        updatedEleve.dateNaissance = UPDATED_DATE_NAISSANCE;
        updatedEleve.niveauScolaire = UPDATED_NIVEAU_SCOLAIRE;
        updatedEleve.niveauArabe = UPDATED_NIVEAU_ARABE;
        updatedEleve.comment = UPDATED_COMMENT;
        updatedEleve.image = UPDATED_IMAGE;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedEleve)
            .when()
            .put("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Eleve in the database
        var eleveList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
        var testEleve = eleveList.stream().filter(it -> updatedEleve.id.equals(it.id)).findFirst().get();
        assertThat(testEleve.nom).isEqualTo(UPDATED_NOM);
        assertThat(testEleve.prenom).isEqualTo(UPDATED_PRENOM);
        assertThat(testEleve.dateNaissance).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testEleve.niveauScolaire).isEqualTo(UPDATED_NIVEAU_SCOLAIRE);
        assertThat(testEleve.niveauArabe).isEqualTo(UPDATED_NIVEAU_ARABE);
        assertThat(testEleve.comment).isEqualTo(UPDATED_COMMENT);
        assertThat(testEleve.image).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    public void updateNonExistingEleve() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
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
            .body(eleve)
            .when()
            .put("/api/eleves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Eleve in the database
        var eleveList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEleve() {
        // Initialize the database
        eleve = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(eleve)
            .when()
            .post("/api/eleves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the eleve
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/eleves/{id}", eleve.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var eleveList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(eleveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllEleves() {
        // Initialize the database
        eleve = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(eleve)
            .when()
            .post("/api/eleves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the eleveList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(eleve.id.intValue()))
            .body("nom", hasItem(DEFAULT_NOM))            .body("prenom", hasItem(DEFAULT_PRENOM))            .body("dateNaissance", hasItem(TestUtil.formatDateTime(DEFAULT_DATE_NAISSANCE)))            .body("niveauScolaire", hasItem(DEFAULT_NIVEAU_SCOLAIRE))            .body("niveauArabe", hasItem(DEFAULT_NIVEAU_ARABE))            .body("comment", hasItem(DEFAULT_COMMENT))            .body("image", hasItem(DEFAULT_IMAGE.toString()));
    }

    @Test
    public void getEleve() {
        // Initialize the database
        eleve = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(eleve)
            .when()
            .post("/api/eleves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the eleve
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/eleves/{id}", eleve.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the eleve
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves/{id}", eleve.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(eleve.id.intValue()))
            
                .body("nom", is(DEFAULT_NOM))
                .body("prenom", is(DEFAULT_PRENOM))
                .body("dateNaissance", is(TestUtil.formatDateTime(DEFAULT_DATE_NAISSANCE)))
                .body("niveauScolaire", is(DEFAULT_NIVEAU_SCOLAIRE))
                .body("niveauArabe", is(DEFAULT_NIVEAU_ARABE))
                .body("comment", is(DEFAULT_COMMENT))
                .body("image", is(DEFAULT_IMAGE.toString()));
    }

    @Test
    public void getNonExistingEleve() {
        // Get the eleve
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/eleves/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
