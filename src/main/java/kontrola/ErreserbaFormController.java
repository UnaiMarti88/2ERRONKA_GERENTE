package kontrola;

import DatuBasea.ErabiltzaileakDB;
import DatuBasea.MahaiakDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Erabiltzailea;
import model.Erreserba;
import model.Mahaia;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class ErreserbaFormController {

    @FXML private DatePicker dpEguna;
    @FXML private TextField txtOrdua;
    @FXML private ComboBox<Integer> cmbMota;
    @FXML private ComboBox<Mahaia> cmbMahaia;

    private Erreserba erreserba;

    public static Erreserba openForm(Erreserba erreserba) {
        try {
            FXMLLoader loader = new FXMLLoader(ErreserbaFormController.class.getResource("/fxml/ErreserbaForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle(erreserba == null ? "Erreserba gehitu" : "Erreserba editatu");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(ErreserbaFormController.class.getResource("/css/estilo.css").toExternalForm());
            stage.setScene(scene);

            ErreserbaFormController controller = loader.getController();
            controller.kargatuAukerak();
            controller.setErreserba(erreserba);

            stage.showAndWait();
            return controller.getErreserba();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void kargatuAukerak() {
        List<Mahaia> mahaiak = MahaiakDB.lortuMahaiak();
        cmbMahaia.setItems(FXCollections.observableArrayList(mahaiak));
        cmbMota.setItems(FXCollections.observableArrayList(0, 1)); // 0: Local, 1: Online?

        cmbMahaia.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Mahaia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIzena() + " - " + item.getEgoera());
            }
        });
        cmbMahaia.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Mahaia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIzena() + " - " + item.getEgoera());
            }
        });
    }

    public void setErreserba(Erreserba erreserba) {
        if (erreserba == null) {
            this.erreserba = null;
            dpEguna.setValue(LocalDate.now());
            txtOrdua.setText("14:00");
            return;
        }

        this.erreserba = erreserba;
        dpEguna.setValue(erreserba.getData().toLocalDate());
        txtOrdua.setText(erreserba.getData().toLocalTime().toString());
        cmbMota.setValue(erreserba.getMota());

        cmbMahaia.getItems().stream()
                .filter(mahaia -> mahaia.getId() == this.erreserba.getMahaiakId())
                .findFirst()
                .ifPresent(cmbMahaia::setValue);
    }

    public Erreserba getErreserba() {
        return erreserba;
    }

    @FXML
    private void gordeErreserba() {
        String orduaText = txtOrdua.getText().trim();
        LocalDate eguna = dpEguna.getValue();

        if (orduaText.isEmpty() || eguna == null || cmbMahaia.getValue() == null || cmbMota.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Bete beharrezko eremu guztiak.").showAndWait();
            return;
        }

        LocalTime ordua;
        try {
            ordua = LocalTime.parse(orduaText);
        } catch (DateTimeParseException e) {
            new Alert(Alert.AlertType.WARNING, "Orduak HH:mm formatua izan behar du.").showAndWait();
            return;
        }

        if (erreserba == null) {
            erreserba = new Erreserba();
        }

        erreserba.setData(LocalDateTime.of(eguna, ordua));
        erreserba.setMota(cmbMota.getValue());
        erreserba.setMahaiakId(cmbMahaia.getValue().getId());

        Stage stage = (Stage) dpEguna.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void itxi() {
        erreserba = null;
        Stage stage = (Stage) dpEguna.getScene().getWindow();
        stage.close();
    }
}
        erreserba.setPrezioTotala(prezioTotala);
        erreserba.setOrdainduta(chkOrdainduta.isSelected());
        erreserba.setFakturaRuta(txtFakturaRuta.getText().trim().isEmpty() ? null : txtFakturaRuta.getText().trim());
        erreserba.setLangileaId(cmbLangilea.getValue().getId());
        erreserba.setMahaiaId(cmbMahaia.getValue().getId());
        itxi();
    }

    @FXML
    private void itxi() {
        Stage stage = (Stage) txtBezeroIzena.getScene().getWindow();
        stage.close();
    }
}