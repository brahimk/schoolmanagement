package com.assalam.school.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.assalam.school.TestUtil;
import com.assalam.school.domain.Famille;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

    import java.util.List;

@QuarkusTest
public class FamilleResourceTest {

    private static final TypeRef<Famille> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<Famille>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Long DEFAULT_CODE_POSTAL = 1L;
    private static final Long UPDATED_CODE_POSTAL = 2L;

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final Long DEFAULT_TELEPHONE_1 = 1L;
    private static final Long UPDATED_TELEPHONE_1 = 2L;

    private static final Long DEFAULT_TELEPHONE_2 = 1L;
    private static final Long UPDATED_TELEPHONE_2 = 2L;

    private static final String DEFAULT_EMAIL_1 = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_2 = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";


    String adminToken;

    Famille famille;

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
    public static Famille createEntity() {
        var famille = new Famille();
        famille.nom = DEFAULT_NOM;
        famille.prenom = DEFAULT_PRENOM;
        famille.adresse = DEFAULT_ADRESSE;
        famille.codePostal = DEFAULT_CODE_POSTAL;
        famille.ville = DEFAULT_VILLE;
        famille.telephone1 = DEFAULT_TELEPHONE_1;
        famille.telephone2 = DEFAULT_TELEPHONE_2;
        famille.email1 = DEFAULT_EMAIL_1;
        famille.email2 = DEFAULT_EMAIL_2;
        famille.comment = DEFAULT_COMMENT;
        return famille;
    }

    @BeforeEach
    public void initTest() {
        famille = createEntity();
    }

    @Test
    public void createFamille() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Famille
        famille = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Famille in the database
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeCreate + 1);
        var testFamille = familleList.stream().filter(it -> famille.id.equals(it.id)).findFirst().get();
        assertThat(testFamille.nom).isEqualTo(DEFAULT_NOM);
        assertThat(testFamille.prenom).isEqualTo(DEFAULT_PRENOM);
        assertThat(testFamille.adresse).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testFamille.codePostal).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testFamille.ville).isEqualTo(DEFAULT_VILLE);
        assertThat(testFamille.telephone1).isEqualTo(DEFAULT_TELEPHONE_1);
        assertThat(testFamille.telephone2).isEqualTo(DEFAULT_TELEPHONE_2);
        assertThat(testFamille.email1).isEqualTo(DEFAULT_EMAIL_1);
        assertThat(testFamille.email2).isEqualTo(DEFAULT_EMAIL_2);
        assertThat(testFamille.comment).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    public void createFamilleWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Famille with an existing ID
        famille.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Famille in the database
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNomIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        famille.nom = null;

        // Create the Famille, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Famille in the database
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkPrenomIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        famille.prenom = null;

        // Create the Famille, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Famille in the database
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkTelephone1IsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        famille.telephone1 = null;

        // Create the Famille, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Famille in the database
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkTelephone2IsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        famille.telephone2 = null;

        // Create the Famille, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Famille in the database
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkEmail1IsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        famille.email1 = null;

        // Create the Famille, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Famille in the database
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateFamille() {
        // Initialize the database
        famille = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the famille
        var updatedFamille = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles/{id}", famille.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the famille
        updatedFamille.nom = UPDATED_NOM;
        updatedFamille.prenom = UPDATED_PRENOM;
        updatedFamille.adresse = UPDATED_ADRESSE;
        updatedFamille.codePostal = UPDATED_CODE_POSTAL;
        updatedFamille.ville = UPDATED_VILLE;
        updatedFamille.telephone1 = UPDATED_TELEPHONE_1;
        updatedFamille.telephone2 = UPDATED_TELEPHONE_2;
        updatedFamille.email1 = UPDATED_EMAIL_1;
        updatedFamille.email2 = UPDATED_EMAIL_2;
        updatedFamille.comment = UPDATED_COMMENT;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedFamille)
            .when()
            .put("/api/familles")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Famille in the database
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
        var testFamille = familleList.stream().filter(it -> updatedFamille.id.equals(it.id)).findFirst().get();
        assertThat(testFamille.nom).isEqualTo(UPDATED_NOM);
        assertThat(testFamille.prenom).isEqualTo(UPDATED_PRENOM);
        assertThat(testFamille.adresse).isEqualTo(UPDATED_ADRESSE);
        assertThat(testFamille.codePostal).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testFamille.ville).isEqualTo(UPDATED_VILLE);
        assertThat(testFamille.telephone1).isEqualTo(UPDATED_TELEPHONE_1);
        assertThat(testFamille.telephone2).isEqualTo(UPDATED_TELEPHONE_2);
        assertThat(testFamille.email1).isEqualTo(UPDATED_EMAIL_1);
        assertThat(testFamille.email2).isEqualTo(UPDATED_EMAIL_2);
        assertThat(testFamille.comment).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    public void updateNonExistingFamille() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
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
            .body(famille)
            .when()
            .put("/api/familles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Famille in the database
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteFamille() {
        // Initialize the database
        famille = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the famille
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/familles/{id}", famille.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var familleList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(familleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllFamilles() {
        // Initialize the database
        famille = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the familleList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(famille.id.intValue()))
            .body("nom", hasItem(DEFAULT_NOM))            .body("prenom", hasItem(DEFAULT_PRENOM))            .body("adresse", hasItem(DEFAULT_ADRESSE))            .body("codePostal", hasItem(DEFAULT_CODE_POSTAL.intValue()))            .body("ville", hasItem(DEFAULT_VILLE))            .body("telephone1", hasItem(DEFAULT_TELEPHONE_1.intValue()))            .body("telephone2", hasItem(DEFAULT_TELEPHONE_2.intValue()))            .body("email1", hasItem(DEFAULT_EMAIL_1))            .body("email2", hasItem(DEFAULT_EMAIL_2))            .body("comment", hasItem(DEFAULT_COMMENT));
    }

    @Test
    public void getFamille() {
        // Initialize the database
        famille = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(famille)
            .when()
            .post("/api/familles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the famille
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/familles/{id}", famille.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the famille
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles/{id}", famille.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(famille.id.intValue()))
            
                .body("nom", is(DEFAULT_NOM))
                .body("prenom", is(DEFAULT_PRENOM))
                .body("adresse", is(DEFAULT_ADRESSE))
                .body("codePostal", is(DEFAULT_CODE_POSTAL.intValue()))
                .body("ville", is(DEFAULT_VILLE))
                .body("telephone1", is(DEFAULT_TELEPHONE_1.intValue()))
                .body("telephone2", is(DEFAULT_TELEPHONE_2.intValue()))
                .body("email1", is(DEFAULT_EMAIL_1))
                .body("email2", is(DEFAULT_EMAIL_2))
                .body("comment", is(DEFAULT_COMMENT));
    }

    @Test
    public void getNonExistingFamille() {
        // Get the famille
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/familles/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
