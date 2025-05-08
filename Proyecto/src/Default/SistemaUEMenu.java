package Default;

import model.*;

import java.io.*;
import java.util.*;

public class SistemaUEMenu {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Map<String, Usuario> usuarios = cargarUsuarios("Proyecto/Data/usuarios.txt");

        Usuario usuario = null;
        while (usuario == null) {
            limpiarPantalla(); // Limpia la consola antes de cada intento

            System.out.println("=== Inicio de Sesión ===");
            System.out.print("Nombre de usuario: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine().trim();

            usuario = autenticar(nombre, contrasena, usuarios);

            if (usuario == null) {
                System.out.println("\nUsuario o contraseña incorrectos. Intenta de nuevo.");
                System.out.println("\nPresiona ENTER para volver a intentar...");
                scanner.nextLine();
            }
        }

        limpiarPantalla();
        System.out.println("Bienvenido, " + usuario.getNombre() + " (" + usuario.getTipo() + ")");
        usuario.iniciar();
    }



    private static Map<String, Usuario> cargarUsuarios(String archivo) {
        Map<String, Usuario> mapa = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    String tipo = partes[0].trim();
                    String nombre = partes[1].trim();
                    String clave = partes[2].trim();

                    Usuario u = null;
                    switch (tipo) {
                        case "C":
                            u = new Consumidor(nombre, clave);
                            break;
                        case "A":
                            u = new Administrador(nombre, clave);
                            break;
                        case "G":
                            u = new Gerente(nombre, clave);
                            break;
                        default:
                            continue;
                    }

                    if (u != null) {
                        mapa.put(nombre, u);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer usuarios: " + e.getMessage());
        }

        return mapa;
    }

    private static Usuario autenticar(String nombre, String clave, Map<String, Usuario> usuarios) {
        if (usuarios.containsKey(nombre)) {
            Usuario u = usuarios.get(nombre);
            if (u.getContrasena().equals(clave)) {
                return u;
            }
        }
        return null;
    }

    private static void limpiarPantalla() {
        for (int i = 0; i < 30; i++) System.out.println();
    }

}
