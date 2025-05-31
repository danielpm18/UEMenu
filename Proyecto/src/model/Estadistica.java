package model;

import java.io.*;
import java.util.*;

public class Estadistica {

	public static void mostrarEstadisticas() {
	    limpiarPantalla();
	    String archivoVentas = "Proyecto/Data/ventas.txt";
	    Map<String, Integer> contador = new HashMap<>();
	    Map<String, Double> precios = new HashMap<>();
	    double totalVentas = 0.0;
	    int totalProductos = 0;

	    try (BufferedReader br = new BufferedReader(new FileReader(archivoVentas))) {
	        String linea;
	        while ((linea = br.readLine()) != null) {
	            String[] partes = linea.split(";");
	            if (partes.length == 2) {
	                String nombre = partes[0].trim();
	                double precio = Double.parseDouble(partes[1].trim());
	                contador.put(nombre, contador.getOrDefault(nombre, 0) + 1);
	                precios.put(nombre, precio);
	                totalVentas += precio;
	                totalProductos++;
	            }
	        }
	    } catch (IOException e) {
	        System.out.println("No se pudo leer el archivo de ventas: " + e.getMessage());
	        return;
	    }

	    String opcion;
	    while (true) {
	        System.out.println("\n=== Estadísticas de ventas ===");
	        System.out.println("1. Ver top 10");
	        System.out.println("2. Ver todas las ventas");
	        System.out.print("Selecciona una opción: ");
	        opcion = new Scanner(System.in).nextLine().trim();
	        if (opcion.equals("1") || opcion.equals("2")) break;
	    }

	    limpiarPantalla();
	    System.out.println("\n=== ESTADÍSTICAS DE VENTAS ===");
	    System.out.println("Total productos vendidos: " + totalProductos);
	    System.out.printf("Total recaudado: %.2f€%n", totalVentas);

	    if (opcion.equals("1")) {
	        System.out.println("\nTop 10 productos más vendidos:");
	        contador.entrySet().stream()
	                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
	                .limit(10)
	                .forEach(e -> System.out.println(e.getKey() + " - " + precios.get(e.getKey()) + "€ - "+ e.getValue() + " ventas"));
	    } else {
	        System.out.println("\nTodos los productos vendidos:");
	        contador.entrySet().stream()
	                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
	                .forEach(e -> System.out.println(e.getKey() + " - " + precios.get(e.getKey()) + "€ - "+ e.getValue() + " ventas"));
	    }

	    System.out.println("\nPresiona ENTER para continuar...");
	    new Scanner(System.in).nextLine();
	}

	private static void limpiarPantalla() {
	    for (int i = 0; i < 35; i++) System.out.println();
	}


}
