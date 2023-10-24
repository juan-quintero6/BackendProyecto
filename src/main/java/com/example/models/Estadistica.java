/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author juane
 */
@Entity
public class Estadistica implements Serializable{
   
    private static long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    @Column(name = "create_at", updatable = false)
    @Temporal(TemporalType.DATE)
     private Calendar createdAt;
    @NotNull
    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Calendar updatedAt;
    
    int id_reserva;
    
    String contenido;
    
    String tipoEstadistica;
    
    int id_usuario;
    
    int año;
    
    int mes;

    public Estadistica() {    
    }

    public Estadistica(int id_reserva, String contenido, String tipoContenido, int id_usuario, int año, int mes) {
        this.id_reserva = id_reserva;
        this.contenido = contenido;
        this.tipoEstadistica = tipoContenido;
        this.id_usuario = id_usuario;
        this.año = año;
        this.mes = mes;
    }
    
    @PreUpdate
    private void updateTimestamp(){
        this.updatedAt = Calendar.getInstance();
    }
    
    @PrePersist
    private void creationTimestamp(){
        this.createdAt = this.updatedAt = Calendar.getInstance();
    }
    
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
    
    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTipoEstadistica() {
        return tipoEstadistica;
    }

    public void setTipoEstadistica(String tipoEstadistica) {
        this.tipoEstadistica = tipoEstadistica;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }
 
}
