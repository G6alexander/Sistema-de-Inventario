import java.util.Scanner;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class GestorLibros {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<LibroSimple> librosDisponibles = new ArrayList<>();
    
    
    static void editarLibro() {
    try (Connection conn = GestorBaseDatos.conectar()) {
        System.out.print("ID del libro a editar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nuevo título: ");
        String titulo = scanner.nextLine();

        System.out.print("Nuevo autor: ");
        String autor = scanner.nextLine();

        System.out.print("Nuevo editorial: ");
        String editorial = scanner.nextLine();

        System.out.print("Nuevo género: ");
        String genero = scanner.nextLine();

        System.out.print("Nuevo precio: ");
        double precio = scanner.nextDouble();
        scanner.nextLine();

        String sql = "UPDATE libros SET titulo=?, autor=?, editorial=?, genero=?, precio=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, titulo);
        stmt.setString(2, autor);
        stmt.setString(3, editorial);
        stmt.setString(4, genero);
        stmt.setDouble(5, precio);
        stmt.setInt(6, id);

        int filas = stmt.executeUpdate();
        if (filas > 0) {
            System.out.println("✅ Libro actualizado.");
        } else {
            System.out.println("⚠️ Libro no encontrado.");
        }

    } catch (Exception e) {
        System.out.println("❌ Error al editar libro: " + e.getMessage());
    }
}
    
   static void agregarLibro() {
    try (Connection conn = GestorBaseDatos.conectar()) {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Autor: ");
        String autor = scanner.nextLine();

        System.out.print("Editorial: ");
        String editorial = scanner.nextLine();

        System.out.print("Género: ");
        String genero = scanner.nextLine();

        System.out.print("Precio: ");
        double precio = scanner.nextDouble();

        System.out.print("Stock inicial: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO libros (isbn, titulo, autor, editorial, genero, precio, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, isbn);
        stmt.setString(2, titulo);
        stmt.setString(3, autor);
        stmt.setString(4, editorial);
        stmt.setString(5, genero);
        stmt.setDouble(6, precio);
        stmt.setInt(7, stock);

        stmt.executeUpdate();
        System.out.println("✅ Libro agregado al inventario.");

    } catch (Exception e) {
        System.out.println("❌ Error al agregar libro: " + e.getMessage());
    }
} 
    
    static void mostrarLibrosDesdeBD() {
    try (Connection conn = GestorBaseDatos.conectar()) {
        String sql = "SELECT id, isbn, titulo, autor, editorial, genero, precio, stock FROM libros";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        System.out.println("\n--- LIBROS DISPONIBLES ---");
        int i = 1;
        while (rs.next()) {
            System.out.println(i++ + ". " + rs.getString("titulo") + " – " + rs.getString("autor"));
            System.out.println("   Editorial: " + rs.getString("editorial"));
            System.out.println("   Género: " + rs.getString("genero"));
            System.out.println("   Precio: S/" + rs.getDouble("precio"));
            System.out.println("   Stock: " + rs.getInt("stock"));
            System.out.println("--------------------------------");
        }

        if (i == 1) {
            System.out.println("No hay libros registrados.");
        }

    } catch (Exception e) {
        System.out.println("❌ Error al mostrar libros: " + e.getMessage());
    }
}
    static void cargarLibrosDesdeBD() {
    librosDisponibles.clear(); // Limpia la lista antes de cargar

    try (Connection conn = GestorBaseDatos.conectar()) {
        String sql = "SELECT isbn ,titulo, autor FROM libros";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String isbn = rs.getString("isbn");
            String titulo = rs.getString("titulo");
            String autor = rs.getString("autor");
            librosDisponibles.add(new LibroSimple(isbn,titulo, autor));
        }

    } catch (Exception e) {
        System.out.println("❌ Error al cargar libros desde BD: " + e.getMessage());
    }
}
    
    static void mostrarLibros() {
        cargarLibrosDesdeBD();
        boolean seguir = true;
        while (seguir) {
            System.out.println("\n--- LIBROS DISPONIBLES ---");
            for (int i = 0; i < librosDisponibles.size(); i++) {
                System.out.println((i + 1) + ". " + librosDisponibles.get(i));
            }
            System.out.println((librosDisponibles.size() + 1) + ". Volver");

            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            if (opcion == librosDisponibles.size() + 1) {
                seguir = false;
            } else if (opcion >= 1 && opcion <= librosDisponibles.size()) {
                LibroSimple libro = librosDisponibles.get(opcion - 1);
                System.out.println("Has seleccionado: " + libro.getTitulo() + " por " + libro.getAutor());
            } else {
                System.out.println("Opción inválida.");
            }
        }
    }

   
}

