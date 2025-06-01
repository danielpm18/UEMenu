package model;

import java.io.*;
import java.util.*;

//Clase Producto: representa un menú o producto individual dentro de un edificio.
//Cada producto tiene información como edificio, categoría, etiquetas, nombre y precio.
public class Producto {
	// Atributos privados para almacenar la información del producto.
    private String edificio;
    private String categoria;  // MENUS o PRODUCTOS
    private String etiquetas;
    private String nombre;
    private double precio;

    // Constructor: crea un nuevo objeto Producto con todos sus datos.
    public Producto(String edificio, String categoria, String etiquetas, String nombre, double precio) {
        this.edificio = edificio;
        this.categoria = categoria;
        this.etiquetas = etiquetas;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters: permiten acceder a los valores de cada atributo segun su nombre indica
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

    // Método toString(): devuelve un String legible para mostrar la información del producto en pantalla.
    // Formato: [Edificio - Categoría] [Etiquetas] Nombre - Precio€
    @Override
    public String toString() {
        return String.format("[%s - %s] [%s] %s - %.2f€", edificio, categoria, etiquetas, nombre, precio);
    }

    // Método leerDesdeArchivo(): lee un archivo de texto y devuelve una lista de productos.
    // Cada línea en el archivo debe tener el formato: etiquetas;nombre;precio
    // Usa el edificio y la categoría proporcionados para crear cada Producto.
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

    // Método escribirEnArchivo(): guarda una lista de productos en un archivo de texto.
    // Cada producto se guarda en una línea con el formato: etiquetas;nombre;precio
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
