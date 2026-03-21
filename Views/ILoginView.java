package Views;

public interface ILoginView {
    /**
     * Interface for the Controller to listen to login/signup actions.
     */
    interface ViewListener {
        void onLoginRequested(String username, String password);
        void onSignupRequested(String username, String password);
    }

    void setViewListener(ViewListener listener);

    void showError(String message);
    void showMessage(String message);
    void hide();
    void show();
}