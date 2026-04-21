package kontrola;

import DatuBasea.MahaiakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Mahaia;

public class MahaiakController {

    @FXML private TableView<Mahaia> mahaiakTable;
    @FXML private TableColumn<Mahaia, String> colIzena;
    @FXML private TableColumn<Mahaia, String> colErabiltzailea;
    @FXML private TableColumn<Mahaia, String> colPasahitza;
    @FXML private TableColumn<Mahaia, Void> colChatAccess;
    @FXML private TextField txtBilatu;
    @FXML private Button btnAdd, btnEdit, btnDelete;

    private ObservableList<Mahaia> mahaiak;
    private FilteredList<Mahaia> filtratua;

    @FXML
    public void initialize() {
        colIzena.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIzena()));
        colErabiltzailea.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getErabiltzailea()));
        colPasahitza.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPasahitza()));

        // Botón toggle para el chat
        colChatAccess.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Mahaia m = getTableView().getItems().get(getIndex());
                    actualizarBotonChat(m);
                    btn.setOnAction(event -> {
                        int nuevoEstado = m.getChatBaimena() == 1 ? 0 : 1;
                        if (MahaiakDB.updateChatBaimena(m.getId(), nuevoEstado)) {
                            m.setChatBaimena(nuevoEstado);
                            actualizarBotonChat(m);
                        }
                    });
                    setGraphic(btn);
                }
            }

            private void actualizarBotonChat(Mahaia m) {
                if (m.getChatBaimena() == 1) {
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
            filtratua.setPredicate(m -> {
                if (val == null || val.isEmpty()) return true;
                String lower = val.toLowerCase();
                return m.getIzena().toLowerCase().contains(lower) || 
                       (m.getErabiltzailea() != null && m.getErabiltzailea().toLowerCase().contains(lower));
            });
        });

        mahaiakTable.setRowFactory(tv -> {
            TableRow<Mahaia> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Mahaia clicked = row.getItem();
                    irekiFormularioa(clicked);
                }
            });
            return row;
        });

        mahaiakTable.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> botoiakAktualizatu());
        botoiakAktualizatu();
    }

    private void kargatuDatuak() {
        mahaiak = FXCollections.observableArrayList(MahaiakDB.lortuMahaiak());
        filtratua = new FilteredList<>(mahaiak, m -> true);
        mahaiakTable.setItems(filtratua);
    }

    private void botoiakAktualizatu() {
        Mahaia sel = mahaiakTable.getSelectionModel().getSelectedItem();
        btnEdit.setDisable(sel == null);
        btnDelete.setDisable(sel == null);
    }

    @FXML
    private void gehituMahai() { irekiFormularioa(null); }

    @FXML
    private void editatuMahai() {
        Mahaia m = mahaiakTable.getSelectionModel().getSelectedItem();
        if (m != null) irekiFormularioa(m);
        else alerta("Hautatu mahai bat editatzeko.");
    }

    @FXML
    private void ezabatuMahai() {
        Mahaia m = mahaiakTable.getSelectionModel().getSelectedItem();
        if (m == null) { alerta("Hautatu mahai bat ezabatzeko."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Ziur zaude mahai hau ezabatu nahi duzula?", ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            boolean deleted = MahaiakDB.ezabatuMahai(m.getId());
            if (deleted) kargatuDatuak();
            else alerta("Errorea: ezin izan da mahai ezabatu.");
        }
    }

    private void irekiFormularioa(Mahaia m) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MahaiaForm.fxml"));
            Parent root = loader.load();

            MahaiaFormController controller = loader.getController();
            controller.setMahai(m);

            Stage stage = new Stage();
            stage.setTitle(m == null ? "Mahaia gehitu" : "Mahaia editatu");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            kargatuDatuak();
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea: ezin izan da formularioa ireki.");
        }
    }

    private void alerta(String mezua) {
        new Alert(Alert.AlertType.WARNING, mezua).showAndWait();
    }
}
