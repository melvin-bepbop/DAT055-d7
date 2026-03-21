package Views;

import java.io.File;

public interface IChatView {
    /**
     * Interface for the Controller to listen to user actions from the View.
     */
    interface ViewListener {
        void onSendTextMessage(String rawText);
        void onSendImageMessage(File selectedFile);
    }

    /**
     * Attaches the listener (usually the Controller) to the View.
     * @param listener The controller that will handle the business logic.
     */
    void setViewListener(ViewListener listener);
}