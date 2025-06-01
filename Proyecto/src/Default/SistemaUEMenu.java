package Default;

import model.*;

import java.io.*;
import java.util.*;

public class SistemaUEMenu {

    static Scanner scanner = new Scanner(System.in);

    // Método principal que arranca la aplicación.
    // Pide al usuario iniciar sesión, valida el acceso y ejecuta el menú del usuario según su rol.
    public static void main(String[] args) {
    	// Carga el archivo de usuarios y crea un mapa con sus datos.
    	Map<String, Usuario> usuarios = cargarUsuarios("Proyecto/Data/usuarios.txt");

    	// Bucle para el proceso de inicio de sesión: pide el usuario y la contraseña hasta que se introduzca correctamente.
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
     // Una vez que el usuario se autentica, limpia la pantalla, muestra un saludo, y ejecuta el menú del usuario según su rol.
        limpiarPantalla();
        System.out.println("Bienvenido, " + usuario.getNombre() + " (" + usuario.getTipo() + ")");
        usuario.iniciar();
    }


 // Método para cargar los usuarios desde el archivo 'usuarios.txt'.
 // Cada línea del archivo contiene el tipo (Consumidor, Administrador o Gerente), el nombre, 
    //y la contraseña, separados por punto y coma.
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
 // Método para autenticar a un usuario: comprueba si el nombre existe en el mapa
 // y si la contraseña coincide. Si es correcto, devuelve el objeto Usuario. Si no, devuelve null.
    private static Usuario autenticar(String nombre, String clave, Map<String, Usuario> usuarios) {
        if (usuarios.containsKey(nombre)) {
            Usuario u = usuarios.get(nombre);
            if (u.getContrasena().equals(clave)) {
                return u;
            }
        }
        return null;
    }

 // Método para limpiar la pantalla: imprime 40 líneas en blanco para dar la sensación de una pantalla limpia.
    private static void limpiarPantalla() {
        for (int i = 0; i < 40; i++) System.out.println();
    }

}
