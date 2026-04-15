package kontrola;

import DatuBasea.PlaterakDB;
import DatuBasea.ProduktuakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Platera;
import model.Produktuak;

import java.util.List;

public class PlateraFormController {

    @FXML private TextField txtIzena, txtMota, txtPrezioa;
    @FXML private ComboBox<String> cmbKategoria;
    @FXML private TableView<Produktuak> produktuTaula;
    @FXML private TableColumn<Produktuak, String> colProduktuIzena;

    private final ObservableList<Produktuak> platerakoProduktuak = FXCollections.observableArrayList();
    private Platera editatzen;

    public void setPlatera(Platera p) {
        this.editatzen = p;
        if (p != null) {
            txtIzena.setText(p.getIzena());
            txtMota.setText(p.getMota());
            txtPrezioa.setText(String.valueOf(p.getPrezioa()));
            platerakoProduktuak.setAll(PlaterakDB.lortuPlaterakoProduktuak(p.getId()));
            
            
            
        }
    }

    @FXML
    public void initialize() {
        cmbKategoria.setItems(FXCollections.observableArrayList(PlaterakDB.lortuPlateraMotak()));
        cmbKategoria.setEditable(true);

        colProduktuIzena.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIzena()));
        produktuTaula.setItems(platerakoProduktuak);
    }

    @FXML
    private void gehituProduktua() {
        List<Produktuak> guztiak = ProduktuakDB.lortuProduktuak();
        ChoiceDialog<Produktuak> dialog = new ChoiceDialog<>(null, guztiak);
        dialog.setTitle("Gehitu produktua");
        dialog.setHeaderText("Aukeratu platerari gehitzeko produktua");
        dialog.showAndWait().ifPresent(p -> {
            if (!platerakoProduktuak.contains(p)) {
                platerakoProduktuak.add(p);
            }
        });
    }

    @FXML
    private void kenduProduktua() {
        Produktuak p = produktuTaula.getSelectionModel().getSelectedItem();
        if (p != null) platerakoProduktuak.remove(p);
    }

    @FXML
    private void gordePlatera() {
        String izena = txtIzena.getText().trim();
        String mota = txtMota.getText().trim();
        String prezioStr = txtPrezioa.getText().trim();

        if (izena.isEmpty() || mota.isEmpty() || prezioStr.isEmpty()) {
            alerta("Bete beharrezko eremu guztiak.");
            return;
        }

        try {
            if (editatzen == null) editatzen = new Platera();
            editatzen.setIzena(izena);
            editatzen.setMota(mota);
            editatzen.setPrezioa(Double.parseDouble(prezioStr));
            editatzen.setPlateraMotakId(1); // Default category

            int id;
            if (editatzen.getId() == 0) {
                id = PlaterakDB.gehitu(editatzen);
            } else {
                id = editatzen.getId();
                PlaterakDB.eguneratu(editatzen);
            }

            if (id != -1) {
                PlaterakDB.ezabatuPlaterakoProduktuak(id);
                for (Produktuak p : platerakoProduktuak) {
                    PlaterakDB.gehituPlaterariProduktua(id, p.getId());
                }
                itxi();
            }
        } catch (NumberFormatException e) {
            alerta("Prezioa ez da zenbaki balioduna.");
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
