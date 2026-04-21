package DatuBasea;

import Util.Conn;
import model.Mahaia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahaiakDB {

    public static List<Mahaia> lortuMahaiak() {
        List<Mahaia> lista = new ArrayList<>();
        String sql = "SELECT id, izena, erabiltzailea, pasahitza, chat_baimena FROM mahaiak";

        try (Connection c = Conn.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String chatBaimenaStr = rs.getString("chat_baimena");
                int chatBaimena = (chatBaimenaStr != null && (chatBaimenaStr.equals("1") || chatBaimenaStr.equalsIgnoreCase("true"))) ? 1 : 0;
                
                lista.add(new Mahaia(
                        rs.getInt("id"),
                        rs.getString("izena"),
                        rs.getString("erabiltzailea"),
                        rs.getString("pasahitza"),
                        chatBaimena
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static int gehituMahai(Mahaia m) {
        String sql = "INSERT INTO mahaiak (izena, erabiltzailea, pasahitza, chat_baimena) VALUES (?, ?, ?, ?)";

        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getIzena());
            ps.setString(2, m.getErabiltzailea());
            ps.setString(3, m.getPasahitza());
            ps.setString(4, String.valueOf(m.getChatBaimena()));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void eguneratuMahai(Mahaia m) {
        String sql = "UPDATE mahaiak SET izena=?, erabiltzailea=?, pasahitza=?, chat_baimena=? WHERE id=?";

        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getIzena());
            ps.setString(2, m.getErabiltzailea());
            ps.setString(3, m.getPasahitza());
            ps.setString(4, String.valueOf(m.getChatBaimena()));
            ps.setInt(5, m.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateChatBaimena(int id, int nuevoEstado) {
        String sql = "UPDATE mahaiak SET chat_baimena = ? WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(nuevoEstado));
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean ezabatuMahai(int id) {
        String sql = "DELETE FROM mahaiak WHERE id=?";

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
}
