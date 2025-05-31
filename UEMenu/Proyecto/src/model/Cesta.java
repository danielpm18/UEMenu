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
            for (int i = 0; i < productos.size(); i++) {
                System.out.println((i + 1) + ": " + productos.get(i));
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

    public void eliminarProducto(Scanner scanner) {
        if (productos.isEmpty()) {
            System.out.println("La cesta está vacía.");
            return;
        }

        while (true) {
        	limpiarPantalla();
            mostrarContenido();
            System.out.print("Número del producto a eliminar (o escribe VOLVER para salir): ");
            String entrada = scanner.nextLine().trim().toUpperCase();

            if (entrada.equals("VOLVER")) {
                System.out.println("Saliendo de la eliminación de productos...");
                break;
            }

            try {
                int idx = Integer.parseInt(entrada) - 1;
                if (idx >= 0 && idx < productos.size()) {
                    Producto eliminado = productos.remove(idx);
                    total -= eliminado.getPrecio();
                    System.out.println("✅ Producto eliminado de la cesta: " + eliminado.getNombre());
                } else {
                    System.out.println("Número inválido. Inténtalo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un número válido o escribe VOLVER.");
            }

            if (productos.isEmpty()) {
                System.out.println("✅ La cesta está vacía.");
                break;
            }
        }
    }


    private void limpiarPantalla() {
        for (int i = 0; i < 30; i++) System.out.println();
    }
}
