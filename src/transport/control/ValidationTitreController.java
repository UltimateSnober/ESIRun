package transport.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import transport.core.*;
import transport.data.Donnees;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ValidationTitreController {

    @FXML
    private TextField idField;

    @FXML
    private TitledPane resultPane;

    @FXML
    private Label validationLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label dateAchatLabel;

    @FXML
    private Label prixLabel;

    @FXML
    private Label usagerLabel;

    @FXML
    private Label validiteLabel;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        // Reset the form
        resetForm();
    }

    @FXML
    private void validerTitre() {
        // Clear previous messages
        clearMessage();

        try {
            // Get ID from field
            if (idField.getText().isEmpty()) {
                showErrorMessage("Veuillez entrer un ID de titre.");
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idField.getText().trim());
            } catch (NumberFormatException e) {
                showErrorMessage("ID invalide. Veuillez entrer un nombre entier.");
                return;
            }

            // Find title with this ID
            TitreTransport titre = Donnees.trouverTitreParId(id);

            if (titre == null) {
                showErrorMessage("Aucun titre trouvé avec l'ID: " + id);
                resultPane.setVisible(false);
                resultPane.setManaged(false);
                return;
            }

            // Check validity using the isValid method from the TitreTransport class
            LocalDateTime now = LocalDateTime.now();
            boolean isValid = titre.isValid(now);

            // Format date for display
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String dateStr = titre.getDateAchat().format(formatter);

            // Update UI with title information
            typeLabel.setText(titre instanceof Ticket ? "Ticket" : "Carte Navigation");
            dateAchatLabel.setText(dateStr);
            prixLabel.setText(String.format("%.2f DA", titre.getPrix()));

            // Get owner information
            Personne personne = null;
            if (titre instanceof Ticket) {
                personne = ((Ticket) titre).getPersonne();
            } else if (titre instanceof CarteNavigation) {
                personne = ((CarteNavigation) titre).getPersonne();
            }

            if (personne != null) {
                usagerLabel.setText(personne.getPrenom() + " " + personne.getNom());
            } else {
                usagerLabel.setText("Inconnu");
            }

            // Update validity info
            if (isValid) {
                validationLabel.setText("✅ TITRE VALIDE");
                validationLabel.getStyleClass().clear();
                validationLabel.getStyleClass().add("success-message");
                validationLabel.getStyleClass().add("large-text");

                // Calculate expiration info
                LocalDateTime expiration;
                if (titre instanceof Ticket) {
                    expiration = titre.getDateAchat().plusDays(1);
                    validiteLabel.setText("Valide jusqu'au " + expiration.format(formatter));
                } else if (titre instanceof CarteNavigation) {
                    expiration = titre.getDateAchat().plusYears(1);
                    validiteLabel.setText("Valide jusqu'au " + expiration.format(formatter));
                }

                showSuccessMessage("Le titre est valide.");
            } else {
                validationLabel.setText("❌ TITRE EXPIRÉ");
                validationLabel.getStyleClass().clear();
                validationLabel.getStyleClass().add("error-message");
                validationLabel.getStyleClass().add("large-text");
                validiteLabel.setText("Ce titre a expiré");

                showErrorMessage("Le titre a expiré et n'est plus valide.");
            }

            // Show results section
            resultPane.setVisible(true);
            resultPane.setManaged(true);
            resultPane.setExpanded(true);

        } catch (Exception e) {
            showErrorMessage("Erreur lors de la validation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToAchatTitre(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/transport/ui/achatTitre.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("ESIRun - Achat de Titres");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void resetForm() {
        idField.clear();
        resultPane.setVisible(false);
        resultPane.setManaged(false);
        clearMessage();
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

    /**
     * Display an information message in blue
     *
     * @param message The info message to display
     */
    private void showInfoMessage(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("info-message");
    }

    /**
     * Clear any message
     */
    private void clearMessage() {
        messageLabel.setText("");
        messageLabel.getStyleClass().clear();
    }
}
