package Utils;
import javax.swing.ImageIcon;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Utility methods for converting images to and from Base64 strings.
 *
 * This is used for sending images as text payloads over the network protocol.
 */
public class ImageUtils {

    /**
     * Encodes a file into a Base64 string.
     *
     * @param imageFile image file to encode
     * @return Base64 representation of the file, or null if encoding fails
     */
    public static String encodeFileToBase64(File imageFile) {
        try {
        
            byte[] fileContent = Files.readAllBytes(imageFile.toPath());
            
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            System.out.println("Error encoding image: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decodes a Base64 string into an ImageIcon.
     *
     * @param base64String Base64 string to decode
     * @return decoded ImageIcon, or null if decoding fails
     */
    public static ImageIcon decodeBase64ToImage(String base64String) {
        try {
            
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            
            return new ImageIcon(imageBytes);
        } catch (Exception e) {
            System.out.println("Error decoding image: " + e.getMessage());
            return null;
        }
    }
}