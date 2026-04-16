package DatuBasea;

import model.Erabiltzailea;
import Util.Conn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginManager {

    public boolean login(Erabiltzailea erabiltzailea) {

        String sql = "SELECT id FROM langileak " +
            "WHERE (erabiltzailea = ? OR email = ?) AND pasahitza = ? AND baimena = 1";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, erabiltzailea.getErabiltzailea());
            stmt.setString(2, erabiltzailea.getErabiltzailea());
            stmt.setString(3, erabiltzailea.getPasahitza());

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
