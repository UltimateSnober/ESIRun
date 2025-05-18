
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the accueil (home) screen
        Parent root = FXMLLoader.load(getClass().getResource("transport/ui/accueil.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ESIRun - Accueil");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
