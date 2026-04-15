package kontrola;

import DatuBasea.ProduktuakDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Produktuak;

public class ProduktuaFormController {

    @FXML private TextField txtIzena, txtPrezioa, txtStock, txtStockMin, txtStockMax;
    @FXML private ComboBox<String> cmbMota;

    private Produktuak editatzen;

    public void setProduktua(Produktuak p) {
        this.editatzen = p;
        if (p != null) {
            txtIzena.setText(p.getIzena());
            txtPrezioa.setText(String.valueOf(p.getPrezioa()));
            txtStock.setText(String.valueOf(p.getStock()));
            txtStockMin.setText(p.getStockMin() != null ? String.valueOf(p.getStockMin()) : "");
            txtStockMax.setText(p.getStockMax() != null ? String.valueOf(p.getStockMax()) : "");
            
            
            
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

        if (izena.isEmpty() || prezioStr.isEmpty() || stockStr.isEmpty()) {
            alerta("Bete beharrezko eremuak.");
            return;
        }

        try {
            if (editatzen == null) editatzen = new Produktuak();
            editatzen.setIzena(izena);
            editatzen.setPrezioa(Double.parseDouble(prezioStr));
            editatzen.setStock(Integer.parseInt(stockStr));
            editatzen.setStockMin(txtStockMin.getText().isEmpty() ? null : Integer.parseInt(txtStockMin.getText()));
            editatzen.setStockMax(txtStockMax.getText().isEmpty() ? null : Integer.parseInt(txtStockMax.getText()));
            editatzen.setProduktuenMotakId(1); // Default category

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
