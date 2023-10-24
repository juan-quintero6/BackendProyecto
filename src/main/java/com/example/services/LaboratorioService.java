/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Laboratorio;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author USUARIO
 */
@Path("/laboratorio")
@Produces(MediaType.APPLICATION_JSON)
public class LaboratorioService {

    //Atributo EntityManager: Corresponde clase java Laboratorio
    @PersistenceContext(unitName = "Proyecto-JPA")
    EntityManager entityManagerLaboratorio;

    //Inicializacion: Corresponde clase java Laboratorio
    @PostConstruct
    public void initProducto() {
        try {
            entityManagerLaboratorio = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Get: De la clase java Laboratorio 
    @GET
    @Path("/getLaboratorio")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLaboratorios() {
        Query q = entityManagerLaboratorio.createQuery("select u from Laboratorio u order by u.laboratorio ASC");
        List<Laboratorio> laboratorios = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(laboratorios).build();
    }

    //Post: De la clase java Laboratorio 
    @POST
    @Path("/addLaboratorio")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLaboratorio(Laboratorio laboratorio) {
        JSONObject rta = new JSONObject();
        Laboratorio laboratorioTmp = new Laboratorio();
        //laboratorioTmp.setId(laboratorio.getId());
        laboratorioTmp.setLaboratorio(laboratorio.getLaboratorio());
        try {
            entityManagerLaboratorio.getTransaction().begin();
            entityManagerLaboratorio.persist(laboratorioTmp);
            entityManagerLaboratorio.getTransaction().commit();
            entityManagerLaboratorio.refresh(laboratorioTmp);
            rta.put("Laboratorio_id", laboratorioTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManagerLaboratorio.getTransaction().isActive()) {
                entityManagerLaboratorio.getTransaction().rollback();
            }
            laboratorioTmp = null;
        } finally {
            entityManagerLaboratorio.clear();
            entityManagerLaboratorio.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
    }

    @PUT
    @Path("/actualizarLaboratorio/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLaboratorio(@PathParam("id") Long id, Laboratorio laboratorioActualizado) throws JSONException {
        JSONObject rta = new JSONObject();
        Laboratorio laboratorioExistente = entityManagerLaboratorio.find(Laboratorio.class, id);

        if (laboratorioExistente != null) {
            laboratorioExistente.setLaboratorio(laboratorioActualizado.getLaboratorio());

            try {
                entityManagerLaboratorio.getTransaction().begin();
                entityManagerLaboratorio.merge(laboratorioExistente);
                entityManagerLaboratorio.getTransaction().commit();
                entityManagerLaboratorio.refresh(laboratorioExistente);
                rta.put("Laboratorio_id", laboratorioExistente.getId());
            } catch (Throwable t) {
                t.printStackTrace();
                if (entityManagerLaboratorio.getTransaction().isActive()) {
                    entityManagerLaboratorio.getTransaction().rollback();
                }
            } finally {
                entityManagerLaboratorio.clear();
                entityManagerLaboratorio.close();
            }
        } else {
            // Manejo de error si el laboratorio no existe
            rta.put("error", "Laboratorio no encontrado");
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
    }

    @DELETE
    @Path("/deleteLaboratorio/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLaboratorio(@PathParam("id") Long id) throws JSONException {
        JSONObject rta = new JSONObject();
        Laboratorio laboratorioExistente = entityManagerLaboratorio.find(Laboratorio.class, id);

        if (laboratorioExistente != null) {
            try {
                entityManagerLaboratorio.getTransaction().begin();
                entityManagerLaboratorio.remove(laboratorioExistente);
                entityManagerLaboratorio.getTransaction().commit();
                rta.put("success", "Laboratorio eliminado exitosamente");
            } catch (Throwable t) {
                t.printStackTrace();
                if (entityManagerLaboratorio.getTransaction().isActive()) {
                    entityManagerLaboratorio.getTransaction().rollback();
                }
            } finally {
                entityManagerLaboratorio.clear();
                entityManagerLaboratorio.close();
            }
        } else {
            // Manejo de error si el laboratorio no existe
            rta.put("error", "Laboratorio no encontrado");
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
    }

}
