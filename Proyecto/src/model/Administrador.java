package model;

import java.io.*;
import java.util.*;
//Clase Administrador: extiende la clase Consumidor. Hereda sus funciones y añade la capacidad de editar productos y menús.
public class Administrador extends Consumidor {
	// Constructor del Administrador. Recibe nombre y contraseña, y los pasa al constructor de la clase padre (Consumidor).
    public Administrador(String nombre, String contrasena) {
        super(nombre, contrasena);
    }
 
    // Devuelve el tipo de usuario: en este caso "Administrador". Esto se usa para mostrar el rol del usuario al iniciar sesión.
    @Override
    public String getTipo() {
        return "Administrador";
    }

    // muestra el menú principal del Administrador en bucle hasta que decida finalizar.
    // Permite ver menús/productos, eliminar de la cesta, filtrar, editar productos/menús o finalizar
    @Override
    public void iniciar() {
        while (true) {
            limpiarPantalla();
            mostrarCesta();
            System.out.println("=== Menú Administrador ===");
            System.out.println("1. Ver menús/productos por edificio");
            System.out.println("2. Eliminar producto de la cesta");
            System.out.println("3. Filtrar por etiquetas");
            System.out.println("4. Editar productos/menús");
            System.out.println("5. Finalizar y ver recibo");
            System.out.print("Selecciona una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> seleccionarEdificio();
                case "2" -> cesta.eliminarProducto(scanner);
                case "3" -> filtrarPorEtiqueta();
                case "4" -> editarArchivos();
                case "5" -> {
                    mostrarRecibo();
                    return;
                }
                default -> {}
            }
        }
    }
    // Método para editar archivos de menús y productos.
    // Pide el edificio y el tipo de compra (MENUS o PRODUCTOS), carga los datos, y permite añadir, modificar o eliminar productos.
    protected void editarArchivos() {
    	// Bucle para pedir el edificio (A-E)
    	String edificio;
    	while (true) {
    		limpiarPantalla();
    	    System.out.print("Selecciona edificio (A-E) o VOLVER: ");
    	    edificio = scanner.nextLine().toUpperCase();
    	    if (edificio.equals("VOLVER")) return;
    	    if (List.of("A", "B", "C", "D", "E").contains(edificio)) break;
    	}

    	// Bucle para pedir si quiere editar MENUS o PRODUCTOS
    	String tipo;
    	while (true) {
    		limpiarPantalla();
    		System.out.println("Edificio " + edificio);
    	    System.out.print("¿Editar MENUS o PRODUCTOS?: ");
    	    tipo = scanner.nextLine().toUpperCase();
    	    if (tipo.equals("MENUS") || tipo.equals("PRODUCTOS")) break;
    	}

    	// Carga el archivo correspondiente (según edificio y tipo)
        String archivo = "Proyecto/Data/" + (tipo.equals("MENUS") ? "MenuEdificio" : "ProductoEdificio") + edificio + ".txt";
        List<Producto> productos = Producto.leerDesdeArchivo(archivo, edificio, tipo);

        // Bucle para mostrar y editar productos: permite añadir, modificar o eliminar.
        // Muestra los productos y un menú de opciones hasta que elija salir (S).
        while (true) {
            limpiarPantalla();
            System.out.println("=== Editar " + tipo + " del edificio " + edificio + " ===");
            for (int i = 0; i < productos.size(); i++) {
                System.out.println((i+1) + ": " + productos.get(i));
            }
            System.out.println();
            System.out.println("A - Añadir nuevo | M - Modificar | E - Eliminar | S - Salir");
            System.out.print("Elige opción: ");
            String opcion = scanner.nextLine().toUpperCase();

            switch (opcion) {
         // Opción A: Añadir un nuevo producto al archivo.
         // Pide las etiquetas (con validación), el nombre (no puede estar vacío) y el precio (número válido y no negativo).
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

	            	String nombre = "";
	            	while (nombre.isEmpty()) {
	            	    System.out.print("Nombre del producto: ");
	            	    nombre = scanner.nextLine().trim();
	            	    if (nombre.isEmpty()) {
	            	        System.out.println("El nombre no puede estar vacío. Inténtalo de nuevo.");
	            	    }
	            	}

	
	            	double precio = -1;
	            	while (precio < 0) {
	            	    System.out.print("Precio (usa punto o coma): ");
	            	    String precioStr = scanner.nextLine().replace(",", ".");
	            	    try {
	            	        precio = Double.parseDouble(precioStr);
	            	        if (precio < 0) {
	            	            System.out.println("El precio no puede ser negativo. Inténtalo de nuevo.");
	            	        }
	            	    } catch (NumberFormatException e) {
	            	        System.out.println("Por favor, introduce un número válido.");
	            	    }
	            	}

	
	                productos.add(new Producto(edificio, tipo, finalEtiquetas, nombre, precio));
	             }
	            
	            // Opción M: Modificar un producto existente. Pide el número, luego permite editar etiquetas, nombre y precio.
	            case "M" -> {
	                int idx = -1;
	                while (true) {
	                    System.out.print("Número del producto a modificar: ");
	                    String entrada = scanner.nextLine();
	                    try {
	                        idx = Integer.parseInt(entrada.trim()) - 1;  // Recuerda: el usuario ve los productos desde 1, restamos 1
	                        if (idx >= 0 && idx < productos.size()) {
	                            break;  // Número correcto, salimos del bucle
	                        } else {
	                        	System.out.println("");
	                            System.out.println("Número inválido. Inténtalo de nuevo.");
	                        }
	                    } catch (NumberFormatException e) {
	                    	System.out.println("");
	                        System.out.println("Por favor, introduce un número válido.");
	                    }
	                }

	                Producto p = productos.get(idx);
	                System.out.println("");
	                System.out.println("Modificando: " + p);

	                // Etiquetas
	                while (true) {
	                    System.out.print("Nuevas etiquetas válidas (SG,V,P,C) (ENTER para mantener): ");
	                    String nuevasEtiquetas = scanner.nextLine().toUpperCase();
	                    if (nuevasEtiquetas.isEmpty()) break;

	                    String[] etiquetasArray = nuevasEtiquetas.split(",");
	                    Set<String> etiquetaSet = new LinkedHashSet<>();
	                    boolean todasValidas = true;

	                    for (String e : etiquetasArray) {
	                        e = e.trim();
	                        if (!ETIQUETAS_VALIDAS.contains(e)) {
	                            System.out.println("❌ La etiqueta '" + e + "' no es válida. Inténtalo de nuevo.");
	                            todasValidas = false;
	                            break;
	                        }
	                        etiquetaSet.add(e);
	                    }

	                    if (todasValidas) {
	                        String finalEtiquetas = String.join(",", etiquetaSet);
	                        p = new Producto(p.getEdificio(), p.getCategoria(), finalEtiquetas, p.getNombre(), p.getPrecio());
	                        break;
	                    }
	                }

	                // Nombre
	                while (true) {
	                    System.out.print("Nuevo nombre (ENTER para mantener): ");
	                    String nuevoNombre = scanner.nextLine().trim();
	                    if (nuevoNombre.isEmpty()) break;
	                    if (nuevoNombre.length() < 1) {
	                        System.out.println("El nombre no puede estar vacío. Inténtalo de nuevo.");
	                    } else {
	                        p = new Producto(p.getEdificio(), p.getCategoria(), p.getEtiquetas(), nuevoNombre, p.getPrecio());
	                        break;
	                    }
	                }

	                // Precio
	                while (true) {
	                    System.out.print("Nuevo precio (ENTER para mantener): ");
	                    String nuevoPrecio = scanner.nextLine().replace(",", ".").trim();
	                    if (nuevoPrecio.isEmpty()) break;
	                    try {
	                        double precio = Double.parseDouble(nuevoPrecio);
	                        if (precio < 0) {
	                            System.out.println("El precio no puede ser negativo. Inténtalo de nuevo.");
	                        } else {
	                            p = new Producto(p.getEdificio(), p.getCategoria(), p.getEtiquetas(), p.getNombre(), precio);
	                            break;
	                        }
	                    } catch (NumberFormatException e) {
	                        System.out.println("Por favor, introduce un número válido.");
	                    }
	                }

	                productos.set(idx, p);
	                System.out.println("Producto modificado correctamente.");
	            }

	            // Opción E: Eliminar un producto de la lista. Pide el número, valida la entrada, y elimina.
	            case "E" -> {
	                int idx = -1;
	                while (true) {
	                    System.out.print("Número del producto a eliminar: ");
	                    String entrada = scanner.nextLine();
	                    try {
	                        idx = Integer.parseInt(entrada.trim()) - 1;
	                        if (idx >= 0 && idx < productos.size()) {
	                            break;
	                        } else {
	                        	System.out.println("");
	                            System.out.println("Número inválido. Inténtalo de nuevo.");
	                        }
	                    } catch (NumberFormatException e) {
	                    	System.out.println("");
	                        System.out.println("Por favor, introduce un número válido.");
	                    }
	                }

	                productos.remove(idx);
	                System.out.println("");
	                System.out.println("Producto eliminado correctamente.");
	            }
	            
	         // Opción S: Guarda los cambios en el archivo de productos/menús y sale de la edición.
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
