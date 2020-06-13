package com.assalam.school.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.assalam.school.domain.Discipline;
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
 * REST controller for managing {@link com.assalam.school.domain.Discipline}.
 */
@Path("/api/disciplines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DisciplineResource {

    private final Logger log = LoggerFactory.getLogger(DisciplineResource.class);

    private static final String ENTITY_NAME = "discipline";

    @ConfigProperty(name = "application.name")
    String applicationName;


    
    /**
     * {@code POST  /disciplines} : Create a new discipline.
     *
     * @param discipline the discipline to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new discipline, or with status {@code 400 (Bad Request)} if the discipline has already an ID.
     */
    @POST
    @Transactional
    public Response createDiscipline(@Valid Discipline discipline, @Context UriInfo uriInfo) {
        log.debug("REST request to save Discipline : {}", discipline);
        if (discipline.id != null) {
            throw new BadRequestAlertException("A new discipline cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Discipline.persistOrUpdate(discipline);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /disciplines} : Updates an existing discipline.
     *
     * @param discipline the discipline to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated discipline,
     * or with status {@code 400 (Bad Request)} if the discipline is not valid,
     * or with status {@code 500 (Internal Server Error)} if the discipline couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateDiscipline(@Valid Discipline discipline) {
        log.debug("REST request to update Discipline : {}", discipline);
        if (discipline.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Discipline.persistOrUpdate(discipline);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, discipline.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /disciplines/:id} : delete the "id" discipline.
     *
     * @param id the id of the discipline to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteDiscipline(@PathParam("id") Long id) {
        log.debug("REST request to delete Discipline : {}", id);
        Discipline.findByIdOptional(id).ifPresent(discipline -> {
            discipline.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /disciplines} : get all the disciplines.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of disciplines in body.
     */
    @GET
    public List<Discipline> getAllDisciplines() {
        log.debug("REST request to get all Disciplines");
        return Discipline.findAll().list();
    }


    /**
     * {@code GET  /disciplines/:id} : get the "id" discipline.
     *
     * @param id the id of the discipline to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the discipline, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getDiscipline(@PathParam("id") Long id) {
        log.debug("REST request to get Discipline : {}", id);
        Optional<Discipline> discipline = Discipline.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(discipline);
    }
}
