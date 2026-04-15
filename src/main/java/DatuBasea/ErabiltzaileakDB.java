package DatuBasea;

import model.Erabiltzailea;
import Util.Conn;
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

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, erabiltzaileIzena);
            ps.setString(2, pasahitza);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Erabiltzailea> getAll() {
        List<Erabiltzailea> lista = new ArrayList<>();
        String sql = "SELECT * FROM langileak ORDER BY id";

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql);
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
            (izena, abizena, erabiltzailea, pasahitza, email, telefonoa, baimena, mahaiak_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql)) {
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
            pasahitza = ?, email = ?, telefonoa = ?, baimena = ?, mahaiak_id = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql)) {
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
            ps.setInt(9, e.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM langileak WHERE id = ?";

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Erabiltzailea mapResultSet(ResultSet rs) throws SQLException {
        Erabiltzailea e = new Erabiltzailea();
        e.setId(rs.getInt("id"));
        e.setIzena(rs.getString("izena"));
        e.setAbizena(rs.getString("abizena"));
        e.setErabiltzailea(rs.getString("erabiltzailea"));
        e.setPasahitza(rs.getString("pasahitza"));
        e.setEmail(rs.getString("email"));
        e.setTelefonoa(rs.getString("telefonoa"));
        e.setBaimena(rs.getInt("baimena"));
        int mId = rs.getInt("mahaiak_id");
        if (!rs.wasNull()) {
            e.setMahaiakId(mId);
        }
        return e;
    }
}
