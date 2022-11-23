import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashingAlgorithm
{
    public static byte[] useSHA256(byte[] input)
    {
        // Usage of prebuilt SHA-256 algorithm to generate password hash.
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHexRepresentation(byte[] input)
    {
        // This method generates the hexadecimal representation of a byte array.

        String hex = "";
        // Iterating through each byte in the array
        for (byte i : input)
        {
            hex += String.format("%02X", i);
        }
        return hex;
    }

}
