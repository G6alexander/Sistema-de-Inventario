public class Usuario {
    private String nombre;
    private String usuario;
    private String contraseña;
    private String tipo;

    public Usuario(String nombre, String usuario, String contraseña, String tipo) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public String getUsuario() { return usuario; }
    public String getContraseña() { return contraseña; }
    public String getTipo() { return tipo; }
}


