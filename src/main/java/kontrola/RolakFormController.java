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
    @FXML private TableColumn<Erabiltzailea, String> colRola;
    @FXML private TableColumn<Erabiltzailea, Void> colChat;

    @FXML private TextField txtBilatu;

    private ObservableList<Erabiltzailea> langileak;
    private FilteredList<Erabiltzailea> filtratua;
    private final ErabiltzaileakDB db = new ErabiltzaileakDB();

    @FXML
    public void initialize() {
        colIzena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        colAbizena.setCellValueFactory(new PropertyValueFactory<>("abizena"));
        colErabiltzailea.setCellValueFactory(new PropertyValueFactory<>("erabiltzailea"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRola.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRola()));

        // Botón toggle para el chat
        colChat.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Erabiltzailea e = getTableView().getItems().get(getIndex());
                    actualizarBoton(e);
                    btn.setOnAction(event -> {
                        int nuevoEstado = e.getTxatBaimena() == 1 ? 0 : 1;
                        if (db.updateChatBaimena(e.getId(), nuevoEstado)) {
                            e.setTxatBaimena(nuevoEstado);
                            actualizarBoton(e);
                        }
                    });
                    setGraphic(btn);
                }
            }

            private void actualizarBoton(Erabiltzailea e) {
                if (e.getTxatBaimena() == 1) {
                    btn.setText("✅ Gaituta");
                    btn.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #166534; -fx-font-size: 11;");
                } else {
                    btn.setText("❌ Desgaituta");
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
    }

    private void kargatuDatuak() {
        langileak = FXCollections.observableArrayList(db.getAll());
        filtratua = new FilteredList<>(langileak, p -> true);
        langileakTable.setItems(filtratua);
    }
}
