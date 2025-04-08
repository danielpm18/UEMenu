package model;

import java.util.ArrayList;

public class Producto {

    public String nombre;
    public int precio;
    public int cantidad;
    public ArrayList<String> listaAlergenos;

    public Producto (String nombre, int precio, int cantidad) {

        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.listaAlergenos = new ArrayList<>();

    }

}
