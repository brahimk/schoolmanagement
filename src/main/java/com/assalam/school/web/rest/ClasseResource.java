package com.assalam.school.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.assalam.school.domain.Classe;
import com.assalam.school.web.rest.errors.BadRequestAlertException;
import com.assalam.school.web.util.HeaderUtil;
import com.assalam.school.web.util.ResponseUtil;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.assalam.school.domain.Classe}.
 */
@Path("/api/classes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ClasseResource {

    private final Logger log = LoggerFactory.getLogger(ClasseResource.class);

    private static final String ENTITY_NAME = "classe";

    @ConfigProperty(name = "application.name")
    String applicationName;


    
    /**
     * {@code POST  /classes} : Create a new classe.
     *
     * @param classe the classe to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new classe, or with status {@code 400 (Bad Request)} if the classe has already an ID.
     */
    @POST
    @Transactional
    public Response createClasse(@Valid Classe classe, @Context UriInfo uriInfo) {
        log.debug("REST request to save Classe : {}", classe);
        if (classe.id != null) {
            throw new BadRequestAlertException("A new classe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Classe.persistOrUpdate(classe);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /classes} : Updates an existing classe.
     *
     * @param classe the classe to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated classe,
     * or with status {@code 400 (Bad Request)} if the classe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classe couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateClasse(@Valid Classe classe) {
        log.debug("REST request to update Classe : {}", classe);
        if (classe.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Classe.persistOrUpdate(classe);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classe.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /classes/:id} : delete the "id" classe.
     *
     * @param id the id of the classe to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteClasse(@PathParam("id") Long id) {
        log.debug("REST request to delete Classe : {}", id);
        Classe.findByIdOptional(id).ifPresent(classe -> {
            classe.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /classes} : get all the classes.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of classes in body.
     */
    @GET
    public List<Classe> getAllClasses() {
        log.debug("REST request to get all Classes");
        return Classe.findAll().list();
    }


    /**
     * {@code GET  /classes/:id} : get the "id" classe.
     *
     * @param id the id of the classe to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the classe, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getClasse(@PathParam("id") Long id) {
        log.debug("REST request to get Classe : {}", id);
        Optional<Classe> classe = Classe.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(classe);
    }
}
