package com.assalam.school.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.assalam.school.domain.Famille;
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
 * REST controller for managing {@link com.assalam.school.domain.Famille}.
 */
@Path("/api/familles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FamilleResource {

    private final Logger log = LoggerFactory.getLogger(FamilleResource.class);

    private static final String ENTITY_NAME = "famille";

    @ConfigProperty(name = "application.name")
    String applicationName;


    
    /**
     * {@code POST  /familles} : Create a new famille.
     *
     * @param famille the famille to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new famille, or with status {@code 400 (Bad Request)} if the famille has already an ID.
     */
    @POST
    @Transactional
    public Response createFamille(@Valid Famille famille, @Context UriInfo uriInfo) {
        log.debug("REST request to save Famille : {}", famille);
        if (famille.id != null) {
            throw new BadRequestAlertException("A new famille cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Famille.persistOrUpdate(famille);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /familles} : Updates an existing famille.
     *
     * @param famille the famille to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated famille,
     * or with status {@code 400 (Bad Request)} if the famille is not valid,
     * or with status {@code 500 (Internal Server Error)} if the famille couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateFamille(@Valid Famille famille) {
        log.debug("REST request to update Famille : {}", famille);
        if (famille.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Famille.persistOrUpdate(famille);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, famille.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /familles/:id} : delete the "id" famille.
     *
     * @param id the id of the famille to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteFamille(@PathParam("id") Long id) {
        log.debug("REST request to delete Famille : {}", id);
        Famille.findByIdOptional(id).ifPresent(famille -> {
            famille.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /familles} : get all the familles.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of familles in body.
     */
    @GET
    public List<Famille> getAllFamilles() {
        log.debug("REST request to get all Familles");
        return Famille.findAll().list();
    }


    /**
     * {@code GET  /familles/:id} : get the "id" famille.
     *
     * @param id the id of the famille to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the famille, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getFamille(@PathParam("id") Long id) {
        log.debug("REST request to get Famille : {}", id);
        Optional<Famille> famille = Famille.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(famille);
    }
}
