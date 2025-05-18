package transport.control;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import transport.core.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;

public class AchatTitreController {

    @FXML
    private TextField idField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private VBox carteOptionsPane;

    @FXML
    private ComboBox<TypeCarte> typeCarteComboBox;

    @FXML
    private ComboBox<UsagerItem> usagerComboBox;

    @FXML
    private Label prixLabel;

    @FXML
    private Button acheterButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Label messageLabel;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        // Initialize type combo box
        typeComboBox.setItems(FXCollections.observableArrayList("Ticket", "Carte Navigation"));
        typeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateTypeSelection(newVal);
        });

        // Initialize carte type combo box
        typeCarteComboBox.setItems(FXCollections.observableArrayList(TypeCarte.values()));
        typeCarteComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                calculerPrix();
            }
        });

        // Hide carte options initially
        carteOptionsPane.setVisible(false);
        carteOptionsPane.setManaged(false);

        // Set default values
        typeComboBox.getSelectionModel().selectFirst();

        // Load users into the combo box - SIMPLIFIED APPROACH
        refreshUsagers();

        // Add listener to usager combobox to update UI
        usagerComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                messageLabel.setText("Usager sélectionné: " + newVal.toString());
            }
        });
    }

    /**
     * Refreshes the users list in the combo box - SIMPLIFIED
     */
    @FXML
    private void refreshUsagers() {
        List<Personne> usagers = Donnees.getUsagers();
        ObservableList<UsagerItem> items = FXCollections.observableArrayList();

        for (Personne p : usagers) {
            items.add(new UsagerItem(p));
        }

        usagerComboBox.setItems(items);
        usagerComboBox.setVisibleRowCount(Math.min(10, items.size())); // Show up to 10 rows

        // Show status message
        if (items.isEmpty()) {
            showErrorMessage("Aucun usager disponible. Veuillez d'abord ajouter des usagers.");
        } else {
            showInfoMessage("Liste des usagers actualisée (" + items.size() + " usagers disponibles)");
        }
    }

    /**
     * Updates UI based on selected ticket type.
     */
    private void updateTypeSelection(String selection) {
        // Check if usager list is empty and prompt - moved outside the if statements
        if (usagerComboBox.getItems().isEmpty()) {
            showErrorMessage("Attention: Aucun usager disponible. Veuillez d'abord ajouter un usager.");
        }

        if ("Carte Navigation".equals(selection)) {
            carteOptionsPane.setVisible(true);
            carteOptionsPane.setManaged(true);

            // Set a default ID if empty
            if (idField.getText().isEmpty()) {
                idField.setText(String.valueOf(generateId()));
            }
        } else {
            carteOptionsPane.setVisible(false);
            carteOptionsPane.setManaged(false);
            idField.clear(); // Clear ID for tickets as they autogenerate IDs
            prixLabel.setText("Prix: 50 DA");
        }

        calculerPrix();
    }

    /**
     * Calculates and displays the ticket price.
     */
    private void calculerPrix() {
        if ("Ticket".equals(typeComboBox.getValue())) {
            prixLabel.setText("Prix: 50 DA");
        } else if ("Carte Navigation".equals(typeComboBox.getValue())) {
            TypeCarte typeCarte = typeCarteComboBox.getValue();
            double prixBase = 5000;

            if (typeCarte != null) {
                double reduction = 0;

                switch (typeCarte) {
                    case JUNIOR ->
                        reduction = 0.7;
                    case SENIOR ->
                        reduction = 0.75;
                    case SOLIDARITE ->
                        reduction = 0.5;
                    case PARTENAIRE ->
                        reduction = 0.6;
                    default ->
                        reduction = 1.0;
                }

                double prixFinal = prixBase * reduction;
                prixLabel.setText(String.format("Prix: %.2f DA", prixFinal));
            } else {
                // If no card type is selected, use the base price of 5000 DA
                prixLabel.setText("Prix: 5000.00 DA");
            }
        }
    }

    /**
     * Validates the form inputs before purchase
     */
    private boolean validateForm() {
        // Check if a user is selected for both ticket types
        if (usagerComboBox.getValue() == null) {
            showErrorMessage("Veuillez sélectionner un usager.");
            return false;
        }

        if ("Carte Navigation".equals(typeComboBox.getValue())) {
            // Validate ID
            try {
                int id = Integer.parseInt(idField.getText());
                if (id <= 0) {
                    showErrorMessage("L'ID doit être un nombre positif.");
                    return false;
                }

                // Check for duplicate IDs
                if (Donnees.trouverTitreParId(id) != null) {
                    showErrorMessage("Cet ID existe déjà. Veuillez choisir un autre ID.");
                    return false;
                }
            } catch (NumberFormatException e) {
                showErrorMessage("ID invalide. Veuillez entrer un nombre entier.");
                return false;
            }
        }

        return true;
    }

    /**
     * Handles the purchase action.
     */
    @FXML
    private void acheter() {
        try {
            // Clear any previous messages
            clearMessage();

            // Validate form
            if (!validateForm()) {
                return;
            }

            // Get selected user - moved outside the if statements since both types need it
            UsagerItem usagerItem = usagerComboBox.getValue();
            if (usagerItem == null) {
                showErrorMessage("Veuillez sélectionner un usager.");
                return;
            }

            Personne personne = usagerItem.getPersonne();
            TitreTransport titre = null;

            if ("Ticket".equals(typeComboBox.getValue())) {
                // Create ticket with the selected person
                titre = new Ticket(personne);
                Donnees.ajouterTitre(titre);
                showSuccessMessage("Ticket acheté avec succès! ID: " + titre.getId()
                        + " - Prix: 50 DA - Usager: " + personne.getPrenom() + " " + personne.getNom());
            } else if ("Carte Navigation".equals(typeComboBox.getValue())) {
                // Rest of the code for CarteNavigation remains the same
                TypeCarte typeCarte = typeCarteComboBox.getValue();
                int id = Integer.parseInt(idField.getText());

                // Create the navigation card with the person
                titre = new CarteNavigation(id, typeCarte, personne);
                // Add the title to the data store
                Donnees.ajouterTitre(titre);

                // Calculate price for display
                double prix = 5000.0;
                if (typeCarte != null) {
                    double reduction = 1.0;
                    switch (typeCarte) {
                        case JUNIOR ->
                            reduction = 0.7;
                        case SENIOR ->
                            reduction = 0.75;
                        case SOLIDARITE ->
                            reduction = 0.5;
                        case PARTENAIRE ->
                            reduction = 0.6;
                    }
                    prix = 5000.0 * reduction;
                }

                // Message based on whether type is selected
                if (typeCarte == null) {
                    showSuccessMessage("Carte Navigation standard achetée avec succès! ID: " + id
                            + " - Prix: 5000.00 DA - Associée à: " + personne.getPrenom() + " " + personne.getNom());
                } else {
                    showSuccessMessage(String.format("Carte Navigation %s achetée avec succès! ID: %d - Prix: %.2f DA - Associée à: %s %s",
                            typeCarte, id, prix, personne.getPrenom(), personne.getNom()));
                }
            }

            // Reset form after successful purchase
            resetForm();
        } catch (Exception e) {
            showErrorMessage("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Resets the form fields.
     */
    private void resetForm() {
        typeComboBox.getSelectionModel().selectFirst();
        idField.clear();
        usagerComboBox.setValue(null);
        typeCarteComboBox.setValue(null);
        carteOptionsPane.setVisible(false);
        carteOptionsPane.setManaged(false);
        calculerPrix();
    }

    /**
     * Generates a random ID for cartes.
     */
    private int generateId() {
        int id;
        do {
            id = 1000 + (int) (Math.random() * 9000);
        } while (Donnees.trouverTitreParId(id) != null); // Ensure ID is unique
        return id;
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

    /**
     * Display an informational message in blue
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

    // Helper class for the combo box items
    private static class UsagerItem {

        private final Personne personne;

        public UsagerItem(Personne personne) {
            this.personne = personne;
        }

        public Personne getPersonne() {
            return personne;
        }

        @Override
        public String toString() {
            return personne.getId() + " - " + personne.getPrenom() + " " + personne.getNom();
        }
    }
}
