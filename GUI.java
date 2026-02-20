import javax.swing.*;
import java.awt.*;

// Main class
public class GUI {
 private JPanel chatHistoryPanel;
 private JScrollPane scrollPane;
 private JPanel channelListPanel;
    // Main driver method
    public GUI(String[] channels)
    {
     
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
            chanBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Makes button fill width
            channelListPanel.add(chanBtn);
            channelListPanel.add(Box.createVerticalStrut(5)); // Space between buttons
        }

        JScrollPane channelScroll = new JScrollPane(channelListPanel);
        channelScroll.setPreferredSize(new Dimension(150, 0));
        channelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        channelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Keep sidebar at 150px wide

            JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new BorderLayout());
                JTextField inputField = new JTextField();
                JButton sendButton = new JButton("Send");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.add(channelScroll, BorderLayout.WEST);
            // making the frame visible
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
            textLabel.setLineWrap(true);            // Wrap text to next line
            textLabel.setWrapStyleWord(true);       // Don't break words in half
            textLabel.setEditable(false);           // Make it read-only
            textLabel.setOpaque(false);             // Make background transparent (shows bubble color)
            textLabel.setColumns(20);               // Limits the width naturally
            textLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            textLabel.setSize(textLabel.getPreferredSize());

    JLabel userLabel = new JLabel(user + " (" + time + ")");

        // Alignment: Right for "Me", Left for "Them"
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
        chatHistoryPanel.add(Box.createVerticalStrut(10)); // Space between messages
        

        chatHistoryPanel.revalidate();
        chatHistoryPanel.repaint();
    SwingUtilities.invokeLater(() -> {
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {String[] channels = {"tjo", "hej", "hello"};
        GUI myChat = new GUI(channels);
        // Test a couple messages
        myChat.addMessage("Epstien", "One pizza please", "4:45 PM", false);
        myChat.addMessage("Diddy", "baby oil", "4:46 PM", true);
    });
    }

}