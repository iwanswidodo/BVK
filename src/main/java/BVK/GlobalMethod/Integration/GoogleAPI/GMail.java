package BVK.GlobalMethod.Integration.GoogleAPI;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.model.Thread;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static BVK.GlobalMethod.Encryption.Encryption.decode64;

/* class to demonstrate use of Gmail list labels API */
public class GMail extends GoogleAPI {

    private static com.google.api.services.gmail.Gmail initGmailService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        com.google.api.services.gmail.Gmail service = new com.google.api.services.gmail.Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }


    /**
     * Read message body of choosen email
     *
     * @param messageNumber the number of email, starting from 0.
     * @return Message body in string
     */
    public static String getGmailMessage(int messageNumber) throws GeneralSecurityException, IOException {

        Gmail service = initGmailService();

        // Get the thread ID
        String user = "me";
        ListThreadsResponse listResponse = service.users().threads().list(user).execute();
        List<Thread> threads = listResponse.getThreads();


        // Get message from thread ID
        Message message = service.users().messages().get(user, threads.get(messageNumber).getId()).execute();
        String messageBody = "";
        if (message.getPayload().getParts() != null) {
            for (MessagePart part : message.getPayload().getParts()) {
                if (part.getMimeType().equals("text/plain")) {
                    messageBody = part.getBody().getData();
                    break;
                }
            }
            return decode64(messageBody);
        } else {
            return "No message found";
        }
    }
}
