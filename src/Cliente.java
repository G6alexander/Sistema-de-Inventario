public class Cliente {
    private String nombre;
    private String dni;
    private String historial;

    public Cliente(String nombre, String dni, String historial) {
        this.nombre = nombre;
        this.dni = dni;
        this.historial = historial;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getHistorial() { return historial; }
    public void setHistorial(String historial) { this.historial = historial; }
}

