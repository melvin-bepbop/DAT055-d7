import javax.swing.ImageIcon;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public class ImageUtils {

    // PHASE 1: Bob turns his picture into a String to send to the database
    public static String encodeFileToBase64(File imageFile) {
        try {
            // Read the physical file into raw bytes
            byte[] fileContent = Files.readAllBytes(imageFile.toPath());
            // Convert those bytes into a giant text string
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            System.out.println("Error encoding image: " + e.getMessage());
            return null;
        }
    }

    // PHASE 2: Jerry turns the String from the database back into a picture
    public static ImageIcon decodeBase64ToImage(String base64String) {
        try {
            // Convert the giant text string back into raw bytes
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            // Turn the bytes into an ImageIcon that your GUI can draw
            return new ImageIcon(imageBytes);
        } catch (Exception e) {
            System.out.println("Error decoding image: " + e.getMessage());
            return null;
        }
    }
}