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
        System.out.print("Selecciona edificio (A-E) o VOLVER: ");
        String edificio = scanner.nextLine().toUpperCase();
        if (edificio.equals("VOLVER")) return;
        if (!List.of("A", "B", "C", "D", "E").contains(edificio)) return;

        System.out.print("¿Editar MENUS o PRODUCTOS?: ");
        String tipo = scanner.nextLine().toUpperCase();
        if (!tipo.equals("MENUS") && !tipo.equals("PRODUCTOS")) return;

        String archivo = "Proyecto/Data/" + (tipo.equals("MENUS") ? "MenuEdificio" : "ProductoEdificio") + edificio + ".txt";
        List<Producto> productos = Producto.leerDesdeArchivo(archivo, edificio, tipo);

        while (true) {
            limpiarPantalla();
            System.out.println("=== Editar " + tipo + " del edificio " + edificio + " ===");
            for (int i = 0; i < productos.size(); i++) {
                System.out.println(i + ": " + productos.get(i));
            }
            System.out.println("A - Añadir nuevo | M - Modificar | E - Eliminar | S - Salir");
            System.out.print("Elige opción: ");
            String opcion = scanner.nextLine().toUpperCase();

            switch (opcion) {
                case "A" -> {
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Etiquetas (separadas por coma): ");
                    String etiquetas = scanner.nextLine();
                    System.out.print("Precio: ");
                    double precio = Double.parseDouble(scanner.nextLine());
                    productos.add(new Producto(edificio, tipo, etiquetas, nombre, precio));
                }
                case "M" -> {
                    System.out.print("Número del producto a modificar: ");
                    int idx = Integer.parseInt(scanner.nextLine());
                    if (idx >= 0 && idx < productos.size()) {
                        Producto p = productos.get(idx);
                        System.out.println("Modificando: " + p);
                        System.out.print("Nuevo nombre (ENTER para mantener): ");
                        String nuevoNombre = scanner.nextLine();
                        if (!nuevoNombre.isEmpty()) p = new Producto(p.getEdificio(), p.getCategoria(), p.getEtiquetas(), nuevoNombre, p.getPrecio());

                        System.out.print("Nuevas etiquetas (ENTER para mantener): ");
                        String nuevasEtiquetas = scanner.nextLine();
                        if (!nuevasEtiquetas.isEmpty()) p = new Producto(p.getEdificio(), p.getCategoria(), nuevasEtiquetas, p.getNombre(), p.getPrecio());

                        System.out.print("Nuevo precio (ENTER para mantener): ");
                        String nuevoPrecio = scanner.nextLine();
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
