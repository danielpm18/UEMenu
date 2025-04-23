package model;
import java.io.*;
import java.util.*;
public class Menu {
    static Scanner scanner = new Scanner(System.in);
    static List<Item> cesta = new ArrayList<>();
    static double total = 0.0;

    static class Item {
        String edificio;
        String categoria;
        String etiquetas;
        String nombre;
        double precio;

        public Item(String edificio, String categoria, String etiquetas, String nombre, double precio) {
            this.edificio = edificio;
            this.categoria = categoria;
            this.etiquetas = etiquetas;
            this.nombre = nombre;
            this.precio = precio;
        }

        @Override
        public String toString() {
            String displayEtiquetas = etiquetas != null && !etiquetas.isEmpty() ? "[" + etiquetas + "] " : "";
            return String.format("[%s - %s] %s%s - %.2f€", edificio, categoria, displayEtiquetas, nombre, precio);
        }
    }

    public static void main(String[] args) {
        while (true) {
            limpiarPantalla();
            mostrarCesta();
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Ver menús/productos por edificio");
            System.out.println("2. Filtrar por etiquetas (SG, V, P, C) o excluirlas");
            System.out.println("3. Finalizar y ver recibo");
            System.out.print("Selecciona una opción: ");
            String opcion = scanner.nextLine();

            if (opcion.equals("3")) {
                mostrarRecibo();
                break;
            } else if (opcion.equals("2")) {
                System.out.print("Escribe una etiqueta (SG, V, P, C): ");
                String etiqueta = scanner.nextLine().toUpperCase();
                System.out.print("¿Quieres incluir (I) o excluir (E) esta etiqueta?: ");
                String modo = scanner.nextLine().toUpperCase();
                boolean excluir = modo.equals("E");
                mostrarItemsFiltrados(etiqueta, excluir);
            } else if (opcion.equals("1")) {
                seleccionarEdificio();
            }
        }
    }

    static void seleccionarEdificio() {
        System.out.print("Elige un edificio (A-E) o escribe VOLVER: ");
        String edificio = scanner.nextLine().toUpperCase();
        if (edificio.equals("VOLVER")) return;
        if (!List.of("A", "B", "C", "D", "E").contains(edificio)) return;

        while (true) {
            limpiarPantalla();
            mostrarCesta();
            System.out.println("Edificio " + edificio);
            System.out.print("¿Quieres ver MENUS o PRODUCTOS? (Escribe VOLVER para cambiar de edificio): ");
            String tipo = scanner.nextLine().toUpperCase();

            if (tipo.equals("VOLVER")) break;
            if (!tipo.equals("MENUS") && !tipo.equals("PRODUCTOS")) continue;

            String directorio = tipo.equals("MENUS") ? "proyecto/src/data Menu/" : "proyecto/src/data Producto/";
            String archivo = directorio + (tipo.equals("MENUS") ? "Menu Edificio " : "Producto Edificio ") + edificio + ".txt";

            List<Item> items = leerArchivo(archivo, edificio, tipo);
            if (items.isEmpty()) continue;

            while (true) {
                limpiarPantalla();
                mostrarCesta();
                System.out.println("Edificio " + edificio + " - " + tipo);
                for (int i = 0; i < items.size(); i++) {
                    System.out.println(i + ": " + items.get(i));
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
                    if (idx >= 0 && idx < items.size()) {
                        cesta.add(items.get(idx));
                        total += items.get(idx).precio;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    static void mostrarItemsFiltrados(String filtroEtiqueta, boolean excluir) {
        List<Item> filtrados = new ArrayList<>();
        for (String edificio : List.of("A", "B", "C", "D", "E")) {
            for (String tipo : List.of("MENUS", "PRODUCTOS")) {
                String directorio = tipo.equals("MENUS") ? "proyecto/src/data Menu/" : "proyecto/src/data Producto/";
                String archivo = directorio + (tipo.equals("MENUS") ? "Menu Edificio " : "Producto Edificio ") + edificio + ".txt";
                List<Item> items = leerArchivo(archivo, edificio, tipo);
                for (Item item : items) {
                    if (item.etiquetas != null && !item.etiquetas.isEmpty()) {
                        List<String> etiquetas = Arrays.asList(item.etiquetas.split(","));
                        boolean contiene = etiquetas.contains(filtroEtiqueta);
                        if ((excluir && !contiene) || (!excluir && contiene)) {
                            filtrados.add(item);
                        }
                    } else {
                        if (excluir && (item.etiquetas == null || item.etiquetas.isEmpty())) {
                        } else if (!excluir && (item.etiquetas == null || item.etiquetas.isEmpty()) && !filtroEtiqueta.isEmpty()) {
                        } else {
                            if (!excluir && (item.etiquetas == null || item.etiquetas.isEmpty()) && filtroEtiqueta.isEmpty()){
                                filtrados.add(item);
                            } else if (excluir && (item.etiquetas == null || item.etiquetas.isEmpty()) && !filtroEtiqueta.isEmpty()){
                                filtrados.add(item);
                            }
                        }
                    }
                }
            }
        }

        while (true) {
            limpiarPantalla();
            mostrarCesta();
            System.out.println("=== Resultados filtrados ===");
            if (filtrados.isEmpty()) {
                System.out.println("No se encontraron items con los criterios de filtro especificados.");
            } else {
                for (int i = 0; i < filtrados.size(); i++) {
                    System.out.println(i + ": " + filtrados.get(i));
                }
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
                    cesta.add(filtrados.get(idx));
                    total += filtrados.get(idx).precio;
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }


    static List<Item> leerArchivo(String ruta, String edificio, String categoria) {
        List<Item> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {

                if (categoria.equals("MENUS")) {
                    String[] partes = linea.split(";");
                    if (partes.length == 3) {
                        String nombreMenu = partes[0] + "; " + partes[1] + "; " + partes[2];
                        lista.add(new Item(edificio, categoria, "", nombreMenu, 7.0));
                    } else {
                        System.out.println("Advertencia: Línea de menú con formato inesperado en " + ruta + ": " + linea);
                    }
                } else if (categoria.equals("PRODUCTOS")) {
                    // --- MODIFICACIÓN PARA PRODUCTOS ---
                    String[] productStrings = linea.split(";");
                    if (productStrings.length > 0) {
                        for (String productString : productStrings) {
                            String nombreProducto = productString.trim();

                            StringBuilder etiquetasBuilder = new StringBuilder();
                            if (nombreProducto.toLowerCase().contains("sin gluten")) {
                                etiquetasBuilder.append("SG,");
                            }

                            String etiquetasProducto = etiquetasBuilder.toString();
                            if (etiquetasProducto.endsWith(",")) {
                                etiquetasProducto = etiquetasProducto.substring(0, etiquetasProducto.length() - 1);
                            }

                            lista.add(new Item(edificio, categoria, etiquetasProducto, nombreProducto, 3.0));
                        }
                    } else {
                        System.out.println("Advertencia: Línea de producto vacía o solo con delimitadores en " + ruta + ": " + linea);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Archivo no encontrado: " + ruta);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + ruta);
            e.printStackTrace();
        }
        return lista;
    }

    static void mostrarCesta() {
        if (cesta.isEmpty()) {
            System.out.println("Cesta vacía.");
        } else {
            System.out.println("=== CESTA ===");
            for (Item item : cesta) {
                System.out.println("- " + item);
            }
            System.out.printf("TOTAL: %.2f€%n", total);
        }
        System.out.println();
    }

    static void mostrarRecibo() {
        limpiarPantalla();
        System.out.println("\n=== RECIBO FINAL ===");
        for (Item item : cesta) {
            System.out.println(item);
        }
        System.out.printf("TOTAL: %.2f€%n", total);
        System.out.println("\n¡Gracias por usar la app UEMenu!");
    }

    static void limpiarPantalla() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
}