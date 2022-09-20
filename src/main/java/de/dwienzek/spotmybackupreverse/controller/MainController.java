package de.dwienzek.spotmybackupreverse.controller;

import de.dwienzek.spotmybackupreverse.api.SpotMyBackupReverseAPI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainController {

    private static final String GITHUB_URL = "https://github.com/Skyleiger/SpotMyBackupReverse";
    private static final Logger LOGGER = LogManager.getLogger(MainController.class);

    @FXML
    private TextField inputFileTextField;
    @FXML
    private TextField outputFileTextField;


    @FXML
    private void onInputFileSelectButtonClicked(MouseEvent event) {
        LOGGER.trace("InputFileSelectButton clicked.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select input file");
        fileChooser.setInitialFileName("spotMyBackup.json");

        File chosenFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (chosenFile != null) { // chosenFile can be null, when the dialog was cancelled by the user
            inputFileTextField.setText(chosenFile.getAbsolutePath());
        }
    }

    @FXML
    private void onOutputFileSelectButtonClicked(MouseEvent event) {
        LOGGER.trace("OutputFileSelectButton clicked.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select output file");
        fileChooser.setInitialFileName("spotMyBackupReverse.json");

        File chosenFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (chosenFile != null) { // chosenFile can be null, when the dialog was cancelled by the user
            outputFileTextField.setText(chosenFile.getAbsolutePath());
        }
    }

    @FXML
    private void onReverseSpotMyBackupFileButtonClicked() {
        LOGGER.trace("ReverseSpotMyBackupFileButton clicked.");

        String inputPathString = inputFileTextField.getText();
        String outputPathString = outputFileTextField.getText();

        if (validateFields(inputPathString, outputPathString)) {
            return;
        }

        Path inputPath = Path.of(inputPathString);
        if (validateInputPath(inputPath)) {
            return;
        }

        Path outputPath = Path.of(outputPathString);

        SpotMyBackupReverseAPI spotMyBackupReverseAPI = new SpotMyBackupReverseAPI();
        try {
            spotMyBackupReverseAPI.reverseSpotMyBackupFile(inputPath, outputPath);
            showInfoMessage("The SpotMyBackup file was successfully reversed. The new file is stored at " + outputPath.toAbsolutePath().toString());
        } catch (IOException exception) {
            LOGGER.error("Failed to reverse SpotMyBackupFile.", exception);
            showErrorMessage("The SpotMyBackupFile could not be reversed: " + exception.getLocalizedMessage() + ". See logs for more information.");
        }
    }

    private boolean validateFields(String inputPathString, String outputPathString) {
        if (isEmpty(inputPathString)) {
            showErrorMessage("Please enter an input file.");
            return true;
        }

        if (isEmpty(outputPathString)) {
            showErrorMessage("Please enter an output file.");
            return true;
        }

        return false;
    }

    private boolean validateInputPath(Path inputPath) {
        if (!Files.exists(inputPath)) {
            showErrorMessage("The entered input file does not exists.");
            return true;
        }

        return false;
    }

    private void showInfoMessage(String message) {
        LOGGER.debug("Info message shown: {}", message);
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).show();
    }

    private void showErrorMessage(String message) {
        LOGGER.debug("Error message shown: {}", message);
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).show();
    }

    private boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }

    @FXML
    private void onGitHubHyperlinkClicked() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI(GITHUB_URL));
    }

}
