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

    public static byte[] HMAC_SHA256(byte[] message,byte[] secretKey){
        byte[] hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return hmacSha256;
    }

    public static byte[] hash(byte[] salt,String password)
    {
        byte[] passwordArray = password.getBytes(StandardCharsets.UTF_8);
        byte[] saltedPassword = new byte[16+passwordArray.length];
        int index = 0;
        for(byte s : salt){
            saltedPassword[index++] = s;
        }
        for(byte b : passwordArray){
            saltedPassword[index++] = b;
        }
        return useSHA256(saltedPassword);
    }

    public static String getHexRepresentation(byte[] input)
    {

        String hex = "";

        // Iterating through each byte in the array
        for (byte i : input) {
            hex += String.format("%02X", i);
        }

        return hex;
    }
    public static String getStringRepresentation(byte[] hexRepresentation){
        String hexString = new String(hexRepresentation);
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexString.length(); i+=2) {
            String str = hexString.substring(i, i+2);
            output.append((char)Integer.parseUnsignedInt(str,16));
        }
        return output.toString().trim();
    }


    public static byte[] generateSalt()
    {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
