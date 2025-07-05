public class LibroSimple {
    private String isbn;
    private String titulo;
    private String autor;

    public LibroSimple(String isbn ,String titulo, String autor) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    @Override
    public String toString() {
        return titulo + " – " + autor;
    }
}
