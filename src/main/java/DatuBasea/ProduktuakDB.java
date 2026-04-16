package DatuBasea;

import model.Produktuak;
import Util.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduktuakDB {

    public static List<Produktuak> lortuProduktuak() {
        List<Produktuak> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.izena, p.prezioa, p.stock, p.irudia, p.produktuen_motak_id, m.izena AS mota_izena " +
                     "FROM produktuak p JOIN produktuen_motak m ON p.produktuen_motak_id = m.id";

        try (Connection conn = Conn.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Produktuak p = new Produktuak();
                p.setId(rs.getInt("id"));
                p.setIzena(rs.getString("izena"));
                p.setPrezioa(rs.getDouble("prezioa"));
                p.setStock(rs.getInt("stock"));
                p.setIrudia(rs.getString("irudia"));
                p.setProduktuenMotakId(rs.getInt("produktuen_motak_id"));
                p.setMotaIzena(rs.getString("mota_izena"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static int gehituProduktua(Produktuak p) {
        String sql = "INSERT INTO produktuak (izena, prezioa, stock, irudia, produktuen_motak_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getIzena());
            ps.setDouble(2, p.getPrezioa());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getIrudia());
            ps.setInt(5, p.getProduktuenMotakId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        p.setId(idGenerado); 
                        return idGenerado;   
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void eguneratuProduktua(Produktuak p) {
        String sql = "UPDATE produktuak SET izena=?, prezioa=?, stock=?, irudia=?, produktuen_motak_id=? WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getIzena());
            ps.setDouble(2, p.getPrezioa());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getIrudia());
            ps.setInt(5, p.getProduktuenMotakId());
            ps.setInt(6, p.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean ezabatuProduktua(int id) {
        String sql = "DELETE FROM produktuak WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> lortuMotak() {
        List<String> motak = new ArrayList<>();
        String sql = "SELECT izena FROM produktuen_motak ORDER BY izena";

        try (Connection conn = Conn.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                motak.add(rs.getString("izena"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return motak;
    }

    public static int lortuMotaId(String izena) {
        String sql = "SELECT id FROM produktuen_motak WHERE izena = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, izena);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 6; // Default to 'Bebida' or similar if not found, based on previous SQL read
    }
}
