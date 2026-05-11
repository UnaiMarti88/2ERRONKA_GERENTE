package DatuBasea;

import Util.Conn;
import model.Kategoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KategoriakDB {

    
    public static List<Kategoria> lortuKategoriak() {
        List<Kategoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM produktuen_motak";

        try (Connection c = Conn.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Kategoria(rs.getInt("id"), rs.getString("izena")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    
    public static Map<Integer, String> lortuKategoriakMap() {
        Map<Integer, String> mapa = new HashMap<>();
        for (Kategoria k : lortuKategoriak()) {
            mapa.put(k.getId(), k.getIzena());
        }
        return mapa;
    }

    
    public static int gehituKategoria(Kategoria k) {
        String sql = "INSERT INTO produktuen_motak (izena) VALUES (?)";

        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, k.getIzena());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    
    public static void eguneratuKategoria(Kategoria k) {
        String sql = "UPDATE produktuen_motak SET izena=? WHERE id=?";

        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, k.getIzena());
            ps.setInt(2, k.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static boolean dagoProdukturik(int kategoriaId) {
        String sql = "SELECT COUNT(*) FROM produktuak WHERE produktuen_motak_id=?";
        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, kategoriaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public static boolean ezabatuKategoria(int id) {
        if (dagoProdukturik(id)) {
            return false; 
        }

        String sql = "DELETE FROM produktuen_motak WHERE id=?";
        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public static List<Kategoria> lortuKategoriak(String filtro) {
        List<Kategoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM produktuen_motak WHERE izena LIKE ?";

        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + filtro + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Kategoria(rs.getInt("id"), rs.getString("izena")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
