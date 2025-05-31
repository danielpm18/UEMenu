package model;

import java.util.*;

public class Gerente extends Administrador {

    public Gerente(String nombre, String contrasena) {
        super(nombre, contrasena);
    }

    @Override
    public String getTipo() {
        return "Gerente";
    }

    @Override
    public void iniciar() {
        while (true) {
            limpiarPantalla();
            mostrarCesta();
            System.out.println("=== Menú Gerente ===");
            System.out.println("1. Ver menús/productos por edificio");
            System.out.println("2. Eliminar producto de la cesta");
            System.out.println("3. Filtrar por etiquetas");
            System.out.println("4. Editar productos/menús");
            System.out.println("5. Ver estadísticas de ventas");
            System.out.println("6. Finalizar y ver recibo");
            System.out.print("Selecciona una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> seleccionarEdificio();
                case "2" -> cesta.eliminarProducto(scanner);
                case "3" -> filtrarPorEtiqueta();
                case "4" -> editarArchivos();
                case "5" -> Estadistica.mostrarEstadisticas();
                case "6" -> {
                    mostrarRecibo();
                    return;
                }
                default -> {}
            }
        }
    }
}
