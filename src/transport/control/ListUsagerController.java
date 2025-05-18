package transport.control;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import transport.core.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListUsagerController {

    @FXML
    private TableView<Personne> tableView;

    @FXML
    private TableColumn<Personne, String> nomColumn;

    @FXML
    private TableColumn<Personne, String> prenomColumn;

    @FXML
    private TableColumn<Personne, LocalDate> dateNaissanceColumn;

    @FXML
    private TableColumn<Personne, Boolean> handicapeColumn;

    @FXML
    private TableColumn<Personne, String> typeColumn;

    @FXML
    private TableColumn<Personne, String> matriculeColumn;

    @FXML
    private TableColumn<Personne, TypeFonction> fonctionColumn;

    @FXML
    private TableColumn<Personne, Integer> idColumn; // Added ID column

    @FXML
    private Label totalLabel;

    @FXML
    private ComboBox<String> filtreTypeComboBox;

    @FXML
    public void initialize() {
        // Set up the table columns
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        handicapeColumn.setCellValueFactory(new PropertyValueFactory<>("handicape"));

        // Custom cell factory for date formatting
        dateNaissanceColumn.setCellFactory(column -> {
            return new TableCell<Personne, LocalDate>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        setText(formatter.format(date));
                    }
                }
            };
        });

        // Custom cell factory for handicapé display
        handicapeColumn.setCellFactory(column -> {
            return new TableCell<Personne, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item ? "Oui" : "Non");
                    }
                }
            };
        });

        // Type column (Employee or User) - FIXED
        typeColumn.setCellValueFactory(cellData -> {
            Personne personne = cellData.getValue();
            String type = personne instanceof Employe ? "Employé" : "Usager";
            return new SimpleStringProperty(type);
        });

        // Employe-specific columns - FIXED
        matriculeColumn.setCellValueFactory(cellData -> {
            Personne personne = cellData.getValue();
            if (personne instanceof Employe) {
                return new SimpleStringProperty(((Employe) personne).getMatricule());
            }
            return new SimpleStringProperty("");
        });

        fonctionColumn.setCellValueFactory(cellData -> {
            Personne personne = cellData.getValue();
            if (personne instanceof Employe) {
                return new SimpleObjectProperty<>(((Employe) personne).getFonction());
            }
            return new SimpleObjectProperty<>();
        });

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Setup for ID column

        // Initialize filter combobox
        filtreTypeComboBox.getItems().addAll("Tous", "Usagers", "Employés");
        filtreTypeComboBox.setValue("Tous");
        filtreTypeComboBox.setOnAction(event -> filtrerListe());

        // Enable modify/delete buttons when a row is selected
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            //modifierButton.setDisable(!hasSelection);
            //supprimerButton.setDisable(!hasSelection);

            if (hasSelection) {
                // Update detail view with selected user's information
                updateDetailView(newSelection);
            }
        });

        // Load all users
        loadUsagers();
    }

    private void loadUsagers() {
        // Get all users from the data
        List<Personne> usagers = Donnees.getUsagers();

        // Apply filter if needed
        String filtre = filtreTypeComboBox.getValue();
        if (filtre != null) {
            if (filtre.equals("Usagers")) {
                usagers = usagers.stream()
                        .filter(p -> !(p instanceof Employe))
                        .toList();
            } else if (filtre.equals("Employés")) {
                usagers = usagers.stream()
                        .filter(p -> p instanceof Employe)
                        .toList();
            }
        }

        // Update the table
        ObservableList<Personne> data = FXCollections.observableArrayList(usagers);
        tableView.setItems(data);

        // Update total count
        totalLabel.setText("Total: " + usagers.size() + " personnes");
    }

    @FXML
    private void filtrerListe() {
        loadUsagers();
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

    private void updateDetailView(Personne selectedPersonne) {
        // Implement the logic to update the detail view with the selected user's information
    }
}
