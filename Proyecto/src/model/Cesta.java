package model;

import java.io.*;
import java.util.*;

public class Cesta {
    private List<Producto> productos = new ArrayList<>();
    private double total = 0.0;

    public void añadir(Producto producto) {
        productos.add(producto);
        total += producto.getPrecio();
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double getTotal() {
        return total;
    }

    public void mostrarContenido() {
        if (productos.isEmpty()) {
            System.out.println("Cesta vacía.");
        } else {
            System.out.println("=== CESTA ===");
            for (Producto item : productos) {
                System.out.println("- " + item);
            }
            System.out.printf("TOTAL: %.2f€%n", total);
        }
        System.out.println();
    }

    public void mostrarReciboYGuardar() {
        limpiarPantalla();
        System.out.println("\n=== RECIBO FINAL ===");
        for (Producto item : productos) {
            System.out.println(item);
        }
        System.out.printf("TOTAL: %.2f€%n", total);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Proyecto/Data/ventas.txt", true))) {
            for (Producto item : productos) {
                bw.write(item.getNombre() + ";" + item.getPrecio());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar ventas: " + e.getMessage());
        }

        System.out.println("\n¡Gracias por usar la app de menús!");
    }

    private void limpiarPantalla() {
        for (int i = 0; i < 30; i++) System.out.println();
    }
}
