package Views;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Simple login view for the client.
 *
 * Displays username and password fields and buttons for login and account creation.
 * Exposes helper methods for reading input and showing dialogs.
 */
public class LoginView {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;

    /**
     * Creates a new login view.
     */
    public LoginView() {
        frame = new JFrame("Login - This cord");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Account");
        panel.add(loginButton);
        panel.add(createAccountButton);

        frame.add(panel);
    }

    /**
     * Shows the window.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Hides and disposes the window.
     */
    public void hide() {
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * Returns the entered username.
     *
     * @return username as text
     */
    public String getUsername() { return usernameField.getText(); }

    /**
     * Returns the entered password.
     *
     * @return password as text
     */
    public String getPassword() { return new String(passwordField.getPassword()); }

    /**
     * Returns the login button.
     *
     * @return login button component
     */
    public JButton getLoginButton() { return loginButton; }

    /**
     * Returns the create-account button.
     *
     * @return create-account button component
     */
    public JButton getCreateAccountButton() { return createAccountButton; }

    /**
     * Shows an error message in a dialog.
     *
     * @param message message text to show
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows an informational message in a dialog.
     *
     * @param message message text to show
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Adds an ActionListener to the login button.
     *
     * @param listener listener to attach
     */
    public void addLoginListener(ActionListener listener) {
    loginButton.addActionListener(listener);
}

/**
 * Adds an ActionListener to the create-account button.
 *
 * @param listener listener to attach
 */
public void addCreateAccountListener(ActionListener listener) {
    createAccountButton.addActionListener(listener);
}
}