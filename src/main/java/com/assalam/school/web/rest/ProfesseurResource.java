package com.assalam.school.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.assalam.school.domain.Professeur;
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
 * REST controller for managing {@link com.assalam.school.domain.Professeur}.
 */
@Path("/api/professeurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ProfesseurResource {

    private final Logger log = LoggerFactory.getLogger(ProfesseurResource.class);

    private static final String ENTITY_NAME = "professeur";

    @ConfigProperty(name = "application.name")
    String applicationName;


    
    /**
     * {@code POST  /professeurs} : Create a new professeur.
     *
     * @param professeur the professeur to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new professeur, or with status {@code 400 (Bad Request)} if the professeur has already an ID.
     */
    @POST
    @Transactional
    public Response createProfesseur(@Valid Professeur professeur, @Context UriInfo uriInfo) {
        log.debug("REST request to save Professeur : {}", professeur);
        if (professeur.id != null) {
            throw new BadRequestAlertException("A new professeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Professeur.persistOrUpdate(professeur);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /professeurs} : Updates an existing professeur.
     *
     * @param professeur the professeur to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated professeur,
     * or with status {@code 400 (Bad Request)} if the professeur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professeur couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateProfesseur(@Valid Professeur professeur) {
        log.debug("REST request to update Professeur : {}", professeur);
        if (professeur.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Professeur.persistOrUpdate(professeur);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professeur.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /professeurs/:id} : delete the "id" professeur.
     *
     * @param id the id of the professeur to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteProfesseur(@PathParam("id") Long id) {
        log.debug("REST request to delete Professeur : {}", id);
        Professeur.findByIdOptional(id).ifPresent(professeur -> {
            professeur.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /professeurs} : get all the professeurs.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of professeurs in body.
     */
    @GET
    public List<Professeur> getAllProfesseurs() {
        log.debug("REST request to get all Professeurs");
        return Professeur.findAll().list();
    }


    /**
     * {@code GET  /professeurs/:id} : get the "id" professeur.
     *
     * @param id the id of the professeur to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the professeur, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getProfesseur(@PathParam("id") Long id) {
        log.debug("REST request to get Professeur : {}", id);
        Optional<Professeur> professeur = Professeur.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(professeur);
    }
}
