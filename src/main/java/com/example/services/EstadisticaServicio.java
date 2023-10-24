/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Estadistica;
import com.example.models.EstadisticaDTO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author juane
 */
@Path("/estadisticas")
@Produces(MediaType.APPLICATION_JSON)
public class EstadisticaServicio {

   @PersistenceContext(unitName = "BackendPU")
    EntityManager entityManager;
    
    @PostConstruct
    public void init() {
        try {
            entityManager =
        PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
        e.printStackTrace();
        }
    }
    
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        Query q = entityManager.createQuery("select u from Estadistica u");
        
        List<Estadistica> estadisticas = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(estadisticas).build();
    }
    
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEstadistica(EstadisticaDTO estadistica) {
        JSONObject rta = new JSONObject();
        Estadistica estadisticaTmp = new Estadistica();
        
        estadisticaTmp.setId_reserva(estadistica.getId_reserva());
        estadisticaTmp.setContenido(estadistica.getContenido());
        estadisticaTmp.setTipoEstadistica(estadistica.getTipoEstadistica());
        estadisticaTmp.setId_usuario(estadistica.getId_reserva());        
        estadisticaTmp.setAño(estadistica.getAño());        
        estadisticaTmp.setMes(estadistica.getMes());        
        
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(estadisticaTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(estadisticaTmp);
            rta.put("estadistica_id", estadisticaTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            estadisticaTmp = null;
            } finally {
            entityManager.clear();
            entityManager.close();
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(estadisticaTmp).build();
    }

    @POST
    @Path("/consultar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarEstadistica(Estadistica estadisticaTmp) {
        try {
            long id = estadisticaTmp.getId();

            Query query = entityManager.createQuery("SELECT r FROM Estadistica r WHERE r.id = :id");
            query.setParameter("id", id);
           
            Estadistica estadisticaCheck = (Estadistica) query.getSingleResult();
                
            return Response.status(Response.Status.OK).entity(estadisticaCheck).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No se encuentra la estadistica").build();
        }
    }
    
    @PUT
    @Path("/editar/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarEstadistica(@PathParam("id") long id, Estadistica estadisticaTmp) {
    try {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Busca la estadística existente por ID
        Estadistica estadisticaExistente = entityManager.find(Estadistica.class, id);

            if (estadisticaExistente != null) {
                // Realiza las modificaciones necesarias en la entidad existente
                estadisticaExistente.setId_reserva(estadisticaTmp.getId_reserva());
                estadisticaExistente.setContenido(estadisticaTmp.getContenido());
                estadisticaExistente.setTipoEstadistica(estadisticaTmp.getTipoEstadistica());
                estadisticaExistente.setId_usuario(estadisticaTmp.getId_reserva());        
                estadisticaExistente.setAño(estadisticaTmp.getAño());        
                estadisticaExistente.setMes(estadisticaTmp.getMes()); 

                // Realiza un merge para guardar los cambios en la base de datos
                entityManager.merge(estadisticaExistente);

                transaction.commit(); // Confirmar la transacción

                return Response.status(Response.Status.OK).entity("Estadística modificada correctamente").build();
            } else {
                transaction.rollback(); // Rollback la transacción
                return Response.status(Response.Status.NOT_FOUND).entity("No se encuentra la estadística").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al editar la estadística: " + e.getMessage()).build();
        }
    }
    
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarEstadistica(Estadistica estadisticaTmp) {
        try {
            long id = estadisticaTmp.getId();

            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            Query query = entityManager.createQuery("DELETE FROM Estadistica c WHERE c.id = :id");
            query.setParameter("id", id);

            int deletedCount = query.executeUpdate();

            if (deletedCount > 0) {
                transaction.commit(); // Confirmar la transacción
                return Response.status(Response.Status.OK).entity("Estadistica eliminada correctamente").build();
            } else {
                transaction.rollback(); // Rollback la transacción
                return Response.status(Response.Status.NOT_FOUND).entity("Estadistica no encontrada").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al eliminar la estadistica: " + e.getMessage()).build();
        }
    }   
}
