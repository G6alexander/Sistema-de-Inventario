
public class Libro {

    private int id;
    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private String genero;
    private double precio;
    private int stock;

    
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // Constructor
    public Libro(int id, String isbn, String titulo, String autor, String editorial, String genero, double precio, int stock) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.genero = genero;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters (puedes agregar setters si los necesitas)
    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getStock() {
        return stock;
    }

    public double getPrecio() {
        return precio;
    }
}
