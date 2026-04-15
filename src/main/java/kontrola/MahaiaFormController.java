package kontrola;

import DatuBasea.MahaiakDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Mahaia;

public class MahaiaFormController {

    @FXML private TextField txtIzena;
    @FXML private ComboBox<String> cmbEgoera; 
    @FXML private Button btnGorde;
    @FXML private Button btnUtzi;

    private Mahaia editatzen;

    @FXML
    public void initialize() {
        cmbEgoera.setItems(FXCollections.observableArrayList(
                "Libre", "Hartzatuta", "Erreserbatuta"
        ));
        cmbEgoera.setEditable(true);
    }

    public void setMahai(Mahaia m) {
        this.editatzen = m;
        if (m != null) {
            txtIzena.setText(m.getIzena());
            cmbEgoera.setValue(m.getEgoera()); 
        }
    }

    @FXML
    private void gordeMahai() {
        String izena = txtIzena.getText().trim();
        String egoera = cmbEgoera.getValue();

        if (izena.isEmpty() || egoera == null || egoera.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Sartu izena eta egoera.").showAndWait();
            return;
        }

        if (editatzen == null) {
            int id = MahaiakDB.gehituMahai(new Mahaia(0, izena, egoera));
            if (id == -1) {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da mahai gorde.").showAndWait();
                return;
            }
        } else {
            editatzen.setIzena(izena);
            editatzen.setEgoera(egoera);
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
