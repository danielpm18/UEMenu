package model;

import java.io.*;
import java.util.*;

public class Producto {
    private String edificio;
    private String categoria;  // MENUS o PRODUCTOS
    private String etiquetas;
    private String nombre;
    private double precio;

    public Producto(String edificio, String categoria, String etiquetas, String nombre, double precio) {
        this.edificio = edificio;
        this.categoria = categoria;
        this.etiquetas = etiquetas;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getEdificio() {
        return edificio;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return String.format("[%s - %s] [%s] %s - %.2f€", edificio, categoria, etiquetas, nombre, precio);
    }

    public static List<Producto> leerDesdeArchivo(String ruta, String edificio, String categoria) {
        List<Producto> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    String etiquetas = partes[0].trim();
                    String nombre = partes[1].trim();
                    double precio = Double.parseDouble(partes[2].trim());
                    lista.add(new Producto(edificio, categoria, etiquetas, nombre, precio));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + ruta);
        }
        return lista;
    }

    public static void escribirEnArchivo(String ruta, List<Producto> productos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Producto p : productos) {
                bw.write(p.getEtiquetas() + ";" + p.getNombre() + ";" + p.getPrecio());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + ruta);
        }
    }
}
