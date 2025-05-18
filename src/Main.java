
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import transport.core.GestionFichiers;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // IMPORTANT: Charger les données au démarrage
        boolean loaded = GestionFichiers.chargerDonnees();
        if (loaded) {
            System.out.println("Données chargées avec succès");
        } else {
            System.out.println("Démarrage avec de nouvelles données");
        }

        // Interface utilisateur
        Parent root = FXMLLoader.load(getClass().getResource("/transport/ui/accueil.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ESIRun - Système de Transport");
        primaryStage.show();

        // IMPORTANT: Sauvegarder les données à la fermeture
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Sauvegarde des données avant fermeture...");
            boolean saved = GestionFichiers.sauvegarderDonnees();
            if (saved) {
                System.out.println("Données sauvegardées avec succès");
            } else {
                System.err.println("ERREUR: Échec de sauvegarde des données!");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
