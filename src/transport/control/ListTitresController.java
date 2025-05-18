package transport.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import transport.core.*;

import java.io.IOException;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class ListTitresController {

    @FXML
    private TableView<TitreDisplay> titresTable;

    @FXML
    private TableColumn<TitreDisplay, Integer> idColumn;

    @FXML
    private TableColumn<TitreDisplay, String> typeColumn;

    @FXML
    private TableColumn<TitreDisplay, String> dateColumn;

    @FXML
    private TableColumn<TitreDisplay, String> prixColumn;

    @FXML
    private TableColumn<TitreDisplay, String> usagerColumn;

    @FXML
    private TableColumn<TitreDisplay, String> detailsColumn;

    @FXML
    private ComboBox<String> filtreTypeComboBox;

    @FXML
    private Label totalLabel;

    private ObservableList<TitreDisplay> allTitres;
    private FilteredList<TitreDisplay> filteredTitres;

    @FXML
    public void initialize() {
        // Initialize filter options
        filtreTypeComboBox.getItems().addAll("Tous", "Ticket", "Carte Navigation");
        filtreTypeComboBox.setValue("Tous");
        filtreTypeComboBox.setOnAction(event -> filtrerListe());

        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAchat"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        usagerColumn.setCellValueFactory(new PropertyValueFactory<>("usager"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));

        // Load data
        refreshList();

        titresTable.setRowFactory(tv -> {
            TableRow<TitreDisplay> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    TitreDisplay rowData = row.getItem();
                    showTitreDetails(rowData);
                }
            });
            return row;
        });
    }

    @FXML
    private void refreshList() {
        List<TitreTransport> titres = Donnees.getTitres();
        allTitres = FXCollections.observableArrayList();

        // Sort titles in descending order by purchase date (most recent first)
        titres.sort(Comparator.comparing(TitreTransport::getDateAchat).reversed());

        // Use DateTimeFormatter instead of SimpleDateFormat
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (TitreTransport titre : titres) {
            String type = titre instanceof Ticket ? "Ticket" : "Carte Navigation";

            // Format LocalDateTime correctly
            String dateStr = titre.getDateAchat().format(dateFormatter);

            String prixStr = String.format("%.2f DA", titre.getPrix());

            // Updated to get personne from both Ticket and CarteNavigation
            String usagerStr = "Inconnu";
            Personne personne = null;

            if (titre instanceof Ticket) {
                personne = ((Ticket) titre).getPersonne();
            } else if (titre instanceof CarteNavigation) {
                personne = ((CarteNavigation) titre).getPersonne();
            }

            if (personne != null) {
                usagerStr = personne.getPrenom() + " " + personne.getNom();
            }

            String details = "";
            if (titre instanceof Ticket) {
                Ticket ticket = (Ticket) titre;
                details = "Usage unique";
            } else if (titre instanceof CarteNavigation) {
                CarteNavigation carte = (CarteNavigation) titre;
                details = "Type: " + (carte.getTypeCarte() != null ? carte.getTypeCarte() : "Standard");
            }

            allTitres.add(new TitreDisplay(titre.getId(), type, dateStr, prixStr, usagerStr, details));
        }

        filteredTitres = new FilteredList<>(allTitres, p -> true);
        titresTable.setItems(filteredTitres);

        updateTotalLabel();
    }

    private void filtrerListe() {
        String filter = filtreTypeComboBox.getValue();

        filteredTitres.setPredicate(titre -> {
            if ("Tous".equals(filter)) {
                return true;
            } else {
                return titre.getType().equals(filter);
            }
        });

        updateTotalLabel();
    }

    private void updateTotalLabel() {
        int count = filteredTitres.size();
        totalLabel.setText("Total: " + count + " titre" + (count > 1 ? "s" : ""));
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

    @FXML
    private void navigateToValidation(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/transport/ui/validationTitre.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("ESIRun - Validation de Titre");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTitreDetails(TitreDisplay titre) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du Titre");
        alert.setHeaderText(titre.getType() + " - ID: " + titre.getId());

        String content = "Date d'achat: " + titre.getDateAchat() + "\n"
                + "Prix: " + titre.getPrix() + "\n"
                + "Usager: " + titre.getUsager() + "\n"
                + "Détails: " + titre.getDetails();

        alert.setContentText(content);

        // Apply custom styling
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/transport/ui/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("info-dialog");

        alert.showAndWait();
    }

    // Helper class to represent a row in the table
    public static class TitreDisplay {

        private final int id;
        private final String type;
        private final String dateAchat;
        private final String prix;
        private final String usager;
        private final String details;

        public TitreDisplay(int id, String type, String dateAchat, String prix, String usager, String details) {
            this.id = id;
            this.type = type;
            this.dateAchat = dateAchat;
            this.prix = prix;
            this.usager = usager;
            this.details = details;
        }

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getDateAchat() {
            return dateAchat;
        }

        public String getPrix() {
            return prix;
        }

        public String getUsager() {
            return usager;
        }

        public String getDetails() {
            return details;
        }
    }
}
