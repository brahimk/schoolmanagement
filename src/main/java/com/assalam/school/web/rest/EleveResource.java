package com.assalam.school.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.assalam.school.domain.Eleve;
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
 * REST controller for managing {@link com.assalam.school.domain.Eleve}.
 */
@Path("/api/eleves")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EleveResource {

    private final Logger log = LoggerFactory.getLogger(EleveResource.class);

    private static final String ENTITY_NAME = "eleve";

    @ConfigProperty(name = "application.name")
    String applicationName;


    
    /**
     * {@code POST  /eleves} : Create a new eleve.
     *
     * @param eleve the eleve to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new eleve, or with status {@code 400 (Bad Request)} if the eleve has already an ID.
     */
    @POST
    @Transactional
    public Response createEleve(@Valid Eleve eleve, @Context UriInfo uriInfo) {
        log.debug("REST request to save Eleve : {}", eleve);
        if (eleve.id != null) {
            throw new BadRequestAlertException("A new eleve cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Eleve.persistOrUpdate(eleve);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /eleves} : Updates an existing eleve.
     *
     * @param eleve the eleve to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated eleve,
     * or with status {@code 400 (Bad Request)} if the eleve is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eleve couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateEleve(@Valid Eleve eleve) {
        log.debug("REST request to update Eleve : {}", eleve);
        if (eleve.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Eleve.persistOrUpdate(eleve);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eleve.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /eleves/:id} : delete the "id" eleve.
     *
     * @param id the id of the eleve to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteEleve(@PathParam("id") Long id) {
        log.debug("REST request to delete Eleve : {}", id);
        Eleve.findByIdOptional(id).ifPresent(eleve -> {
            eleve.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /eleves} : get all the eleves.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of eleves in body.
     */
    @GET
    public List<Eleve> getAllEleves() {
        log.debug("REST request to get all Eleves");
        return Eleve.findAll().list();
    }


    /**
     * {@code GET  /eleves/:id} : get the "id" eleve.
     *
     * @param id the id of the eleve to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the eleve, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getEleve(@PathParam("id") Long id) {
        log.debug("REST request to get Eleve : {}", id);
        Optional<Eleve> eleve = Eleve.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(eleve);
    }
}
