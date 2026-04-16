package kontrola;

import DatuBasea.ProduktuakDB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Produktuak;

public class ProduktuakController {

    @FXML private TableView<Produktuak> produktuTable;
    @FXML private TableColumn<Produktuak, String> colIzena;
    @FXML private TableColumn<Produktuak, Double> colPrezioa;
    @FXML private TableColumn<Produktuak, Integer> colStock;

    @FXML private TextField txtBilatu;
    @FXML private ComboBox<String> cmbMotak;

    @FXML private Button btnAdd, btnEdit, btnDelete;

    private ObservableList<Produktuak> produktuak;
    private FilteredList<Produktuak> filtratua;

    @FXML
    public void initialize() {
        colIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIzena()));
        colPrezioa.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPrezioa()));
        colStock.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStock()));

        produktuak = FXCollections.observableArrayList(ProduktuakDB.lortuProduktuak());
        filtratua = new FilteredList<>(produktuak, p -> true);
        produktuTable.setItems(filtratua);

        ObservableList<String> motak = FXCollections.observableArrayList("Guztiak");
        motak.addAll(ProduktuakDB.lortuMotak());
        cmbMotak.setItems(motak);
        cmbMotak.getSelectionModel().selectFirst();

        txtBilatu.textProperty().addListener((obs, old, val) -> aplikatuFiltro());
        cmbMotak.valueProperty().addListener((obs, old, val) -> aplikatuFiltro());

        produktuTable.setRowFactory(tv -> {
            TableRow<Produktuak> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    produktuTable.getSelectionModel().select(row.getItem());
                    editatuProduktua();
                }
            });
            return row;
        });

        produktuTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, val) -> actualizarBotones());

        actualizarBotones();
    }

    private void aplikatuFiltro() {
        String testua = txtBilatu.getText().toLowerCase();
        String mota = cmbMotak.getValue();

        filtratua.setPredicate(p -> {
            boolean testuaOndo = testua.isEmpty() || p.getIzena().toLowerCase().contains(testua);
            boolean motaOndo = mota == null || mota.equals("Guztiak") || (p.getMotaIzena() != null && p.getMotaIzena().equals(mota));
            return testuaOndo && motaOndo;
        });
    }

    private void actualizarBotones() {
        Produktuak sel = produktuTable.getSelectionModel().getSelectedItem();
        btnEdit.setDisable(sel == null);
        btnDelete.setDisable(sel == null);
    }

    @FXML
    private void gehituProduktua() {
        irekiFormulario(null);
    }

    @FXML
    private void editatuProduktua() {
        Produktuak p = produktuTable.getSelectionModel().getSelectedItem();
        if (p != null) irekiFormulario(p);
    }

    @FXML
    private void ezabatuProduktua() {
        Produktuak p = produktuTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Produktua ezabatu");
            confirm.setHeaderText("Ziur zaude '" + p.getIzena() + "' ezabatu nahi duzula?");
            confirm.setContentText("Ekintza hau ezin da desegin.");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (ProduktuakDB.ezabatuProduktua(p.getId())) {
                    berritu();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Errorea");
                    error.setHeaderText("Ezin izan da produktua ezabatu");
                    error.setContentText("Baliteke produktua plater baten edo eskaera baten parte izatea.");
                    error.showAndWait();
                }
            }
        }
    }

    private void irekiFormulario(Produktuak p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/produktua_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);
            ProduktuaFormController ctrl = loader.getController();
            ctrl.setProduktua(p);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(p == null ? "Produktua gehitu" : "Produktua editatu");
            stage.showAndWait();
            berritu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void berritu() {
        produktuak.setAll(ProduktuakDB.lortuProduktuak());
    }
}
