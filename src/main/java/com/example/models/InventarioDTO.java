/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

/**
 *
 * @author juane
 */
public class InventarioDTO {
 
    int idLaboratorio;
    
    String nameArticulo;
    
    String descripcion;
    
    String cantidad;

    public InventarioDTO() {
    }

    public int getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(int idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public String getNameArticulo() {
        return nameArticulo;
    }

    public void setNameArticulo(String nameArticulo) {
        this.nameArticulo = nameArticulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
        
}
