package BVK.GlobalMethod.Integration.GoogleAPI;

import BVK.GlobalMethod.Logger.Log;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GDrive extends GoogleAPI {

    private static Drive initGDriveService() {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT;
        Drive service = null;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return service;
    }

    public static String getGDriveFileList() throws GeneralSecurityException, IOException {

        Drive service = initGDriveService();
        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        String fileList = "";
        if (files == null || files.isEmpty()) {
            Log.warn("No files found.");
        } else {
            for (File file : files) {
                fileList = fileList + "\n" + file.getName() + " " + file.getId();
            }
        }
        return fileList;
    }


    public static String getGDriveFileId(String fileName) {

        Drive service = initGDriveService();
        // Print the names and IDs for up to 10 files.
        FileList result = null;
        List<File> files = null;
        Drive.Files.List requestList;
        try {
            requestList = service.files().list()
                    .setPageSize(1000)
                    .setFields("nextPageToken, files(id, name)");


            for (int i = 0; i < 5; i++) {
                result = requestList.execute();
                files = result.getFiles();
                requestList.setPageToken(result.getNextPageToken());
                String fileList = "";

                if (files == null || files.isEmpty()) {
                    Log.info("No files found.");

                } else {
                    for (File file : files) {
                        if (file.getName().equals(fileName)) {
                            return file.getId();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        System.out.println("file not found");
        return null;
    }

    /**
     * Create new folder.
     *
     * @return Inserted folder id if successful, {@code null} otherwise.
     * @throws IOException if service account credentials file not found.
     */
    public static String createFolder(String folderName) throws IOException, GeneralSecurityException {

        // Build a new authorized API client service.
        Drive service = initGDriveService();
        // File's metadata.
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = service.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            System.out.println("Folder Created! ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
    }

    public static void deleteFile(String fileName) {
        String fileId = getGDriveFileId(fileName);
        Drive service = initGDriveService();
        try {
            service.files().delete(fileId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }


    /**
     * Upload new file.
     *
     * @return Inserted file metadata if successful, {@code null} otherwise.
     * @throws IOException if service account credentials file not found.
     */
    public static String uploadGDrive(String fileLocation, String MIME, String parentId) throws IOException {
        // Build a new authorized API client service.
        Drive service = initGDriveService();
        // Upload file file on drive.
        File fileMetadata = new File();
        // File's content.
        java.io.File filePath = new java.io.File(System.getProperty("user.dir") + "/" + fileLocation);
        fileMetadata.setName(filePath.getName());
        if (parentId != null && parentId.length() > 0) {

            fileMetadata.setParents(
                    Arrays.asList(parentId));
        }
        // Specify media type and file-path for file.
        FileContent mediaContent = new FileContent(MIME, filePath);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    /**
     * Upload new file.
     *
     * @return Inserted file metadata if successful, {@code null} otherwise.
     * @throws IOException if service account credentials file not found.
     */
    public static String uploadGDrive(String fileLocation, String MIME) throws GoogleJsonResponseException {


        // Build a new authorized API client service.
        Drive service = initGDriveService();
        // Upload file file on drive.
        File fileMetadata = new File();
        // File's content.
        java.io.File filePath = new java.io.File(fileLocation);
        fileMetadata.setName(filePath.getName());
        // Specify media type and file-path for file.
        FileContent mediaContent = new FileContent(MIME, filePath);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Upload new file.
     *
     * @return Inserted file metadata if successful, {@code null} otherwise.
     * @throws IOException if service account credentials file not found.
     */
    public static ByteArrayOutputStream downloadGdrive(String fileName) {

        // Build a new authorized API client service.
        Drive service = initGDriveService();
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            service.files().get(getGDriveFileId(fileName))
                    .executeMediaAndDownloadTo(outputStream);
            return (ByteArrayOutputStream) outputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void downloadGdriveAndWriteFile(String fileName) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Build a new authorized API client service.
        Drive service = initGDriveService();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            service.files().get(getGDriveFileId(fileName))
                    .executeMediaAndDownloadTo(outputStream);
            outputStream.writeTo(fos);
            Log.infoGreen("file downloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
