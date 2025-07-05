import java.util.ArrayList;
import java.util.Date;

public class Venta {
    private Cliente cliente;
    private ArrayList<Libro> librosVendidos;
    private double total;
    private Date fecha;
    private String metodoPago;

    public Venta(Cliente cliente, ArrayList<Libro> librosVendidos, String metodoPago) {
        this.cliente = cliente;
        this.librosVendidos = librosVendidos;
        this.metodoPago = metodoPago;
        this.fecha = new Date();
        this.total = calcularTotal();
    }

    private double calcularTotal() {
        double suma = 0;
        for (Libro libro : librosVendidos) {
            suma += libro.getPrecio();
        }
        return suma;
    }

    @Override
    public String toString() {
        return "Venta a " + cliente + " por $" + total + " - Fecha: " + fecha + " - Pago: " + metodoPago;
    }
}

