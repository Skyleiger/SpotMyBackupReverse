package de.dwienzek.spotmybackupreverse;

import de.dwienzek.spotmybackupreverse.api.SpotMyBackupReverseAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class SpotMyBackupReverseApplication extends Application {

    private static final Logger LOGGER = LogManager.getLogger(SpotMyBackupReverseApplication.class);

    public static void main(String[] arguments) {
        if (arguments.length == 0) {
            runUIMode();
        } else {
            runCommandLineMode(arguments);
        }
    }

    private static void runCommandLineMode(String[] arguments) {
        LOGGER.info("Running SpotMyBackupReverse in command-line mode.");

        Path path = Path.of(arguments[0]);

        try {
            new SpotMyBackupReverseAPI().reverseSpotMyBackupFile(path, path);
        } catch (IOException exception) {
            LOGGER.error("Failed to reverse playlists.", exception);
        }
    }

    private static void runUIMode() {
        LOGGER.info("Running SpotMyBackupReverse in ui mode.");
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML("main"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("SpotMyBackupReverse");
        stage.show();
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SpotMyBackupReverseApplication.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
