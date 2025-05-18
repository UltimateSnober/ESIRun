package transport.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class AccueilController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Bienvenue dans ESIRun - Système de Gestion de Transport");
    }

    @FXML
    private void navigateToAjoutUsager(ActionEvent event) {
        navigateToPage("ajoutUsager.fxml", "Gestion des Usagers", event);
    }

    @FXML
    private void navigateToAchatTitre(ActionEvent event) {
        navigateToPage("achatTitre.fxml", "Achat de Titres", event);
    }

    @FXML
    private void navigateToValidationTitre(ActionEvent event) {
        navigateToPage("validationTitre.fxml", "Validation de Titres", event);
    }

    @FXML
    private void navigateToStatistiques(ActionEvent event) {
        navigateToPage("statistiques.fxml", "Statistiques", event);
    }

    @FXML
    private void navigateToListUsager(ActionEvent event) {
        navigateToPage("listUsager.fxml", "Liste des Usagers", event);
    }

    @FXML
    private void navigateToListTitres(ActionEvent event) {
        navigateToPage("listTitres.fxml", "Liste des Titres", event);
    }

    /**
     * Navigates to the reclamation screen.
     *
     * @param event The action event that triggered this navigation
     */
    @FXML
    private void navigateToReclamation(ActionEvent event) {
        navigateToPage("reclamation.fxml", "Réclamations", event);
    }

    /**
     * Navigates to the specified page.
     *
     * @param fxmlFile The FXML file name to load
     * @param title The title for the new view
     * @param event The action event that triggered this navigation
     */
    private void navigateToPage(String fxmlFile, String title, ActionEvent event) {
        try {
            // Load the FXML file
            Parent page = FXMLLoader.load(getClass().getResource("/transport/ui/" + fxmlFile));

            // Get the Stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new scene with the loaded page
            Scene scene = new Scene(page);

            // Set the new scene
            stage.setScene(scene);
            stage.setTitle("ESIRun - " + title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxmlFile + ": " + e.getMessage());
        }
    }
}
