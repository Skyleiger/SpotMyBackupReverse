package de.dwienzek.spotmybackupreverse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class SpotMyBackupReverseApplication {

    private static final Logger LOGGER = LogManager.getLogger(SpotMyBackupReverseApplication.class);

    public static void main(String[] arguments) {
        if (arguments.length == 0) {
            LOGGER.error("Please enter file argument. Example: 'java -jar SpotMyBackupReverse.jar spotifyBackup.json'");
            System.exit(1);
            return;
        }

        Path path = Path.of(arguments[0]);

        try {
            new SpotMyBackupReverseAPI().reverseSpotMyBackupFile(path, path);
        } catch (IOException exception) {
            LOGGER.error("Failed to reverse playlists.", exception);
        }
    }

}
