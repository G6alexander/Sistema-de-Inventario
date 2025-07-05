import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

public class GestorClientes {
    static Scanner scanner = new Scanner(System.in);
    public static HashMap<String, Cliente> mapaClientes = new HashMap<>();

    public static void registrarCliente() {
        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("DNI del cliente: ");
        String dni = scanner.nextLine().trim();
        System.out.print("Historial: ");
        String historial = scanner.nextLine().trim();

        try (Connection conn = GestorBaseDatos.conectar()) {
            String sql = "INSERT INTO clientes (nombre, dni, historial) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, dni);
            stmt.setString(3, historial);
            stmt.executeUpdate();
            System.out.println("✅ Cliente registrado con éxito.");
        } catch (Exception e) {
            System.out.println("❌ Error al registrar cliente: " + e.getMessage());
        }
    }

    public static void cargarClientesDesdeBD() {
        mapaClientes.clear();
        try (Connection conn = GestorBaseDatos.conectar()) {
            String sql = "SELECT nombre, dni, historial FROM clientes";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String dni = rs.getString("dni");
                String historial = rs.getString("historial");
                Cliente cliente = new Cliente(nombre, dni, historial);
                mapaClientes.put(dni, cliente);
            }
            System.out.println("✅ Clientes cargados.");
        } catch (Exception e) {
            System.out.println("❌ Error al cargar clientes: " + e.getMessage());
        }
    }

    public static void buscarClientePorDNI() {
        if (mapaClientes.isEmpty()) cargarClientesDesdeBD();

        System.out.print("Ingrese DNI del cliente: ");
        String dni = scanner.nextLine();
        Cliente cliente = mapaClientes.get(dni);

        if (cliente != null) {
            System.out.println("✅ Cliente encontrado:");
            System.out.println("Nombre: " + cliente.getNombre());
            System.out.println("DNI: " + cliente.getDni());
            System.out.println("Historial: " + cliente.getHistorial());
        } else {
            System.out.println("❌ Cliente no encontrado.");
        }
    }

    public static void editarCliente() {
        System.out.print("Ingrese DNI del cliente a editar: ");
        String dni = scanner.nextLine();
        try (Connection conn = GestorBaseDatos.conectar()) {
            String sqlBuscar = "SELECT * FROM clientes WHERE dni = ?";
            PreparedStatement stmtBuscar = conn.prepareStatement(sqlBuscar);
            stmtBuscar.setString(1, dni);
            ResultSet rs = stmtBuscar.executeQuery();

            if (rs.next()) {
                System.out.print("Nuevo nombre: ");
                String nuevoNombre = scanner.nextLine();
                String sqlUpdate = "UPDATE clientes SET nombre = ? WHERE dni = ?";
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setString(1, nuevoNombre);
                stmtUpdate.setString(2, dni);
                stmtUpdate.executeUpdate();
                System.out.println("✅ Cliente actualizado.");
            } else {
                System.out.println("❌ Cliente no encontrado.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al editar cliente: " + e.getMessage());
        }
    }

    public static void eliminarCliente() {
        System.out.print("Ingrese DNI del cliente a eliminar: ");
        String dni = scanner.nextLine();
        try (Connection conn = GestorBaseDatos.conectar()) {
            String sql = "DELETE FROM clientes WHERE dni = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dni);
            int filas = stmt.executeUpdate();
            if (filas > 0) System.out.println("✅ Cliente eliminado.");
            else System.out.println("❌ Cliente no encontrado.");
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar cliente: " + e.getMessage());
        }
    }

    public static void mostrarClientes() {
        try (Connection conn = GestorBaseDatos.conectar()) {
            String sql = "SELECT nombre, dni, historial FROM clientes";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            int i = 1;
            System.out.println("\n--- CLIENTES REGISTRADOS ---");
            while (rs.next()) {
                System.out.println(i++ + ". Nombre: " + rs.getString("nombre"));
                System.out.println("   DNI: " + rs.getString("dni"));
                System.out.println("   Historial: " + rs.getString("historial"));
                System.out.println("--------------------------------");
            }
            if (i == 1) System.out.println("No hay clientes registrados.");
        } catch (Exception e) {
            System.out.println("❌ Error al mostrar clientes: " + e.getMessage());
        }
    }

    public static void menuClientes() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- GESTIÓN DE CLIENTES ---");
            System.out.println("1. Buscar cliente por DNI");
            System.out.println("2. Editar cliente");
            System.out.println("3. Eliminar cliente");
            System.out.println("4. Volver");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> buscarClientePorDNI();
                case 2 -> editarCliente();
                case 3 -> eliminarCliente();
                case 4 -> continuar = false;
                default -> System.out.println("Opción inválida.");
            }
        }
    }
}

