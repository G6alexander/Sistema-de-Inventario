
import java.sql.Connection;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Usuario> usuarios = new ArrayList<>();

    static ArrayList<LibroSimple> librosDisponibles = new ArrayList<>();
    static ArrayList<ProveedorSimple> proveedoresDisponibles = new ArrayList<>();

    public static void main(String[] args) {
        inicializarUsuarios();

        Connection conn = GestorBaseDatos.conectar();
        if (conn != null) {
            System.out.println("Conexion exitosa a la base de datos.");
        } else {
            System.out.println("Error al conectar con la base de datos.");
        }

        while (true) {
            System.out.println("=== SISTEMA DE INVENTARIO DE LIBRERIA ===");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Registrarse");
            System.out.print("Seleccione una opcion: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            if (opcion == 1) {
                if (iniciarSesion()) {
                    mostrarMenuPrincipal();
                }
            } else if (opcion == 2) {
                registrarUsuario();
            } else {
                System.out.println("Opcion invalida.");
            }
        }
    }

    static void cargarProveedoresDesdeBD() {
        proveedoresDisponibles.clear(); // Limpia antes de cargar

        try (Connection conn = GestorBaseDatos.conectar()) {
            String sql = "SELECT nombre, descripcion FROM proveedores";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                proveedoresDisponibles.add(new ProveedorSimple(nombre, descripcion));
            }

        } catch (Exception e) {
            System.out.println("Error al cargar proveedores desde BD: " + e.getMessage());
        }
    }

    static HashMap<String, Cliente> mapaClientes = new HashMap<>();

    static void mostrarProveedores() {
        cargarProveedoresDesdeBD();
        boolean seguir = true;
        while (seguir) {
            System.out.println("\n--- PROVEEDORES DISPONIBLES ---");
            for (int i = 0; i < proveedoresDisponibles.size(); i++) {
                System.out.println((i + 1) + ". " + proveedoresDisponibles.get(i).getNombre());
            }
            System.out.println((proveedoresDisponibles.size() + 1) + ". Volver");

            System.out.print("Seleccione un proveedor: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            if (opcion == proveedoresDisponibles.size() + 1) {
                seguir = false;
            } else if (opcion >= 1 && opcion <= proveedoresDisponibles.size()) {
                ProveedorSimple proveedor = proveedoresDisponibles.get(opcion - 1);
                System.out.println("\nInformación del proveedor:");
                System.out.println("Nombre: " + proveedor.getNombre());
                System.out.println("Descripcion: " + proveedor.getDescripcion());
            } else {
                System.out.println("Opcion invalida.");
            }
        }
    }

    static void inicializarUsuarios() {
        usuarios.add(new Usuario("admin", "admin", "1234", "admin"));
    }

    static boolean iniciarSesion() {
        System.out.print("Usuario: ");
        String user = scanner.nextLine().trim();  // eliminamos espacios
        System.out.print("Contrasena: ");
        String pass = scanner.nextLine().trim();

        System.out.println("Intentando iniciar sesión con: " + user + " / " + pass);

        try (Connection conn = GestorBaseDatos.conectar()) {
            if (conn != null) {
                String sql = "SELECT nombre, tipo FROM usuarios WHERE usuario = ? AND contraseña = ?";
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, user);
                stmt.setString(2, pass);

                java.sql.ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String tipo = rs.getString("tipo");
                    System.out.println("✅ Bienvenido " + nombre + " (" + tipo + ")");
                    return true;
                } else {
                    System.out.println(" Usuario o contraseña incorrectos.");
                    return false;
                }
            } else {
                System.out.println(" No se pudo conectar a la base de datos.");
                return false;
            }
        } catch (Exception e) {
            System.out.println(" Error al iniciar sesion: " + e.getMessage());
            return false;
        }
    }

    static void registrarUsuario() {
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contrasena: ");
        String pass = scanner.nextLine();
        System.out.print("Tipo de usuario (admin, cajero, etc): ");
        String tipo = scanner.nextLine();

        try (Connection conn = GestorBaseDatos.conectar()) {
            if (conn != null) {
                String sql = "INSERT INTO usuarios (nombre, usuario, contraseña, tipo) VALUES (?, ?, ?, ?)";
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setString(2, user);
                stmt.setString(3, pass);
                stmt.setString(4, tipo);
                stmt.executeUpdate();
                System.out.println("Usuario registrado con exito en la base de datos.");
            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }
        } catch (Exception e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
        }
    }

    static void registrarVenta() {
        try (Connection conn = GestorBaseDatos.conectar()) {
            System.out.print("ID del cliente: ");
            int idCliente = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            System.out.print("Método de pago (efectivo, tarjeta, etc.): ");
            String metodo = scanner.nextLine();

            System.out.print("Total de la venta: ");
            double total = scanner.nextDouble();
            scanner.nextLine();

            String sql = "INSERT INTO ventas (id_cliente, fecha, metodo_pago, total) VALUES (?, NOW(), ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCliente);
            stmt.setString(2, metodo);
            stmt.setDouble(3, total);

            stmt.executeUpdate();
            System.out.println(" Venta registrada con exito.");

        } catch (Exception e) {
            System.out.println(" Error al registrar venta: " + e.getMessage());
        }
    }

    static void actualizarStock() {
        try (Connection conn = GestorBaseDatos.conectar()) {
            System.out.print("ID del libro: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Nuevo stock: ");
            int nuevoStock = scanner.nextInt();
            scanner.nextLine();

            String sql = "UPDATE libros SET stock=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, id);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println(" Stock actualizado.");
            } else {
                System.out.println("️ Libro no encontrado.");
            }

        } catch (Exception e) {
            System.out.println(" Error al actualizar stock: " + e.getMessage());
        }
    }

    //MENU PRINCIPAL
    static void mostrarMenuPrincipal() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Buscar libro");
            System.out.println("2. Buscar proveedor");
            System.out.println("3. Buscar usuario");
            System.out.println("4. Buscar venta");
            System.out.println("5. Mostrar inventario");
            System.out.println("6. Registrar cliente");
            System.out.println("7. Mostrar cliente");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    GestorLibros.mostrarLibrosDesdeBD();
                    break;
                case 2:
                    mostrarProveedores();
                    break;
                case 3:
                    GestorClientes.menuClientes();
                    break;
                case 4:
                    registrarVenta();
                    break;
                case 5:
                    menuInventario();
                    break;
                case 6:
                    GestorClientes.registrarCliente();
                    break;
                case 7:
                    GestorClientes.mostrarClientes();
                    break;
                case 8:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    //INVENTARIO 
    static void menuInventario() {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n--- GESTION DE INVENTARIO ---");
            System.out.println("1. Agregar nuevo libro");
            System.out.println("2. Editar libro");
            System.out.println("3. Actualizar stock");
            System.out.println("4. Volver");

            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    GestorLibros.agregarLibro();
                    break;
                case 2:
                    GestorLibros.editarLibro();
                    break;
                case 3:
                    actualizarStock();
                    break;
                case 4:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }
}
