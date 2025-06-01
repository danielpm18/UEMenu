package model;

//Clase abstracta Usuario: define los atributos y métodos comunes para todos los usuarios del sistema.
//No se puede crear una instancia directa de Usuario; se debe usar una subclase.
public abstract class Usuario {
	// Atributos protegidos para almacenar el nombre de usuario y la contraseña.
    protected String nombre;
    protected String contrasena;

    // Constructor: inicializa el nombre de usuario y la contraseña.
    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    // Getter para obtener el nombre del usuario.
    public String getNombre() {
        return nombre;
    }

    // Getter para obtener la contraseña del usuario.
    public String getContrasena() {
        return contrasena;
    }

    // Método abstracto getTipo(): será implementado en cada subclase para devolver el tipo de usuario (Consumidor, Administrador, Gerente).
    public abstract String getTipo();

    // Método abstracto iniciar(): cada subclase debe definir cómo se inicia sesión y qué opciones tiene el usuario.
    public abstract void iniciar();  // Este método se implementará distinto en cada subclase
}
