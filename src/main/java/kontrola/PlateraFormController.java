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
import java.util.stream.Collectors;

public class PlateraFormController {

    @FXML private TextField txtIzena, txtMota, txtPrezioa;
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
        colProduktuIzena.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIzena()));
        produktuTaula.setItems(platerakoProduktuak);
    }

    @FXML
    private void gehituProduktua() {
        List<Produktuak> guztiak = ProduktuakDB.lortuProduktuak().stream()
                .filter(p -> p.getProduktuenMotakId() == 8)
                .collect(Collectors.toList());

        if (guztiak.isEmpty()) {
            alerta("Ez da osagai motako produkturik aurkitu.");
            return;
        }

        Dialog<Produktuak> dialog = new Dialog<>();
        dialog.setTitle("Gehitu osagaia");
        dialog.setHeaderText("Aukeratu platerari gehitzeko produktua");

        ButtonType gehituType = new ButtonType("Gehitu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(gehituType, ButtonType.CANCEL);

        ListView<Produktuak> listView = new ListView<>(FXCollections.observableArrayList(guztiak));
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Produktuak item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIzena());
            }
        });

        dialog.getDialogPane().setContent(listView);

        javafx.scene.Node okButton = dialog.getDialogPane().lookupButton(gehituType);
        okButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());

        dialog.setResultConverter(buttonType -> buttonType == gehituType ? listView.getSelectionModel().getSelectedItem() : null);

        dialog.showAndWait().ifPresent(p -> {
            if (p != null && !platerakoProduktuak.stream().anyMatch(existing -> existing.getId() == p.getId())) {
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

            if (PlaterakDB.gordePlatera(editatzen, platerakoProduktuak)) {
                itxi();
            } else {
                alerta("Ezin izan da platera gorde datu-basean.");
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
