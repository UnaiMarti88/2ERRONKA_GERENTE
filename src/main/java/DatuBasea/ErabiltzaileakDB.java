package DatuBasea;

import model.Erabiltzailea;
import Util.Conn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ErabiltzaileakDB {

    public Erabiltzailea login(String erabiltzaileIzena, String pasahitza) {
        String sql = """
            SELECT * FROM langileak
            WHERE erabiltzailea = ? AND pasahitza = ?
        """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, erabiltzaileIzena);
            ps.setString(2, pasahitza);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Erabiltzailea> getAll() {
        List<Erabiltzailea> lista = new ArrayList<>();
        String sql = "SELECT id, izena, abizena, erabiltzailea, pasahitza, email, telefonoa, baimena, mahaiak_id, chat_baimena FROM langileak ORDER BY id";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insert(Erabiltzailea e) {
        String sql = """
            INSERT INTO langileak
            (izena, abizena, erabiltzailea, pasahitza, email, telefonoa, baimena, mahaiak_id, chat_baimena)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getIzena());
            ps.setString(2, e.getAbizena());
            ps.setString(3, e.getErabiltzailea());
            ps.setString(4, e.getPasahitza());
            ps.setString(5, e.getEmail());
            ps.setString(6, e.getTelefonoa());
            ps.setInt(7, e.getBaimena());
            if (e.getMahaiakId() != null) {
                ps.setInt(8, e.getMahaiakId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.setInt(9, e.getTxatBaimena());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Erabiltzailea e) {
        String sql = """
            UPDATE langileak SET
            izena = ?, abizena = ?, erabiltzailea = ?,
            pasahitza = ?, email = ?, telefonoa = ?, baimena = ?, mahaiak_id = ?, chat_baimena = ?
            WHERE id = ?
        """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getIzena());
            ps.setString(2, e.getAbizena());
            ps.setString(3, e.getErabiltzailea());
            ps.setString(4, e.getPasahitza());
            ps.setString(5, e.getEmail());
            ps.setString(6, e.getTelefonoa());
            ps.setInt(7, e.getBaimena());
            if (e.getMahaiakId() != null) {
                ps.setInt(8, e.getMahaiakId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.setInt(9, e.getTxatBaimena());
            ps.setInt(10, e.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM langileak WHERE id = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Erabiltzailea mapResultSet(ResultSet rs) throws SQLException {
        return new Erabiltzailea(
            rs.getInt("id"),
            rs.getString("izena"),
            rs.getString("abizena"),
            rs.getString("erabiltzailea"),
            rs.getString("pasahitza"),
            rs.getString("email"),
            rs.getString("telefonoa"),
            rs.getInt("baimena"),
            rs.getObject("mahaiak_id") != null ? rs.getInt("mahaiak_id") : null,
            rs.getInt("chat_baimena")
        );
    }

    public boolean updateChatBaimena(int langileaId, int baimena) {
        String sql = "UPDATE langileak SET chat_baimena = ? WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, baimena);
            ps.setInt(2, langileaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAppBaimena(int langileaId, int baimena) {
        String sql = "UPDATE langileak SET baimena = ? WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, baimena);
            ps.setInt(2, langileaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
