package com.example.models;

import java.util.Calendar;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-10-22T18:23:21")
@StaticMetamodel(Reserva.class)
public class Reserva_ { 

    public static volatile SingularAttribute<Reserva, Calendar> createdAt;
    public static volatile SingularAttribute<Reserva, String> fecha;
    public static volatile SingularAttribute<Reserva, Integer> id_laboratorio;
    public static volatile SingularAttribute<Reserva, Integer> id_estudiante;
    public static volatile SingularAttribute<Reserva, Long> id;
    public static volatile SingularAttribute<Reserva, Calendar> updatedAt;

}