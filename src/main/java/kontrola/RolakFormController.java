package kontrola;

import DatuBasea.ErabiltzaileakDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Erabiltzailea;

public class RolakFormController {

    @FXML private TableView<Erabiltzailea> langileakTable;
    @FXML private TableColumn<Erabiltzailea, String> colIzena;
    @FXML private TableColumn<Erabiltzailea, String> colAbizena;
    @FXML private TableColumn<Erabiltzailea, String> colErabiltzailea;
    @FXML private TableColumn<Erabiltzailea, String> colEmail;
    @FXML private TableColumn<Erabiltzailea, Void> colAppAccess;
    @FXML private TableColumn<Erabiltzailea, Void> colChatAccess;

    @FXML private TextField txtBilatu;
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;

    private ObservableList<Erabiltzailea> langileak;
    private FilteredList<Erabiltzailea> filtratua;
    private final ErabiltzaileakDB db = new ErabiltzaileakDB();

    @FXML
    public void initialize() {
        colIzena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        colAbizena.setCellValueFactory(new PropertyValueFactory<>("abizena"));
        colErabiltzailea.setCellValueFactory(new PropertyValueFactory<>("erabiltzailea"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Botón toggle para el acceso a la aplicación
        colAppAccess.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Erabiltzailea e = getTableView().getItems().get(getIndex());
                    actualizarBotonApp(e);
                    btn.setOnAction(event -> {
                        int nuevoEstado = e.getBaimena() == 1 ? 0 : 1;
                        if (db.updateAppBaimena(e.getId(), nuevoEstado)) {
                            e.setBaimena(nuevoEstado);
                            actualizarBotonApp(e);
                        }
                    });
                    setGraphic(btn);
                }
            }

            private void actualizarBotonApp(Erabiltzailea e) {
                if (e.getBaimena() == 1) {
                    btn.setText("✅ Bai");
                    btn.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #166534; -fx-font-size: 11;");
                } else {
                    btn.setText("❌ Ez");
                    btn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-font-size: 11;");
                }
                btn.setPrefWidth(100);
            }
        });

        // Botón toggle para el chat
        colChatAccess.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Erabiltzailea e = getTableView().getItems().get(getIndex());
                    actualizarBotonChat(e);
                    btn.setOnAction(event -> {
                        int nuevoEstado = e.getTxatBaimena() == 1 ? 0 : 1;
                        if (db.updateChatBaimena(e.getId(), nuevoEstado)) {
                            e.setTxatBaimena(nuevoEstado);
                            actualizarBotonChat(e);
                        }
                    });
                    setGraphic(btn);
                }
            }

            private void actualizarBotonChat(Erabiltzailea e) {
                if (e.getTxatBaimena() == 1) {
                    btn.setText("✅ Bai");
                    btn.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #166534; -fx-font-size: 11;");
                } else {
                    btn.setText("❌ Ez");
                    btn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-font-size: 11;");
                }
                btn.setPrefWidth(100);
            }
        });

        kargatuDatuak();

        txtBilatu.textProperty().addListener((obs, old, val) -> {
            filtratua.setPredicate(l -> {
                if (val == null || val.isEmpty()) return true;
                String lower = val.toLowerCase();
                return l.getIzena().toLowerCase().contains(lower) || 
                       l.getAbizena().toLowerCase().contains(lower) ||
                       l.getErabiltzailea().toLowerCase().contains(lower);
            });
        });

        btnEdit.disableProperty().bind(langileakTable.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(langileakTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void kargatuDatuak() {
        langileak = FXCollections.observableArrayList(db.getAll());
        filtratua = new FilteredList<>(langileak, p -> true);
        langileakTable.setItems(filtratua);
    }

    @FXML
    private void gehituLangilea() {
        Erabiltzailea e = LangileFormController.openForm(null);
        if (e != null) {
            if (db.insert(e)) {
                kargatuDatuak();
            } else {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da langilea gehitu.").showAndWait();
            }
        }
    }

    @FXML
    private void editatuLangilea() {
        Erabiltzailea selected = langileakTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Erabiltzailea e = LangileFormController.openForm(selected);
        if (e != null) {
            if (db.update(e)) {
                kargatuDatuak();
            } else {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da langilea editatu.").showAndWait();
            }
        }
    }

    @FXML
    private void ezabatuLangilea() {
        Erabiltzailea selected = langileakTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Langilea ezabatu");
        confirm.setHeaderText("Ziur zaude '" + selected.getIzena() + " " + selected.getAbizena() + "' ezabatu nahi duzula?");
        confirm.setContentText("Ekintza hau ezin da desegin.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (db.delete(selected.getId())) {
                kargatuDatuak();
            } else {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da langilea ezabatu. Baliteke beste datu batzuekin lotuta egotea.").showAndWait();
            }
        }
    }
}
