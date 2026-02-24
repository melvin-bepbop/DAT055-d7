import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

// Main class
public class GUI {
    private JPanel chatHistoryPanel;
    private JScrollPane scrollPane;
    private JPanel channelListPanel;
    private LinkedList<JButton> ChannelButtons = new LinkedList<>();

    // Main driver method
    public GUI(String[] channels) {
        JFrame frame = new JFrame("This cord");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        chatHistoryPanel = new JPanel();
        chatHistoryPanel.setLayout(new BoxLayout(chatHistoryPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(chatHistoryPanel);

        channelListPanel = new JPanel();
        channelListPanel.setLayout(new BoxLayout(channelListPanel, BoxLayout.Y_AXIS));
        for (String name : channels) {
            JButton chanBtn = new JButton(name);
            chanBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
            channelListPanel.add(chanBtn);
            channelListPanel.add(Box.createVerticalStrut(5)); 
            this.ChannelButtons.add(chanBtn);
        }

        JScrollPane channelScroll = new JScrollPane(channelListPanel);
        channelScroll.setPreferredSize(new Dimension(150, 0));
        channelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        channelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // --- UPDATED LAYOUT LOGIC ---
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        
        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        JButton imageButton = new JButton("Image");

        // Create a container for BOTH buttons to sit on the East side
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
        
        // Add components to the button container with a gap between them
        buttonContainer.add(Box.createHorizontalStrut(10)); // Gap before the buttons
        buttonContainer.add(imageButton);
        buttonContainer.add(Box.createHorizontalStrut(5));  // Gap between Image and Send
        buttonContainer.add(sendButton);

        // Add the field to center and the button group to the east
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonContainer, BorderLayout.EAST);
        
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(channelScroll, BorderLayout.WEST); 
        frame.add(rightPanel, BorderLayout.CENTER); 

        // --- END UPDATED LAYOUT LOGIC ---

        frame.setVisible(true);
    }

    public void addMessage(String user, String text, String time, boolean isMe) {
        JPanel bubble = new JPanel();
        bubble.setLayout(new BorderLayout());
        bubble.setOpaque(false);
        bubble.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(isMe ? new Color(173, 216, 230) : Color.LIGHT_GRAY);
        content.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JTextArea textLabel = new JTextArea(text);
        textLabel.setLineWrap(true);
        textLabel.setWrapStyleWord(true);
        textLabel.setEditable(false);
        textLabel.setOpaque(false);
        textLabel.setColumns(20);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        textLabel.setSize(textLabel.getPreferredSize());

        JLabel userLabel = new JLabel(user + " (" + time + ")");

        content.add(userLabel);
        content.add(textLabel);

        if (isMe) {
            bubble.add(content, BorderLayout.EAST);
        } else {
            bubble.add(content, BorderLayout.WEST);
        }

        bubble.setMaximumSize(new Dimension(Integer.MAX_VALUE, bubble.getPreferredSize().height));
        content.setMaximumSize(content.getPreferredSize());

        chatHistoryPanel.add(bubble);
        chatHistoryPanel.add(Box.createVerticalStrut(10));

        chatHistoryPanel.revalidate();
        chatHistoryPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    public void clearChat() {
        chatHistoryPanel.removeAll();
        chatHistoryPanel.revalidate();
        chatHistoryPanel.repaint();
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] channels = {"General", "Random", "Dev"};
            GUI myChat = new GUI(channels);
            myChat.addMessage("User1", "Hello world!", "4:45 PM", false);
            myChat.addMessage("Me", "Hey there!", "4:46 PM", true);
        });
    }
}