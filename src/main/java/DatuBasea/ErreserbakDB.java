package DatuBasea;

import Util.Conn;
import model.Erreserba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ErreserbakDB {

    public static List<Erreserba> lortuGuztiak() {
        List<Erreserba> lista = new ArrayList<>();
        String sql = "SELECT id, data, mota, erabiltzaileak_id, mahaiak_id FROM erreserbak ORDER BY data DESC";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Erreserba e = new Erreserba();
                e.setId(rs.getInt("id"));
                Timestamp ts = rs.getTimestamp("data");
                if (ts != null) e.setData(ts.toLocalDateTime());
                e.setMota(rs.getInt("mota"));
                int ebId = rs.getInt("erabiltzaileak_id");
                if (!rs.wasNull()) e.setErabiltzaileakId(ebId);
                e.setMahaiakId(rs.getInt("mahaiak_id"));
                lista.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean insert(Erreserba e) {
        String sql = "INSERT INTO erreserbak (data, mota, erabiltzaileak_id, mahaiak_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setTimestamp(1, e.getData() != null ? Timestamp.valueOf(e.getData()) : null);
            pst.setInt(2, e.getMota());
            if (e.getErabiltzaileakId() != null) {
                pst.setInt(3, e.getErabiltzaileakId());
            } else {
                pst.setNull(3, Types.INTEGER);
            }
            pst.setInt(4, e.getMahaiakId());
            return pst.executeUpdate() == 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean update(Erreserba e) {
        String sql = "UPDATE erreserbak SET data=?, mota=?, erabiltzaileak_id=?, mahaiak_id=? WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setTimestamp(1, e.getData() != null ? Timestamp.valueOf(e.getData()) : null);
            pst.setInt(2, e.getMota());
            if (e.getErabiltzaileakId() != null) {
                pst.setInt(3, e.getErabiltzaileakId());
            } else {
                pst.setNull(3, Types.INTEGER);
            }
            pst.setInt(4, e.getMahaiakId());
            pst.setInt(5, e.getId());
            return pst.executeUpdate() == 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM erreserbak WHERE id = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean dagoErabilita(int id) {
        String sql = "SELECT COUNT(*) AS guztira FROM eskaerak WHERE zerbitzua_id = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt("guztira") > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}