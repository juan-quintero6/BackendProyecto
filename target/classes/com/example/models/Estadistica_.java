package com.example.models;

import java.util.Calendar;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-10-22T18:23:21")
@StaticMetamodel(Estadistica.class)
public class Estadistica_ { 

    public static volatile SingularAttribute<Estadistica, Calendar> createdAt;
    public static volatile SingularAttribute<Estadistica, String> contenido;
    public static volatile SingularAttribute<Estadistica, Integer> id_usuario;
    public static volatile SingularAttribute<Estadistica, String> tipoEstadistica;
    public static volatile SingularAttribute<Estadistica, Integer> id_reserva;
    public static volatile SingularAttribute<Estadistica, Integer> mes;
    public static volatile SingularAttribute<Estadistica, Long> id;
    public static volatile SingularAttribute<Estadistica, Integer> año;
    public static volatile SingularAttribute<Estadistica, Calendar> updatedAt;

}