public class ProveedorSimple {
    private String nombre;
    private String descripcion;

    public ProveedorSimple(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return nombre + " – " + descripcion;
    }
}
