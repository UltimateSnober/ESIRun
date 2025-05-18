package transport.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import transport.core.*;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AjoutUsagerController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private CheckBox handicapCheckBox;
    @FXML
    private CheckBox employeCheckBox;
    @FXML
    private TitledPane employeFields; // Change from VBox to TitledPane
    @FXML
    private TextField matriculeField;
    @FXML
    private ComboBox<String> fonctionComboBox;
    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        fonctionComboBox.getItems().addAll("AGENT", "CONDUCTEUR");
    }

    @FXML
    private void toggleEmployeFields() {
        boolean isChecked = employeCheckBox.isSelected();
        employeFields.setVisible(isChecked);
        employeFields.setManaged(isChecked);
    }

    @FXML
    private void ajouterUsager() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        boolean handicape = handicapCheckBox.isSelected();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null) {
            showErrorMessage("Veuillez remplir tous les champs obligatoires");
            return;
        }

        try {
            if (employeCheckBox.isSelected()) {
                String matricule = matriculeField.getText();
                if (matricule.isEmpty()) {
                    showErrorMessage("Veuillez entrer un matricule");
                    return;
                }

                String fonction = fonctionComboBox.getValue();
                if (fonction == null) {
                    showErrorMessage("Veuillez sélectionner une fonction");
                    return;
                }

                TypeFonction typeFonction = TypeFonction.valueOf(fonction);
                Employe employe = new Employe(nom, prenom, dateNaissance, handicape, matricule, typeFonction);
                Donnees.ajouterUsager(employe);
                showSuccessMessage("Employé ajouté avec succès avec ID: " + employe.getId());
            } else {
                Usager usager = new Usager(nom, prenom, dateNaissance, handicape);
                Donnees.ajouterUsager(usager);
                showSuccessMessage("Usager ajouté avec succès avec ID: " + usager.getId());
            }
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Erreur: " + e.getMessage());
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(null);
        handicapCheckBox.setSelected(false);
        employeCheckBox.setSelected(false);
        matriculeField.clear();
        fonctionComboBox.setValue(null);
        employeFields.setVisible(false);
        employeFields.setManaged(false);
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/transport/ui/accueil.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("ESIRun - Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display an error message in red
     *
     * @param message The error message to display
     */
    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("error-message");
    }

    /**
     * Display a success message in green
     *
     * @param message The success message to display
     */
    private void showSuccessMessage(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("success-message");
    }
}
