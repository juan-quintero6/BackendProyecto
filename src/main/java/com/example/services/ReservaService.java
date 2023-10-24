/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Reserva;
import com.example.models.ReservaDTO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author juane
 */
@Path("/reservas")
@Produces(MediaType.APPLICATION_JSON)
public class ReservaService {
    
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

        Query q = entityManager.createQuery("select u from Reserva u");
        
        List<Reserva> reservas = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(reservas).build();
    }
    
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReserva(ReservaDTO reserva) {
        JSONObject rta = new JSONObject();
        Reserva reservaTmp = new Reserva();
        reservaTmp.setId_estudiante(reserva.getId_estudiante());
        reservaTmp.setId_laboratorio(reserva.getId_laboratorio());
        reservaTmp.setFecha(reserva.getFecha());
        
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(reservaTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(reservaTmp);
            rta.put("reserva_id", reservaTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            reservaTmp = null;
            } finally {
            entityManager.clear();
            entityManager.close();
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(reservaTmp).build();
    }
    
    @POST
    @Path("/consultar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarReserva(Reserva reservaTmp) {
        try {
            long id = reservaTmp.getId();

            Query query = entityManager.createQuery("SELECT r FROM Reserva r WHERE r.id = :id");
            query.setParameter("id", id);
           
            Reserva reservaCheck = (Reserva) query.getSingleResult();
                
                return Response.status(Response.Status.OK).entity(reservaCheck).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No se encuentra la reserva").build();
        }
    }
   
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarReserva(Reserva reservaTmp) {
        try {
            long id = reservaTmp.getId();

            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            Query query = entityManager.createQuery("DELETE FROM Reserva c WHERE c.id = :id");
            query.setParameter("id", id);

            int deletedCount = query.executeUpdate();

            if (deletedCount > 0) {
                transaction.commit(); // Confirmar la transacción
                return Response.status(Response.Status.OK).entity("Reserva eliminado correctamente").build();
            } else {
                transaction.rollback(); // Rollback la transacción
                return Response.status(Response.Status.NOT_FOUND).entity("Reserva no encontrado").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al eliminar la reserva: " + e.getMessage()).build();
        }
    }
}
