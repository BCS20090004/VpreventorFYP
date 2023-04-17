package com.fypvpreventor.VpreventorFYP;

import java.io.IOException;
import java.nio.file.*;

public class FolderWatcher {

    public static void main(String[] args) throws IOException, InterruptedException {

        // Define the folder to watch
        Path folderPath = Paths.get("/path/to/folder");

        // Create a WatchService instance and register the folder to watch for changes
        WatchService watchService = FileSystems.getDefault().newWatchService();
        folderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        // Start the infinite loop to listen for changes
        while (true) {
            // Wait for a new WatchKey event
            WatchKey key = watchService.take();

            // Iterate over the events for the WatchKey
            for (WatchEvent<?> event : key.pollEvents()) {
                // Check if the event is a new file creation event
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    // Get the filename of the new file
                    Path newFilePath = ((WatchEvent<Path>)event).context();
                    System.out.println("New file created: " + newFilePath);
                    // Call your method to upload the new file to Google Drive
                }
            }

            // Reset the WatchKey to listen for more events
            key.reset();
        }
    }
}

