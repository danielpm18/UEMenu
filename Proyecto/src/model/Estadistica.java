package model;

import java.io.*;
import java.util.*;

//Clase Estadistica: contiene métodos para mostrar estadísticas de ventas.
//Lee el archivo de ventas, procesa los datos y permite visualizar estadísticas.
public class Estadistica {

	// Permite elegir entre mostrar el top 10 productos más vendidos o todas las ventas.
	// Lee los datos del archivo "ventas.txt" y calcula totales y recuentos.
	public static void mostrarEstadisticas() {
	    limpiarPantalla();
	    String archivoVentas = "Proyecto/Data/ventas.txt";
	    Map<String, Integer> contador = new HashMap<>();
	    Map<String, Double> precios = new HashMap<>();
	    double totalVentas = 0.0;
	    int totalProductos = 0;

	    // Leer el archivo de ventas.txt línea por línea
	    // Cada línea tiene formato: nombreProducto;precio
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

	    // Menú para elegir qué tipo de estadísticas mostrar
	    // Opciones: 1 para top 10, 2 para todas las ventas
	    String opcion;
	    while (true) {
	        System.out.println("\n=== Estadísticas de ventas ===");
	        System.out.println("1. Ver top 10");
	        System.out.println("2. Ver todas las ventas");
	        System.out.print("Selecciona una opción: ");
	        opcion = new Scanner(System.in).nextLine().trim();
	        if (opcion.equals("1") || opcion.equals("2")) break;
	    }

	    // Mostrar estadísticas finales (resumen de totales y detalles de ventas)
	    limpiarPantalla();
	    System.out.println("\n=== ESTADÍSTICAS DE VENTAS ===");
	    System.out.println("Total productos vendidos: " + totalProductos);
	    System.out.printf("Total recaudado: %.2f€%n", totalVentas);

	    // Mostrar Top 10 productos más vendidos o todos los productos según la opción elegida (orden segun ventas [no precio])
	    // Incluye el nombre del producto, su precio y la cantidad de ventas
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

	    // Espera que el usuario presione ENTER para salir
	    System.out.println("\nPresiona ENTER para continuar...");
	    new Scanner(System.in).nextLine();
	}

	// Método para limpiar la pantalla simulando espacio con líneas en blanco
	private static void limpiarPantalla() {
	    for (int i = 0; i < 40; i++) System.out.println();
	}


}
