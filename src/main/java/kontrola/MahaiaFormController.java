package kontrola;

import DatuBasea.MahaiakDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Mahaia;

public class MahaiaFormController {

    @FXML private TextField txtIzena;
    @FXML private TextField txtErabiltzailea;
    @FXML private PasswordField txtPasahitza;
    @FXML private CheckBox chkChatBaimena;
    @FXML private Button btnGorde;
    @FXML private Button btnUtzi;

    private Mahaia editatzen;

    @FXML
    public void initialize() {
    }

    public void setMahai(Mahaia m) {
        this.editatzen = m;
        if (m != null) {
            txtIzena.setText(m.getIzena());
            txtErabiltzailea.setText(m.getErabiltzailea());
            txtPasahitza.setText(m.getPasahitza());
            chkChatBaimena.setSelected(m.getChatBaimena() == 1);
        }
    }

    @FXML
    private void gordeMahai() {
        String izena = txtIzena.getText().trim();
        String erabiltzailea = txtErabiltzailea.getText().trim();
        String pasahitza = txtPasahitza.getText().trim();
        int chatBaimena = chkChatBaimena.isSelected() ? 1 : 0;

        if (izena.isEmpty() || erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Sartu izena, erabiltzailea eta pasahitza.").showAndWait();
            return;
        }

        if (editatzen == null) {
            int id = MahaiakDB.gehituMahai(new Mahaia(0, izena, erabiltzailea, pasahitza, chatBaimena));
            if (id == -1) {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da mahai gorde.").showAndWait();
                return;
            }
        } else {
            editatzen.setIzena(izena);
            editatzen.setErabiltzailea(erabiltzailea);
            editatzen.setPasahitza(pasahitza);
            editatzen.setChatBaimena(chatBaimena);
            MahaiakDB.eguneratuMahai(editatzen);
        }

        itxi();
    }

    @FXML
    private void itxi() {
        Stage stage = (Stage) txtIzena.getScene().getWindow();
        stage.close();
    }
}
