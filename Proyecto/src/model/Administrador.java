package model;

import java.io.*;
import java.util.*;

public class Administrador extends Consumidor {

    public Administrador(String nombre, String contrasena) {
        super(nombre, contrasena);
    }

    @Override
    public String getTipo() {
        return "Administrador";
    }

    @Override
    public void iniciar() {
        while (true) {
            limpiarPantalla();
            mostrarCesta();
            System.out.println("=== Menú Administrador ===");
            System.out.println("1. Ver menús/productos por edificio");
            System.out.println("2. Filtrar por etiquetas");
            System.out.println("3. Editar productos/menús");
            System.out.println("4. Finalizar y ver recibo");
            System.out.print("Selecciona una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> seleccionarEdificio();
                case "2" -> filtrarPorEtiqueta();
                case "3" -> editarArchivos();
                case "4" -> {
                    mostrarRecibo();
                    return;
                }
                default -> {}
            }
        }
    }

    protected void editarArchivos() {
    	String edificio;
    	while (true) {
    		limpiarPantalla();
    	    System.out.print("Selecciona edificio (A-E) o VOLVER: ");
    	    edificio = scanner.nextLine().toUpperCase();
    	    if (edificio.equals("VOLVER")) return;
    	    if (List.of("A", "B", "C", "D", "E").contains(edificio)) break;
    	}

    	String tipo;
    	while (true) {
    		limpiarPantalla();
    		System.out.println("Edificio " + edificio);
    	    System.out.print("¿Editar MENUS o PRODUCTOS?: ");
    	    tipo = scanner.nextLine().toUpperCase();
    	    if (tipo.equals("MENUS") || tipo.equals("PRODUCTOS")) break;
    	}


        String archivo = "Proyecto/Data/" + (tipo.equals("MENUS") ? "MenuEdificio" : "ProductoEdificio") + edificio + ".txt";
        List<Producto> productos = Producto.leerDesdeArchivo(archivo, edificio, tipo);

        while (true) {
            limpiarPantalla();
            System.out.println("=== Editar " + tipo + " del edificio " + edificio + " ===");
            for (int i = 0; i < productos.size(); i++) {
                System.out.println(i + ": " + productos.get(i));
            }
            System.out.println();
            System.out.println("A - Añadir nuevo | M - Modificar | E - Eliminar | S - Salir");
            System.out.print("Elige opción: ");
            String opcion = scanner.nextLine().toUpperCase();

            switch (opcion) {
	            case "A" -> {
	            	String finalEtiquetas;
	            	while (true) {
	            	    System.out.println("Etiquetas válidas: SG, V, P, C (separadas por coma)");
	            	    System.out.print("Etiquetas: ");
	            	    String etiquetasInput = scanner.nextLine().toUpperCase();
	            	    String[] etiquetasArray = etiquetasInput.split(",");
	            	    boolean todasValidas = true;

	            	    for (String e : etiquetasArray) {
	            	        if (!ETIQUETAS_VALIDAS.contains(e.trim())) {
	            	        	System.out.println("");
	            	            System.out.println("❌ La etiqueta '" + e.trim() + "' no es válida. Inténtalo de nuevo.");
	            	            todasValidas = false;
	            	            break;
	            	        }
	            	    }

	            	    if (todasValidas) {
	            	        Set<String> etiquetaSet = new LinkedHashSet<>();  // Usamos LinkedHashSet para mantener el orden
	            	        for (String e : etiquetasArray) {
	            	            etiquetaSet.add(e.trim());
	            	        }
	            	        finalEtiquetas = String.join(",", etiquetaSet);
	            	        break;
	            	    }
	            	}

	                System.out.print("Nombre del producto: ");
	                String nombre = scanner.nextLine();
	
	                System.out.print("Precio (usa punto decimal o coma): ");
	                String precioStr = scanner.nextLine().replace(",", ".");
	                double precio = Double.parseDouble(precioStr);
	
	                productos.add(new Producto(edificio, tipo, finalEtiquetas, nombre, precio));
	             }

                case "M" -> {
                    System.out.print("Número del producto a modificar: ");
                    int idx = Integer.parseInt(scanner.nextLine());
                    if (idx >= 0 && idx < productos.size()) {
                        Producto p = productos.get(idx);
                        System.out.println("Modificando: " + p);
                        
                        while (true) {
                            System.out.print("Nuevas etiquetas válidas (SG,V,P,C) (ENTER para mantener): ");
                            String nuevasEtiquetas = scanner.nextLine().toUpperCase();
                            if (nuevasEtiquetas.isEmpty()) break;

                            String[] etiquetasArray = nuevasEtiquetas.split(",");
                            boolean todasValidas = true;

                            for (String e : etiquetasArray) {
                                if (!ETIQUETAS_VALIDAS.contains(e.trim())) {
                                	System.out.println("");
                                    System.out.println("❌ La etiqueta '" + e.trim() + "' no es válida. Inténtalo de nuevo.");
                                    todasValidas = false;
                                    break;
                                }
                            }

                            if (todasValidas) {
                                Set<String> etiquetaSet = new LinkedHashSet<>();
                                for (String e : etiquetasArray) {
                                    etiquetaSet.add(e.trim());
                                }
                                String finalEtiquetas = String.join(",", etiquetaSet);
                                p = new Producto(p.getEdificio(), p.getCategoria(), finalEtiquetas, p.getNombre(), p.getPrecio());
                                break;
                            }
                        }

                        System.out.print("Nuevo nombre (ENTER para mantener): ");
                        String nuevoNombre = scanner.nextLine();
                        if (!nuevoNombre.isEmpty()) p = new Producto(p.getEdificio(), p.getCategoria(), p.getEtiquetas(), nuevoNombre, p.getPrecio());
                        
                        System.out.print("Nuevo precio (ENTER para mantener): ");
                        String nuevoPrecio = scanner.nextLine().replace(",", ".");
                        if (!nuevoPrecio.isEmpty()) {
                            try {
                                double precio = Double.parseDouble(nuevoPrecio);
                                p = new Producto(p.getEdificio(), p.getCategoria(), p.getEtiquetas(), p.getNombre(), precio);
                            } catch (NumberFormatException ignored) {}
                        }

                        productos.set(idx, p);
                    }
                }
                case "E" -> {
                    System.out.print("Número del producto a eliminar: ");
                    int idx = Integer.parseInt(scanner.nextLine());
                    if (idx >= 0 && idx < productos.size()) {
                        productos.remove(idx);
                    }
                }
                case "S" -> {
                    Producto.escribirEnArchivo(archivo, productos);
                    System.out.println("Cambios guardados.");
                    return;
                }
                default -> {}
            }
        }
    }
}
