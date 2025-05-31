package model;

import java.io.*;
import java.util.*;

public class Consumidor extends Usuario {

    protected static Scanner scanner = new Scanner(System.in);
    protected Cesta cesta = new Cesta();
    protected static final List<String> ETIQUETAS_VALIDAS = List.of("SG", "V", "P", "C");


    public Consumidor(String nombre, String contrasena) {
        super(nombre, contrasena);
    }

    @Override
    public String getTipo() {
        return "Consumidor";
    }

    @Override
    public void iniciar() {
        while (true) {
            limpiarPantalla();
            mostrarCesta();
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Ver menús/productos por edificio");
            System.out.println("2. Filtrar por etiquetas (SG, V, P, C) o excluirlas");
            System.out.println("3. Finalizar y ver recibo");
            System.out.print("Selecciona una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> seleccionarEdificio();
                case "2" -> filtrarPorEtiqueta();
                case "3" -> {
                    mostrarRecibo();
                    return;
                }
                default -> {}
            }
        }
    }

    protected void seleccionarEdificio() {
        while (true) {
            limpiarPantalla();
            System.out.print("Elige un edificio (A-E) o escribe VOLVER: ");
            String edificio = scanner.nextLine().toUpperCase();
            if (edificio.equals("VOLVER")) return;
            if (!List.of("A", "B", "C", "D", "E").contains(edificio)) continue;

            boolean volverAEdificio = false;

            while (!volverAEdificio) {
                limpiarPantalla();
                System.out.println("Edificio " + edificio);
                System.out.print("¿Quieres ver MENUS o PRODUCTOS? (Escribe VOLVER para cambiar de edificio): ");
                String tipo = scanner.nextLine().toUpperCase();

                if (tipo.equals("VOLVER")) {
                    volverAEdificio = true;
                    continue;
                }
                if (!tipo.equals("MENUS") && !tipo.equals("PRODUCTOS")) continue;

                String archivo = "Proyecto/Data/" + (tipo.equals("MENUS") ? "MenuEdificio" : "ProductoEdificio") + edificio + ".txt";
                List<Producto> items = Producto.leerDesdeArchivo(archivo, edificio, tipo);
                System.out.println("Se cargaron " + items.size() + " items desde: " + archivo);

                if (items.isEmpty()) continue;

                while (true) {
                    limpiarPantalla();
                    mostrarCesta();
                    System.out.println("Edificio " + edificio + " - " + tipo);
                    for (int i = 0; i < items.size(); i++) {
                        System.out.println(i + ": " + items.get(i));
                    }
                    System.out.println("");
                    System.out.println("VOLVER - Volver atrás");
                    System.out.println("FINALIZAR - Terminar y mostrar recibo");
                    System.out.println("");
                    System.out.print("Elige un número o escribe VOLVER/FINALIZAR: ");
                    String entrada = scanner.nextLine().toUpperCase();

                    if (entrada.equals("VOLVER")) break;
                    if (entrada.equals("FINALIZAR")) {
                        mostrarRecibo();
                        System.exit(0);
                    }

                    try {
                        int idx = Integer.parseInt(entrada.trim());
                        if (idx >= 0 && idx < items.size()) {
                            cesta.añadir(items.get(idx));
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
    }


    protected void filtrarPorEtiqueta() {
    	String etiqueta;
    	while (true) {
    		limpiarPantalla();
    	    System.out.print("Elige una etiqueta para filtrar (SG, V, P, C): ");
    	    etiqueta = scanner.nextLine().toUpperCase();
    	    if (ETIQUETAS_VALIDAS.contains(etiqueta)) break;
    	}
    	String modo;
    	while (true) {
    		limpiarPantalla();
    		System.out.println("Etiqueta: " + etiqueta);
    	    System.out.print("¿Quieres incluir (I) o excluir (E) esta etiqueta?: ");
    	    modo = scanner.nextLine().toUpperCase();
    	    if (modo.equals("I") || modo.equals("E")) break;
    	}

        boolean excluir = modo.equals("E");

        List<Producto> filtrados = new ArrayList<>();
        for (String edificio : List.of("A", "B", "C", "D", "E")) {
            for (String tipo : List.of("MENUS", "PRODUCTOS")) {
                String archivo = "Proyecto/Data/" + (tipo.equals("MENUS") ? "MenuEdificio" : "ProductoEdificio") + edificio + ".txt";
                List<Producto> items = Producto.leerDesdeArchivo(archivo, edificio, tipo);
                System.out.println("Se cargaron " + items.size() + " items desde: " + archivo);

                for (Producto item : items) {
                    List<String> etiquetas = Arrays.asList(item.getEtiquetas().split(","));
                    boolean contiene = etiquetas.contains(etiqueta);
                    if ((excluir && !contiene) || (!excluir && contiene)) {
                        filtrados.add(item);
                    }
                }
            }
        }

        while (true) {
            limpiarPantalla();
            mostrarCesta();
            System.out.println("=== Resultados filtrados para: " + modo + " -> " + etiqueta + " ===");
            for (int i = 0; i < filtrados.size(); i++) {
                System.out.println(i + ": " + filtrados.get(i));
            }
            System.out.println("VOLVER - Volver atrás");
            System.out.println("FINALIZAR - Terminar y mostrar recibo");
            System.out.print("Elige un número o escribe VOLVER/FINALIZAR: ");
            String entrada = scanner.nextLine().toUpperCase();

            if (entrada.equals("VOLVER")) break;
            if (entrada.equals("FINALIZAR")) {
                mostrarRecibo();
                System.exit(0);
            }

            try {
                int idx = Integer.parseInt(entrada.trim());
                if (idx >= 0 && idx < filtrados.size()) {
                    cesta.añadir(filtrados.get(idx));
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    protected void mostrarCesta() {
        cesta.mostrarContenido();
    }


    protected void mostrarRecibo() {
        cesta.mostrarReciboYGuardar();
    }



    protected void limpiarPantalla() {
        for (int i = 0; i < 40; i++) System.out.println();
    }
}
