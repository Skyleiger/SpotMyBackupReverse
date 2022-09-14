package de.dwienzek.spotmybackupreverse;

import com.google.gson.*;
import lombok.Data;
import lombok.NonNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class SpotMyBackupReverse {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter file argument. Example: 'java -jar SpotMyBackupReverse.jar spotifybackup.json'");
            System.exit(0);
        }

        new SpotMyBackupReverse(new File(args[0]));
    }

    public SpotMyBackupReverse(@NonNull File file) {
        if (!file.exists()) {
            System.out.println("The given file does not exists.");
            System.exit(0);
        }

        if (!file.isFile()) {
            System.out.println("The given file is not a file.");
            System.exit(0);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            System.out.println("Reading file...");
            FileReader reader = new FileReader(file);
            JsonObject jsonFile = gson.fromJson(reader, JsonObject.class);
            reader.close();
            System.out.println("File read complete.");

            System.out.println("Reordering playlist tracks...");
            JsonObject oldPlaylists = jsonFile.getAsJsonObject("playlists");
            JsonObject newPlaylists = new JsonObject();

            List<Map.Entry<String, JsonElement>> entries = new ArrayList<>(oldPlaylists.entrySet());
            Collections.reverse(entries);

            entries.forEach(entry -> {
                System.out.println("Reordering playlist '" + entry.getKey() + "'...");
                JsonObject currentPlaylist = entry.getValue().getAsJsonObject();
                JsonArray oldTracks = currentPlaylist.getAsJsonArray("tracks");
                List<JsonElement> reorderedTracks = new ArrayList<>(oldTracks.size());
                oldTracks.forEach(element -> reorderedTracks.add(0, element));

                JsonArray newTracks = new JsonArray(reorderedTracks.size());
                reorderedTracks.forEach(newTracks::add);
                currentPlaylist.add("tracks", newTracks);
                newPlaylists.add(entry.getKey(), currentPlaylist);
                System.out.println("Reordered playlist '" + entry.getKey() + "'.");
            });
            
            jsonFile.add("playlists", newPlaylists);
            System.out.println("Reorder complete.");

            System.out.println("Writing file...");
            FileWriter writer = new FileWriter(file);
            gson.toJson(jsonFile, writer);
            writer.close();
            System.out.println("File write complete.");
        } catch (IOException exception) {
            System.err.println("Failed to handle file:");
            exception.printStackTrace();
        }
    }
}
