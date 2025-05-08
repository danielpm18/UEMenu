package model;

public abstract class Usuario {
    protected String nombre;
    protected String contrasena;

    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public abstract String getTipo();

    public abstract void iniciar();  // Este método se implementará distinto en cada subclase
}
