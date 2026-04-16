package kontrola;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Erabiltzailea;

import java.io.IOException;

public class LangileFormController {

    @FXML private TextField txtIzena;
    @FXML private TextField txtAbizena;
    @FXML private TextField txtErabiltzailea;
    @FXML private PasswordField txtPasahitza;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefonoa;
    @FXML private CheckBox chkAppAccess;
    @FXML private CheckBox chkChatAccess;

    private Erabiltzailea erabiltzailea;

    public static Erabiltzailea openForm(Erabiltzailea erabiltzailea) {
        try {
            FXMLLoader loader = new FXMLLoader(LangileFormController.class.getResource("/fxml/LangileForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle(erabiltzailea == null ? "Langile berria" : "Langilea editatu");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(LangileFormController.class.getResource("/css/estilo.css").toExternalForm());
            stage.setScene(scene);

            LangileFormController controller = loader.getController();
            controller.setErabiltzailea(erabiltzailea);

            stage.showAndWait();
            return controller.getErabiltzailea();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void initialize() {
    }

    public void setErabiltzailea(Erabiltzailea e) {
        if (e == null) {
            this.erabiltzailea = null;
            return;
        }

        this.erabiltzailea = e;
        txtIzena.setText(e.getIzena());
        txtAbizena.setText(e.getAbizena());
        txtErabiltzailea.setText(e.getErabiltzailea());
        txtPasahitza.setText(e.getPasahitza());
        txtEmail.setText(e.getEmail());
        txtTelefonoa.setText(e.getTelefonoa());
        chkAppAccess.setSelected(e.getBaimena() == 1);
        chkChatAccess.setSelected(e.getTxatBaimena() == 1);
    }

    public Erabiltzailea getErabiltzailea() {
        return erabiltzailea;
    }

    @FXML
    private void gordeLangilea() {
        String izena = txtIzena.getText().trim();
        String abizena = txtAbizena.getText().trim();
        String user = txtErabiltzailea.getText().trim();
        String pass = txtPasahitza.getText().trim();
        String email = txtEmail.getText().trim();
        String tlf = txtTelefonoa.getText().trim();

        if (izena.isEmpty() || abizena.isEmpty() || user.isEmpty() || pass.isEmpty() || email.isEmpty() || tlf.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Bete beharrezko eremu guztiak.").showAndWait();
            return;
        }

        if (erabiltzailea == null) {
            erabiltzailea = new Erabiltzailea();
        }

        erabiltzailea.setIzena(izena);
        erabiltzailea.setAbizena(abizena);
        erabiltzailea.setErabiltzailea(user);
        erabiltzailea.setPasahitza(pass);
        erabiltzailea.setEmail(email);
        erabiltzailea.setTelefonoa(tlf);
        erabiltzailea.setBaimena(chkAppAccess.isSelected() ? 1 : 0);
        erabiltzailea.setTxatBaimena(chkChatAccess.isSelected() ? 1 : 0);

        Stage stage = (Stage) txtIzena.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void itxi() {
        erabiltzailea = null;
        Stage stage = (Stage) txtIzena.getScene().getWindow();
        stage.close();
    }
}
