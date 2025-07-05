import java.util.ArrayList;

public class Proveedor {
    private String nombre;
    private String ruc;
    private String direccion;
    private String contacto;
    private ArrayList<Libro> libros;

    public Proveedor(String nombre, String ruc, String direccion, String contacto) {
        this.nombre = nombre;
        this.ruc = ruc;
        this.direccion = direccion;
        this.contacto = contacto;
        this.libros = new ArrayList<>();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    
    
    @Override
    public String toString() {
        return "Proveedor: " + nombre + " - Contacto: " + contacto;
    }
}

