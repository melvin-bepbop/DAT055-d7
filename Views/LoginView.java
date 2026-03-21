package Views;
import javax.swing.*;
import java.awt.*;

/**
 * Simple login view for the client.
 *
 * Displays username and password fields and buttons for login and account creation.
 * Exposes helper methods for reading input and showing dialogs.
 */
public class LoginView implements ILoginView{
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private ViewListener listener;
    
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
        setupActionListeners();
    }
@Override
    public void setViewListener(ViewListener listener) {
        this.listener = listener;
    }

    private void setupActionListeners() {
        loginButton.addActionListener(e -> {
            if (listener != null) {
                
                listener.onLoginRequested(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });

        createAccountButton.addActionListener(e -> {
            if (listener != null) {
                
                listener.onSignupRequested(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });
    }

    @Override
    public void show() {
        frame.setVisible(true);
    }

    @Override
    public void hide() {
        frame.setVisible(false);
        frame.dispose();
    }
    /**
     * Shows an error message in a dialog.
     *
     * @param message message text to show
     */
    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
        /**
     * Shows an informational message in a dialog.
     *
     * @param message message text to show
     */
    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }




}