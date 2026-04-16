package DatuBasea;

import model.Platera;
import Util.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaterakDB {

    public static List<Platera> lortuGuztiak() {
        List<Platera> lista = new ArrayList<>();
        String sql = "SELECT id, izena, mota, perezioa FROM platerak";

        try (Connection conn = Conn.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Platera p = new Platera();
                p.setId(rs.getInt("id"));
                p.setIzena(rs.getString("izena"));
                p.setMota(rs.getString("mota"));
                p.setPrezioa(rs.getDouble("perezioa"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static int gehitu(Platera p) {
        String sql = "INSERT INTO platerak (izena, mota, perezioa) VALUES (?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getIzena());
            ps.setString(2, p.getMota());
            ps.setDouble(3, p.getPrezioa());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        p.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void eguneratu(Platera p) {
        String sql = "UPDATE platerak SET izena=?, mota=?, perezioa=? WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getIzena());
            ps.setString(2, p.getMota());
            ps.setDouble(3, p.getPrezioa());
            ps.setInt(4, p.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean ezabatu(int id) {
        String sql = "DELETE FROM platerak WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<model.Produktuak> lortuPlaterakoProduktuak(int plateraId) {
        List<model.Produktuak> lista = new ArrayList<>();
        String sql = """
            SELECT p.id, p.izena, p.prezioa, p.stock, p.irudia, p.produktuen_motak_id
            FROM produktuak p
            JOIN produktuak_has_platerak php ON p.id = php.produktuak_id
            WHERE php.platerak_id = ?
        """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plateraId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.Produktuak p = new model.Produktuak();
                p.setId(rs.getInt("id"));
                p.setIzena(rs.getString("izena"));
                p.setPrezioa(rs.getDouble("prezioa"));
                p.setStock(rs.getInt("stock"));
                p.setIrudia(rs.getString("irudia"));
                p.setProduktuenMotakId(rs.getInt("produktuen_motak_id"));
                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public static void ezabatuPlaterakoProduktuak(int plateraId) {
        String sql = "DELETE FROM produktuak_has_platerak WHERE platerak_id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plateraId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void gehituPlaterariProduktua(int plateraId, int produktuId) {
        String sql = "INSERT INTO produktuak_has_platerak (platerak_id, produktuak_id) VALUES (?, ?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, plateraId);
            ps.setInt(2, produktuId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
