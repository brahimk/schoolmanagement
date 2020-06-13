package com.assalam.school.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.assalam.school.TestUtil;
import com.assalam.school.domain.Classe;
import com.assalam.school.domain.enumeration.Creneau;
import com.assalam.school.domain.enumeration.Salle;

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
public class ClasseResourceTest {

    private static final TypeRef<Classe> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<Classe>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_NIVEAU = "AAAAAAAAAA";
    private static final String UPDATED_NIVEAU = "BBBBBBBBBB";

    private static final Salle DEFAULT_SALLE = Salle.SALLE_1;
    private static final Salle UPDATED_SALLE = Salle.SALLE_2;

    private static final Creneau DEFAULT_CRENEAU = Creneau.MECREDI_MATIN;
    private static final Creneau UPDATED_CRENEAU = Creneau.MERCREDI_APRES_MIDI;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";


    String adminToken;

    Classe classe;

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
    public static Classe createEntity() {
        var classe = new Classe();
        classe.nom = DEFAULT_NOM;
        classe.niveau = DEFAULT_NIVEAU;
        classe.salle = DEFAULT_SALLE;
        classe.creneau = DEFAULT_CRENEAU;
        classe.comment = DEFAULT_COMMENT;
        return classe;
    }

    @BeforeEach
    public void initTest() {
        classe = createEntity();
    }

    @Test
    public void createClasse() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Classe
        classe = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(classe)
            .when()
            .post("/api/classes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Classe in the database
        var classeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(classeList).hasSize(databaseSizeBeforeCreate + 1);
        var testClasse = classeList.stream().filter(it -> classe.id.equals(it.id)).findFirst().get();
        assertThat(testClasse.nom).isEqualTo(DEFAULT_NOM);
        assertThat(testClasse.niveau).isEqualTo(DEFAULT_NIVEAU);
        assertThat(testClasse.salle).isEqualTo(DEFAULT_SALLE);
        assertThat(testClasse.creneau).isEqualTo(DEFAULT_CRENEAU);
        assertThat(testClasse.comment).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    public void createClasseWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Classe with an existing ID
        classe.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(classe)
            .when()
            .post("/api/classes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Classe in the database
        var classeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(classeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNomIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        classe.nom = null;

        // Create the Classe, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(classe)
            .when()
            .post("/api/classes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Classe in the database
        var classeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(classeList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkNiveauIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        classe.niveau = null;

        // Create the Classe, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(classe)
            .when()
            .post("/api/classes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Classe in the database
        var classeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(classeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateClasse() {
        // Initialize the database
        classe = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(classe)
            .when()
            .post("/api/classes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the classe
        var updatedClasse = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes/{id}", classe.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the classe
        updatedClasse.nom = UPDATED_NOM;
        updatedClasse.niveau = UPDATED_NIVEAU;
        updatedClasse.salle = UPDATED_SALLE;
        updatedClasse.creneau = UPDATED_CRENEAU;
        updatedClasse.comment = UPDATED_COMMENT;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedClasse)
            .when()
            .put("/api/classes")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Classe in the database
        var classeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        var testClasse = classeList.stream().filter(it -> updatedClasse.id.equals(it.id)).findFirst().get();
        assertThat(testClasse.nom).isEqualTo(UPDATED_NOM);
        assertThat(testClasse.niveau).isEqualTo(UPDATED_NIVEAU);
        assertThat(testClasse.salle).isEqualTo(UPDATED_SALLE);
        assertThat(testClasse.creneau).isEqualTo(UPDATED_CRENEAU);
        assertThat(testClasse.comment).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    public void updateNonExistingClasse() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
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
            .body(classe)
            .when()
            .put("/api/classes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Classe in the database
        var classeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteClasse() {
        // Initialize the database
        classe = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(classe)
            .when()
            .post("/api/classes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the classe
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/classes/{id}", classe.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var classeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(classeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllClasses() {
        // Initialize the database
        classe = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(classe)
            .when()
            .post("/api/classes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the classeList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(classe.id.intValue()))
            .body("nom", hasItem(DEFAULT_NOM))            .body("niveau", hasItem(DEFAULT_NIVEAU))            .body("salle", hasItem(DEFAULT_SALLE.toString()))            .body("creneau", hasItem(DEFAULT_CRENEAU.toString()))            .body("comment", hasItem(DEFAULT_COMMENT));
    }

    @Test
    public void getClasse() {
        // Initialize the database
        classe = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(classe)
            .when()
            .post("/api/classes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the classe
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/classes/{id}", classe.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the classe
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes/{id}", classe.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(classe.id.intValue()))
            
                .body("nom", is(DEFAULT_NOM))
                .body("niveau", is(DEFAULT_NIVEAU))
                .body("salle", is(DEFAULT_SALLE.toString()))
                .body("creneau", is(DEFAULT_CRENEAU.toString()))
                .body("comment", is(DEFAULT_COMMENT));
    }

    @Test
    public void getNonExistingClasse() {
        // Get the classe
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/classes/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
