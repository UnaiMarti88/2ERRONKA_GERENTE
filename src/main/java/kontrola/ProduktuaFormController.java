package kontrola;

import DatuBasea.ProduktuakDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Produktuak;

public class ProduktuaFormController {

    @FXML private TextField txtIzena, txtPrezioa, txtStock;
    @FXML private ComboBox<String> cmbMota;

    private Produktuak editatzen;

    public void setProduktua(Produktuak p) {
        this.editatzen = p;
        if (p != null) {
            txtIzena.setText(p.getIzena());
            txtPrezioa.setText(String.valueOf(p.getPrezioa()));
            txtStock.setText(String.valueOf(p.getStock()));
            
            // Try to select the correct type
            String sql = "SELECT izena FROM produktuen_motak WHERE id = ?";
            try (java.sql.Connection conn = Util.Conn.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, p.getProduktuenMotakId());
                java.sql.ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    cmbMota.setValue(rs.getString("izena"));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @FXML
    public void initialize() {
        cmbMota.setItems(FXCollections.observableArrayList(ProduktuakDB.lortuMotak()));
        cmbMota.setEditable(true);
    }

    @FXML
    private void gordeProduktua() {
        String izena = txtIzena.getText().trim();
        String prezioStr = txtPrezioa.getText().trim();
        String stockStr = txtStock.getText().trim();
        String mota = cmbMota.getValue();

        if (izena.isEmpty() || prezioStr.isEmpty() || stockStr.isEmpty() || mota == null) {
            alerta("Bete beharrezko eremu guztiak, mota barne.");
            return;
        }

        try {
            if (editatzen == null) editatzen = new Produktuak();
            editatzen.setIzena(izena);
            editatzen.setPrezioa(Double.parseDouble(prezioStr));
            editatzen.setStock(Integer.parseInt(stockStr));
            editatzen.setProduktuenMotakId(ProduktuakDB.lortuMotaId(mota));

            if (editatzen.getId() == 0) {
                ProduktuakDB.gehituProduktua(editatzen);
            } else {
                ProduktuakDB.eguneratuProduktua(editatzen);
            }
            itxi();
        } catch (NumberFormatException e) {
            alerta("Zenbaki formatu okerra.");
        }
    }

    @FXML
    private void itxi() {
        ((Stage) txtIzena.getScene().getWindow()).close();
    }

    private void alerta(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }
}
