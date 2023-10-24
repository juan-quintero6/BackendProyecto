/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//Creación del Entity Manager, modificaciones sobre la clase en donde estamos exponiendo los servicios REST
//Entity Manager: El cual es la interfaz que nos permite hacer persistentes nuestras Entities
//Entity Manager:permite realizar distintas consultas o queries.
// Entity Manager: El ciclo de vida es manejado por el contenedor de aplicaciones.
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Usuario;
import com.example.models.UsuarioDTO;
import com.example.models.Laboratorio;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Catalina
 */
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioService {

    /*crear un atributo de tipo EntityManager con la anotación @PersistenceContext() 
      para indicarle cuál es el contexto que usaremos
     */
    @PersistenceContext(unitName = "Proyecto-JPA")
    EntityManager entityManager;

    /*inicialización del EntityManager
    Esta inicialización la haremos con un método que se ejecutará antes de que se acceda a los servicios,
    para esto usaremos la anotación @PostConstruct.
     */
    @PostConstruct
    public void init() {
        try {
            entityManager = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*consultar 
    Query: nos comunicaremos con la base de datos para realizar la consulta.
     */
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Query q = entityManager.createQuery("select u from Usuario u order by u.surname ASC");
        List<Usuario> competitors = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(competitors).build();
    }
    
    /*Registrar
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompetitor(UsuarioDTO usuario) {
        JSONObject rta = new JSONObject();
        Usuario usuarioTmp = new Usuario();
        usuarioTmp.setAddress(usuario.getAddress());
        usuarioTmp.setAge(usuario.getAge());
        usuarioTmp.setCellphone(usuario.getCellphone());
        usuarioTmp.setCity(usuario.getCity());
        usuarioTmp.setName(usuario.getName());
        usuarioTmp.setSurname(usuario.getSurname());
        usuarioTmp.setContraseña(usuario.getContraseña());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(usuarioTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(usuarioTmp);
            rta.put("usuario_id", usuarioTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            usuarioTmp = null;
        } finally {
            entityManager.clear();
            entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
    }

    /*
    Actualizar
     */
    @PUT
    @Path("/actualizar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompetitor(@PathParam("id") Long id, UsuarioDTO usuario) throws JSONException {
        JSONObject rta = new JSONObject();
        Usuario usuarioTmp = entityManager.find(Usuario.class, id);

        if (usuarioTmp != null) {
            usuarioTmp.setAddress(usuario.getAddress());
            usuarioTmp.setAge(usuario.getAge());
            usuarioTmp.setCellphone(usuario.getCellphone());
            usuarioTmp.setCity(usuario.getCity());
            usuarioTmp.setName(usuario.getName());
            usuarioTmp.setSurname(usuario.getSurname());
            usuarioTmp.setContraseña(usuario.getContraseña());

            try {
                entityManager.getTransaction().begin();
                entityManager.merge(usuarioTmp);
                entityManager.getTransaction().commit();
                entityManager.refresh(usuarioTmp);
                rta.put("usuario_id", usuarioTmp.getId());
                rta.put("nombre", usuarioTmp.getName());
            } catch (Throwable t) {
                t.printStackTrace();
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                usuarioTmp = null;
            } finally {
                entityManager.clear();
                entityManager.close();
            }
        } else {
            rta.put("mensaje", "El usuario con ID " + id + " no se encontró.");
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUsuario(@PathParam("id") Long id) throws JSONException {
            JSONObject rta = new JSONObject();
        Usuario usuarioTmp = entityManager.find(Usuario.class, id);

        if (usuarioTmp != null) {
            try {
                entityManager.getTransaction().begin();
                entityManager.remove(usuarioTmp);
                entityManager.getTransaction().commit();
                rta.put("mensaje", "Usuario con ID " + id + " ha sido eliminado exitosamente.");
            } catch (Throwable t) {
                t.printStackTrace();
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                rta.put("error", "No se pudo eliminar al usuario con ID " + id);
            } finally {
                entityManager.clear();
                entityManager.close();
            }
        } else {
            rta.put("mensaje", "El usuario con ID " + id + " no se encontró.");
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
    
    }

}
