package de.dwienzek.spotmybackupreverse.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The api class of the SpotMyBackupReverse tool.
 * It can be used to use the playlist reverse feature programmatically.
 */
public class SpotMyBackupReverseAPI {

    private final Logger logger;
    private final Level logLevel;
    private final ObjectMapper objectMapper;

    /**
     * The default constructor.
     * It uses a default logger (which has the name of the class),
     * the default log level INFO and a new created object mapper.
     */
    public SpotMyBackupReverseAPI() {
        this.logger = LogManager.getLogger(SpotMyBackupReverseAPI.class);
        this.logLevel = Level.INFO;
        this.objectMapper = new ObjectMapper();
        configureObjectMapper();
    }

    /**
     * The constructor which can be used to customize the api.
     *
     * @param logger       the logger which will be used to log messages
     * @param logLevel     the log level which will be used for log messages
     * @param objectMapper an instance of an jackson object mapper which will be used to convert json from/to string or from/to a file.
     */
    public SpotMyBackupReverseAPI(Logger logger, Level logLevel, ObjectMapper objectMapper) {
        this.logger = logger;
        this.logLevel = logLevel;
        this.objectMapper = objectMapper;
    }

    private void configureObjectMapper() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        objectMapper.setDefaultPrettyPrinter(prettyPrinter);
    }

    /**
     * Reverses the playlists of an SpotMyBackup created file.
     *
     * @param inputPath  the path of the file, which was created from the SpotMyBackup tool
     * @param outputPath the path of the new file, which will contain the reordered playlists
     * @throws IOException when an error while file read/write or the parsing of the content occurs
     */
    public void reverseSpotMyBackupFile(Path inputPath, Path outputPath) throws IOException {
        writeObjectNodeToPath(outputPath, reverseSpotMyBackupFile(readObjectNodeFromPath(inputPath)));
    }

    private ObjectNode readObjectNodeFromPath(Path path) throws IOException {
        logger.log(logLevel, "Reading file '{}'.", path);

        try (InputStream inputStream = Files.newInputStream(path)) {
            ObjectNode objectNode = objectMapper.readValue(inputStream, ObjectNode.class);
            logger.log(logLevel, "File read complete.");
            return objectNode;
        }
    }

    private void writeObjectNodeToPath(Path path, ObjectNode objectNode) throws IOException {
        logger.log(logLevel, "Writing changes to file '{}'.", path);

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, objectNode);
            logger.log(logLevel, "Changes to file written.");
        }
    }

    /**
     * Reverses the playlists of an SpotMyBackup file content.
     *
     * @param input the content of an SpotMyBackup created file as a string
     * @return the new content of the SpotMyBackup file with the reordered playlists
     * @throws JsonProcessingException when an error while content parsing occurs
     */
    public String reverseSpotMyBackupFile(String input) throws JsonProcessingException {
        ObjectNode objectNode = readObjectNodeFromString(input);
        return writeObjectNodeToString(reverseSpotMyBackupFile(objectNode));
    }

    private ObjectNode readObjectNodeFromString(String input) throws JsonProcessingException {
        logger.log(logLevel, "Parsing input text.");
        ObjectNode objectNode = objectMapper.readValue(input, ObjectNode.class);

        logger.log(logLevel, "Input text parsed.");
        return objectNode;
    }

    private String writeObjectNodeToString(ObjectNode objectNode) throws JsonProcessingException {
        logger.log(logLevel, "Writing changes to text.");
        String text = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);

        logger.log(logLevel, "Changes to text written.");
        return text;
    }

    private ObjectNode reverseSpotMyBackupFile(ObjectNode objectNode) {
        logger.log(logLevel, "Reordering playlist tracks...");

        ObjectNode playlistsNode = (ObjectNode) objectNode.get("playlists");
        objectNode.replace("playlists", reorderPlaylists(playlistsNode));

        logger.log(logLevel, "Reorder complete.");

        return objectNode;
    }

    private ObjectNode reorderPlaylists(ObjectNode playlistsNode) {
        List<Map.Entry<String, JsonNode>> fields = new ArrayList<>(playlistsNode.size());
        playlistsNode.fields().forEachRemaining(entry -> fields.add(0, entry));

        List<Map.Entry<String, JsonNode>> newFields = new ArrayList<>(playlistsNode.size());

        for (Map.Entry<String, JsonNode> entry : fields) {
            newFields.add(
                    new AbstractMap.SimpleEntry<>(
                            entry.getKey(),
                            reorderTracks(entry.getKey(), (ObjectNode) entry.getValue())
                    )
            );
            playlistsNode.remove(entry.getKey());
        }

        for (Map.Entry<String, JsonNode> entry : newFields) {
            playlistsNode.set(entry.getKey(), entry.getValue());
        }

        return playlistsNode;
    }

    private ObjectNode reorderTracks(String name, ObjectNode playlistNode) {
        logger.log(logLevel, "Reordering tracks of playlist '{}'.", name);

        ArrayNode oldTracks = (ArrayNode) playlistNode.get("tracks");

        List<JsonNode> reorderedTracks = new ArrayList<>(oldTracks.size());
        oldTracks.forEach(element -> reorderedTracks.add(0, element));

        ArrayNode newTracks = objectMapper.createArrayNode();
        reorderedTracks.forEach(newTracks::add);

        playlistNode.replace("tracks", newTracks);

        logger.log(logLevel, "Reordered tracks of playlist '{}'.", name);

        return playlistNode;
    }

}
