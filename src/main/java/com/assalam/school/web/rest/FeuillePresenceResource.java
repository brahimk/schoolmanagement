package com.assalam.school.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.assalam.school.domain.FeuillePresence;
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
 * REST controller for managing {@link com.assalam.school.domain.FeuillePresence}.
 */
@Path("/api/feuille-presences")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FeuillePresenceResource {

    private final Logger log = LoggerFactory.getLogger(FeuillePresenceResource.class);

    private static final String ENTITY_NAME = "feuillePresence";

    @ConfigProperty(name = "application.name")
    String applicationName;


    
    /**
     * {@code POST  /feuille-presences} : Create a new feuillePresence.
     *
     * @param feuillePresence the feuillePresence to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new feuillePresence, or with status {@code 400 (Bad Request)} if the feuillePresence has already an ID.
     */
    @POST
    @Transactional
    public Response createFeuillePresence(@Valid FeuillePresence feuillePresence, @Context UriInfo uriInfo) {
        log.debug("REST request to save FeuillePresence : {}", feuillePresence);
        if (feuillePresence.id != null) {
            throw new BadRequestAlertException("A new feuillePresence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = FeuillePresence.persistOrUpdate(feuillePresence);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /feuille-presences} : Updates an existing feuillePresence.
     *
     * @param feuillePresence the feuillePresence to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated feuillePresence,
     * or with status {@code 400 (Bad Request)} if the feuillePresence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feuillePresence couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateFeuillePresence(@Valid FeuillePresence feuillePresence) {
        log.debug("REST request to update FeuillePresence : {}", feuillePresence);
        if (feuillePresence.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = FeuillePresence.persistOrUpdate(feuillePresence);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, feuillePresence.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /feuille-presences/:id} : delete the "id" feuillePresence.
     *
     * @param id the id of the feuillePresence to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteFeuillePresence(@PathParam("id") Long id) {
        log.debug("REST request to delete FeuillePresence : {}", id);
        FeuillePresence.findByIdOptional(id).ifPresent(feuillePresence -> {
            feuillePresence.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /feuille-presences} : get all the feuillePresences.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of feuillePresences in body.
     */
    @GET
    public List<FeuillePresence> getAllFeuillePresences() {
        log.debug("REST request to get all FeuillePresences");
        return FeuillePresence.findAll().list();
    }


    /**
     * {@code GET  /feuille-presences/:id} : get the "id" feuillePresence.
     *
     * @param id the id of the feuillePresence to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the feuillePresence, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getFeuillePresence(@PathParam("id") Long id) {
        log.debug("REST request to get FeuillePresence : {}", id);
        Optional<FeuillePresence> feuillePresence = FeuillePresence.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(feuillePresence);
    }
}
