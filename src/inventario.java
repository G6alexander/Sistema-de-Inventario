import java.util.ArrayList;

public class inventario {
    private ArrayList<Libro> libros;

    public inventario() {
        libros = new ArrayList<>();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    public void eliminarLibroPorTitulo(String titulo) {
    libros.removeIf(libro -> libro.getTitulo().equalsIgnoreCase(titulo));
}


    public Libro buscarLibro(String titulo) {
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                return libro;
            }
        }
        return null;
    }

    public void mostrarInventario() {
        for (Libro libro : libros) {
            System.out.println(libro);
        }
    }
}
