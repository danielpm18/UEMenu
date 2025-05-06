package control;

import java.util.List;
import java.util.Scanner;

import static model.Menu.*;

public class SistemaUEMenu {

    static Scanner scanner = new Scanner(System.in);


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
}
