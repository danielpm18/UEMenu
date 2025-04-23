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
            return String.format(Locale.US, "[%s - %s] %s%s - %.2f€", edificio, categoria, displayEtiquetas, nombre, precio);
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
                if (!etiqueta.isEmpty() && !List.of("SG", "V", "P", "C").contains(etiqueta)) {
                    System.out.println("Etiqueta no válida. Presiona Enter para continuar...");
                    scanner.nextLine();
                    continue;
                }

                System.out.print("¿Quieres incluir (I) o excluir (E) esta etiqueta?: ");
                String modo = scanner.nextLine().toUpperCase();
                if (!modo.equals("I") && !modo.equals("E")) {
                    System.out.println("Modo no válido. Presiona Enter para continuar...");
                    scanner.nextLine();
                    continue;
                }

                boolean excluir = modo.equals("E");
                mostrarItemsFiltrados(etiqueta, excluir);
            } else if (opcion.equals("1")) {
                seleccionarEdificio();
            } else {
                System.out.println("Opción no válida. Presiona Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    static void seleccionarEdificio() {
        System.out.print("Elige un edificio (A-E) o escribe VOLVER: ");
        String edificio = scanner.nextLine().toUpperCase();
        if (edificio.equals("VOLVER")) return;
        if (!List.of("A", "B", "C", "D", "E").contains(edificio)) {
            System.out.println("Edificio no válido. Presiona Enter para continuar...");
            scanner.nextLine();
            return;
        }
        while (true) {
            limpiarPantalla();
            mostrarCesta();
            System.out.println("Edificio " + edificio);
            System.out.print("¿Quieres ver MENUS o PRODUCTOS? (Escribe VOLVER para cambiar de edificio): ");
            String tipo = scanner.nextLine().toUpperCase();

            if (tipo.equals("VOLVER")) break;
            if (!tipo.equals("MENUS") && !tipo.equals("PRODUCTOS")) {
                System.out.println("Tipo no válido. Presiona Enter para continuar...");
                scanner.nextLine();
                continue;
            }
            String directorio = tipo.equals("MENUS") ? "proyecto/src/data Menu/" : "proyecto/src/data Producto/";
            String archivo = directorio + (tipo.equals("MENUS") ? "Menu Edificio " : "Producto Edificio ") + edificio + ".txt";
            List<Item> allItemsFlatList = leerArchivo(archivo, edificio, tipo);

            if (allItemsFlatList.isEmpty()) {
                System.out.println("No hay " + tipo.toLowerCase() + "s disponibles para Edificio " + edificio + ".");
                System.out.println("Presiona Enter para continuar...");
                scanner.nextLine();
                continue;
            }

            while (true) {
                limpiarPantalla();
                mostrarCesta();
                System.out.println("Edificio " + edificio + " - " + tipo);

                if (tipo.equals("MENUS")) {
                    for (int i = 0; i < allItemsFlatList.size(); i++) {
                        System.out.println(i + ": " + allItemsFlatList.get(i));
                    }
                } else {
                    String[] categoryHeaders = {"Bocatas:", "Platos principales:", "Postres:", "Bebidas:"};
                    int currentItemIndex = 0;
                    int lineNum = 0;

                    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                        String linea;
                        while ((linea = br.readLine()) != null) {
                            String trimmedLine = linea.trim();
                            if (trimmedLine.isEmpty() || trimmedLine.equals(";")) {
                                continue;
                            }

                            if (lineNum < categoryHeaders.length) {
                                System.out.println("\n" + categoryHeaders[lineNum]);
                            } else {
                                System.out.println("\nCategoría Adicional:");
                            }

                            String[] parts = trimmedLine.split(";");
                            if (parts.length > 0 && parts.length % 2 == 0) {
                                for (int i = 0; i < parts.length; i += 2) {
                                    if(currentItemIndex < allItemsFlatList.size()){
                                        System.out.println(currentItemIndex + ": " + allItemsFlatList.get(currentItemIndex).toString());
                                        currentItemIndex++;
                                    } else {
                                        System.out.println("Advertencia de visualización crítica: Índice " + currentItemIndex + " fuera de rango de la lista de ítems (" + allItemsFlatList.size() + "). Posible error de parseo o archivo inesperado.");
                                    }
                                }
                            } else {
                                System.out.println("Advertencia de visualización: Línea con formato inesperado (número impar/cero de partes) en " + archivo + " para visualización: " + trimmedLine);
                            }
                            lineNum++;
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("ERROR: Archivo de productos no encontrado para mostrar: " + archivo);
                    } catch (IOException e) {
                        System.out.println("Error al leer el archivo de productos para mostrar: " + archivo);
                        e.printStackTrace();
                    }
                }

                System.out.println("\nVOLVER - Volver atrás");
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
                    if (idx >= 0 && idx < allItemsFlatList.size()) {
                        cesta.add(allItemsFlatList.get(idx));
                        total += allItemsFlatList.get(idx).precio;
                    } else {
                        System.out.println("Número de ítem no válido.");
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }
    static Item parseProductItemDescription(String description, String edificio, String categoria) {
        if (description == null || description.trim().isEmpty()) {
            return null;
        }

        String nombreProducto = description.trim();

        StringBuilder etiquetasBuilder = new StringBuilder();
        if (nombreProducto.toLowerCase().contains("sin gluten") || nombreProducto.toLowerCase().contains("sin tacc") || nombreProducto.toLowerCase().contains("(sg)")) {
            etiquetasBuilder.append("SG,");
        }
        if (nombreProducto.toLowerCase().contains("vegano") || nombreProducto.toLowerCase().contains("(v)")) {
            etiquetasBuilder.append("V,");
        } else if (nombreProducto.toLowerCase().contains("vegetariano")) {
            etiquetasBuilder.append("V,");
        }
        if (nombreProducto.toLowerCase().contains("pescado") || nombreProducto.toLowerCase().contains("(p)")) {
            etiquetasBuilder.append("P,");
        }
        if (nombreProducto.toLowerCase().contains("carne") || nombreProducto.toLowerCase().contains("pollo") || nombreProducto.toLowerCase().contains("(c)")) {
            etiquetasBuilder.append("C,");
        }

        String etiquetasProducto = etiquetasBuilder.toString();
        if (etiquetasProducto.endsWith(",")) {
            etiquetasProducto = etiquetasProducto.substring(0, etiquetasProducto.length() - 1);
        }
        return new Item(edificio, categoria, etiquetasProducto, nombreProducto, 0.0);
    }

    static List<Item> leerArchivo(String ruta, String edificio, String categoria) {
        List<Item> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String trimmedLine = linea.trim();
                if (trimmedLine.isEmpty() || trimmedLine.equals(";")) {
                    continue;
                }

                if (categoria.equals("MENUS")) {
                    String[] partes = trimmedLine.split(";");
                    if (partes.length == 3) {
                        String nombreMenu = partes[0] + "; " + partes[1] + "; " + partes[2];
                        lista.add(new Item(edificio, categoria, "", nombreMenu, 7.0));
                    } else {
                        System.out.println("Advertencia: Línea de menú con formato inesperado en " + ruta + ": " + linea);
                    }
                } else if (categoria.equals("PRODUCTOS")) {
                    String[] parts = trimmedLine.split(";");
                    if (parts.length > 0 && parts.length % 2 == 0) {
                        for (int i = 0; i < parts.length; i += 2) {
                            String description = parts[i].trim();
                            String priceString = parts[i+1].trim();

                            try {
                                double price = Double.parseDouble(priceString);
                                Item tempItem = parseProductItemDescription(description, edificio, categoria);

                                if (tempItem != null) {
                                    lista.add(new Item(tempItem.edificio, tempItem.categoria, tempItem.etiquetas, tempItem.nombre, price));
                                } else {
                                    System.out.println("Advertencia: Descripción de producto vacía en línea de productos en " + ruta + ": " + trimmedLine);
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("Advertencia: Precio no válido ('" + priceString + "') para el producto '" + description + "' en " + ruta);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                System.out.println("Error de formato inesperado en línea de productos (faltan precio?) en " + ruta + ": " + trimmedLine);
                                break;
                            }
                        }
                    } else {
                        System.out.println("Advertencia: Línea de producto con formato inesperado (número impar/cero de partes) en " + ruta + ": " + trimmedLine);
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

    static void mostrarItemsFiltrados(String filtroEtiqueta, boolean excluir) {
        List<Item> filtrados = new ArrayList<>();
        for (String edificio : List.of("A", "B", "C", "D", "E")) {
            for (String tipo : List.of("MENUS", "PRODUCTOS")) {
                String directorio = tipo.equals("MENUS") ? "proyecto/src/data Menu/" : "proyecto/src/data Producto/";
                String archivo = directorio + (tipo.equals("MENUS") ? "Menu Edificio " : "Producto Edificio ") + edificio + ".txt";
                List<Item> items = leerArchivo(archivo, edificio, tipo);

                for (Item item : items) {
                    boolean itemHasFilterTag = item.etiquetas != null &&
                            !item.etiquetas.isEmpty() &&
                            Arrays.asList(item.etiquetas.split(",")).contains(filtroEtiqueta);

                    if (filtroEtiqueta.isEmpty()) {
                        filtrados.add(item);
                    } else {
                        if (excluir) {
                            if (!itemHasFilterTag) {
                                filtrados.add(item);
                            }
                        } else {
                            if (itemHasFilterTag) {
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
            System.out.println("\nVOLVER - Volver atrás");
            System.out.println("FINALIZAR - Terminar y mostrar recibo");
            System.out.print("Elige un número o escribe VOLVER/FINALIZAR: ");
            String entrada = scanner.nextLine().toUpperCase();

            if (entrada.equals("VOLVER")) break;
            if (entrada.equals("FINALIZAR")) {
                mostrarRecibo();
                System.exit(0);
            }
        }
    }


    static void mostrarCesta() {
        if (cesta.isEmpty()) {
            System.out.println("Cesta vacía");
        } else {
            System.out.println("=== CESTA ===");
            for (Item item : cesta) {
                System.out.println("- " + item);
            }
            System.out.printf(Locale.US, "TOTAL: %.2f€%n", total);
        }
        System.out.println();
    }

    static void mostrarRecibo() {
        limpiarPantalla();
        System.out.println("\n=== RECIBO FINAL ===");
        for (Item item : cesta) {
            System.out.println(item);
        }
        System.out.printf(Locale.US, "TOTAL: %.2f€%n", total);
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