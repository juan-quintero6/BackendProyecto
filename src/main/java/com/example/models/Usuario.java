/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Catalina
 */
@Entity
public class Usuario implements Serializable {
    
    //relación al nuevo modelo.
    @OneToMany(cascade = ALL, mappedBy = "usuario")
    private Set<Laboratorio> laboratorios;

    //método que utilce el atributo recién creado.
    public Set<Laboratorio> getProducts() {
        return laboratorios;
    }

    //agregar la versión de serialización
    private static final long serialVersionUID = 1L;

    //los datos se van a guardar en una base de datos relacional, se agrega el atributo id.
    //Para indicarle a JPA que ese atributo equivale al id
    //Se usa la anotación @Id y @GeneratedValue 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    //NotNull: El elemento no debe ser nulo
    //@Column(name =“”): Asignar nombre distinto al que tiene el atributo, para la base de datos
    @NotNull
    @Column(name = "cretae_at", updatable = false)
    @Temporal(TemporalType.DATE)
    private Calendar createAt;

    @NotNull
    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Calendar updatedAt;

   
    private String name;

    private String surname;

    private int age;

    private String cellphone;

    private String address;

    private String city;
    
    private String contraseña;

    public Usuario() {

    }

    public Usuario(String contraseñaN, String nameN, String surnameN, int ageN, String cellphoneN, String addressN, String cityN) {
        
        name = nameN;
        surname = surnameN;
        age = ageN;
        cellphone = cellphoneN;
        address = addressN;
        city = cityN;
        contraseña= contraseñaN;
    }



    //@PreUpdate:significa que antes de actualizar una tupla, 
    //se ejecutará el método sobre el cual se encuentre la anotación.
    @PreUpdate
    private void updateTimestamp() {
        this.updatedAt = Calendar.getInstance();
    }

    //@PrePersist significa que antes de guardar un objeto, 
    //ejecutará el método sobre el cual se encuentre la anotación.
    @PrePersist
    private void creationTimestamp() {
        this.createAt = this.updatedAt = Calendar.getInstance();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    
    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

}
