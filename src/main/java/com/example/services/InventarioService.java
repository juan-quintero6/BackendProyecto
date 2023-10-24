/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Estadistica;

import com.example.models.Inventario;
import com.example.models.InventarioDTO;
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
@Path("/inventarios")
@Produces(MediaType.APPLICATION_JSON)
public class InventarioService {

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

        Query q = entityManager.createQuery("select u from Inventario u");
        
        List<Inventario> inventarios = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(inventarios).build();
    }
    
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEstadistica(InventarioDTO inventario) {
        JSONObject rta = new JSONObject();
        Inventario inventarioTmp = new Inventario();
        
        inventarioTmp.setIdLaboratorio(inventario.getIdLaboratorio());
        inventarioTmp.setNameArticulo(inventario.getNameArticulo());
        inventarioTmp.setDescripcion(inventario.getDescripcion());
        inventarioTmp.setCantidad(inventario.getCantidad());
      
        
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(inventarioTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(inventarioTmp);
            rta.put("inventario_id", inventarioTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            inventarioTmp = null;
            } finally {
            entityManager.clear();
            entityManager.close();
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(inventarioTmp).build();
    }
    
     @POST
    @Path("/consultar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarInventario(Inventario inventarioTmp) {
        try {
            long id = inventarioTmp.getId();

            Query query = entityManager.createQuery("SELECT r FROM Inventario r WHERE r.id = :id");
            query.setParameter("id", id);
           
            Inventario inventarioCheck = (Inventario) query.getSingleResult();
                
            return Response.status(Response.Status.OK).entity(inventarioCheck).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("No se encuentra el inventario").build();
        }
    }
    
    @PUT
    @Path("/editar/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarEstadistica(@PathParam("id") long id, Inventario inventarioTmp) {
    try {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Busca la estadística existente por ID
        Inventario inventarioExistente = entityManager.find(Inventario.class, id);

            if (inventarioExistente != null) {
                // Realiza las modificaciones necesarias en la entidad existente
                inventarioExistente.setIdLaboratorio(inventarioTmp.getIdLaboratorio());
                inventarioExistente.setNameArticulo(inventarioTmp.getNameArticulo());
                inventarioExistente.setDescripcion(inventarioTmp.getDescripcion());
                inventarioExistente.setCantidad(inventarioTmp.getCantidad());        


                // Realiza un merge para guardar los cambios en la base de datos
                entityManager.merge(inventarioExistente);

                transaction.commit(); // Confirmar la transacción

                return Response.status(Response.Status.OK).entity("Inventario modificado correctamente").build();
            } else {
                transaction.rollback(); // Rollback la transacción
                return Response.status(Response.Status.NOT_FOUND).entity("No se encuentra el inventario").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al editar el inventario: " + e.getMessage()).build();
        }
    }
    
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarEstadistica(Inventario inventarioTmp) {
        try {
            long id = inventarioTmp.getId();

            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            Query query = entityManager.createQuery("DELETE FROM Inventario c WHERE c.id = :id");
            query.setParameter("id", id);

            int deletedCount = query.executeUpdate();

            if (deletedCount > 0) {
                transaction.commit(); // Confirmar la transacción
                return Response.status(Response.Status.OK).entity("Inventario eliminado correctamente").build();
            } else {
                transaction.rollback(); // Rollback la transacción
                return Response.status(Response.Status.NOT_FOUND).entity("Inventario no encontrada").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al eliminar el inventatario: " + e.getMessage()).build();
        }
    }   
    
    
}
