package kontrola;

import DatuBasea.PlaterakDB;
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
import model.Platera;

public class PlaterakController {

    @FXML private TableView<Platera> plateraTable;
    @FXML private TableColumn<Platera, String> colIzena;
    @FXML private TableColumn<Platera, String> colMota;
    @FXML private TableColumn<Platera, Double> colPrezioa;

    @FXML private TextField txtBilatu;
    @FXML private ComboBox<String> cmbMotak;

    @FXML private Button btnAdd, btnEdit, btnDelete;

    private ObservableList<Platera> platerak;
    private FilteredList<Platera> filtratua;

    @FXML
    public void initialize() {
        colIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIzena()));
        colMota.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMota()));
        colPrezioa.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPrezioa()));

        platerak = FXCollections.observableArrayList(PlaterakDB.lortuGuztiak());
        filtratua = new FilteredList<>(platerak, p -> true);
        plateraTable.setItems(filtratua);

        cmbMotak.getItems().setAll(PlaterakDB.lortuPlateraMotak());

        txtBilatu.textProperty().addListener((obs, old, val) -> aplikatuFiltro());
        cmbMotak.valueProperty().addListener((obs, old, val) -> aplikatuFiltro());

        plateraTable.setRowFactory(tv -> {
            TableRow<Platera> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    plateraTable.getSelectionModel().select(row.getItem());
                    editatuPlatera();
                }
            });
            return row;
        });

        plateraTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, val) -> actualizarBotones());

        actualizarBotones();
    }

    private void aplikatuFiltro() {
        String testua = txtBilatu.getText().toLowerCase();

        filtratua.setPredicate(p -> {
            boolean testuaOndo = testua.isEmpty() || p.getIzena().toLowerCase().contains(testua);
            return testuaOndo;
        });
    }

    private void actualizarBotones() {
        Platera sel = plateraTable.getSelectionModel().getSelectedItem();
        btnEdit.setDisable(sel == null);
        btnDelete.setDisable(sel == null);
    }

    @FXML
    private void gehituPlatera() {
        irekiFormulario(null);
    }

    @FXML
    private void editatuPlatera() {
        Platera p = plateraTable.getSelectionModel().getSelectedItem();
        if (p != null) irekiFormulario(p);
    }

    @FXML
    private void ezabatuPlatera() {
        Platera p = plateraTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            PlaterakDB.ezabatu(p.getId());
            berritu();
        }
    }

    private void irekiFormulario(Platera p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/platera_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);
            PlateraFormController ctrl = loader.getController();
            ctrl.setPlatera(p);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(p == null ? "Platera gehitu" : "Platera editatu");
            stage.showAndWait();
            berritu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void berritu() {
        platerak.setAll(PlaterakDB.lortuGuztiak());
    }
}
