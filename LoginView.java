import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;

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

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
        frame.dispose();
    }

    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public JButton getLoginButton() { return loginButton; }
    public JButton getCreateAccountButton() { return createAccountButton; }

    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    public void addLoginListener(ActionListener listener) {
    loginButton.addActionListener(listener);
}

public void addCreateAccountListener(ActionListener listener) {
    createAccountButton.addActionListener(listener);
}
}