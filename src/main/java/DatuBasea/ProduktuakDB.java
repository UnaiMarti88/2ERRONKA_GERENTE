package DatuBasea;

import model.Produktuak;
import Util.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduktuakDB {

    public static List<Produktuak> lortuProduktuak() {
        List<Produktuak> lista = new ArrayList<>();
        String sql = "SELECT id, izena, prezioa, stock, stock_min, stock_max, irudia, produktuen_motak_id FROM produktuak";

        try (Connection conn = Conn.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Produktuak p = new Produktuak();
                p.setId(rs.getInt("id"));
                p.setIzena(rs.getString("izena"));
                p.setPrezioa(rs.getDouble("prezioa"));
                p.setStock(rs.getInt("stock"));
                p.setStockMin(rs.getInt("stock_min"));
                p.setStockMax(rs.getInt("stock_max"));
                p.setIrudia(rs.getString("irudia"));
                p.setProduktuenMotakId(rs.getInt("produktuen_motak_id"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static int gehituProduktua(Produktuak p) {
        String sql = "INSERT INTO produktuak (izena, prezioa, stock, stock_min, stock_max, irudia, produktuen_motak_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getIzena());
            ps.setDouble(2, p.getPrezioa());
            ps.setInt(3, p.getStock());
            if (p.getStockMin() != null) ps.setInt(4, p.getStockMin()); else ps.setNull(4, Types.INTEGER);
            if (p.getStockMax() != null) ps.setInt(5, p.getStockMax()); else ps.setNull(5, Types.INTEGER);
            ps.setString(6, p.getIrudia());
            ps.setInt(7, p.getProduktuenMotakId());

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
        String sql = "UPDATE produktuak SET izena=?, prezioa=?, stock=?, stock_min=?, stock_max=?, irudia=?, produktuen_motak_id=? WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getIzena());
            ps.setDouble(2, p.getPrezioa());
            ps.setInt(3, p.getStock());
            if (p.getStockMin() != null) ps.setInt(4, p.getStockMin()); else ps.setNull(4, Types.INTEGER);
            if (p.getStockMax() != null) ps.setInt(5, p.getStockMax()); else ps.setNull(5, Types.INTEGER);
            ps.setString(6, p.getIrudia());
            ps.setInt(7, p.getProduktuenMotakId());
            ps.setInt(8, p.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ezabatuProduktua(int id) {
        String sql = "DELETE FROM produktuak WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
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
}
