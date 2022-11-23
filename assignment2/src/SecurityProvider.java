import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SecurityProvider
{

    /*
        This class is created to provide encryption and decryption algorithms.
        CBC mode is implemented by using the ECB scheme from the last homework.
        IV and nonce values are hard coded as shown in the constructor.

     */
    private final Cipher encryptionAlgorithm;
    private final Cipher decryptionAlgorithm;
    private final SecretKey key;
    private final byte[] IV;

    public SecurityProvider() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        // Hard coded IV and Key values.
        this.key = generateKey("5yB+/=K%");
        this.IV = generateIVorNonce("Kx+/*21y");
        this.encryptionAlgorithm = Cipher.getInstance("DES/ECB/NoPadding");;
        this.encryptionAlgorithm.init(Cipher.ENCRYPT_MODE, key);
        this.decryptionAlgorithm = Cipher.getInstance("DES/ECB/NoPadding");
        this.decryptionAlgorithm.init(Cipher.DECRYPT_MODE, key);

    }
    public byte[] padPlainText(byte[] plainText)
    {
        // Padding algorithm.
        byte[] byteArray = plainText;
        int remainder = byteArray.length % 8;
        byte[] padded = new byte[byteArray.length + (8 - remainder)];
        Arrays.fill(padded, (byte) (8 - remainder));
        int index = 0;
        for (byte b : byteArray) {
            padded[index++] = b;
        }
        return padded;
    }
    // This method clears padding bytes after decryption.
    public byte[] clearPadding(byte[] padded){
        byte lastByte = padded[padded.length - 1];
        int plainTextLength = padded.length - lastByte;
        byte[] withoutPadding = new byte[plainTextLength];
        for (int i = 0; i < plainTextLength; i++) {
            withoutPadding[i] = padded[i];
        }
        return withoutPadding;
    }

    public byte[] CBCEncryption(byte plainText[]) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        plainText = padPlainText(plainText);
        byte[] lastCipherText = this.IV;
        int blockCount = plainText.length / 8;
        byte[] encryptedMessage = new byte[plainText.length];
        int outputIndex = 0;
        int currentBlock = 1;
        // Each block is encrypted separately.
        while (currentBlock <= blockCount) {
            byte[] currentPlainText = new byte[8];
            int index = 0;
            //Fetching the bytes in current block.
            for (int i = (currentBlock - 1) * 8; i < currentBlock * 8; i++) {
                currentPlainText[index] = plainText[i];
                index++;
            }
            //Calculating the cipherInput.
            byte[] cipherInput = XOR(lastCipherText, currentPlainText);
            byte[] encrypted = encryptionAlgorithm.doFinal(cipherInput);
            // Calculating the cipher output.
            for (byte b : encrypted) {
                encryptedMessage[outputIndex++] = b;
            }
            lastCipherText = encrypted;
            // Moving to the next block.
            currentBlock++;
        }
        return encryptedMessage;

    }

    public byte[] CBCDecryption(byte cipherText[]) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] decryptedMessage = new byte[cipherText.length];
        int outputIndex = 0;
        byte[] previousCipherText = this.IV;
        int blockCount = cipherText.length / 8;
        int currentBlock = 1;
        // Each block is decrypted separately.
        while (currentBlock <= blockCount) {
            byte[] currentCipherText = new byte[8];
            int index = 0;
            //Fetching the bytes in current block.
            for (int i = (currentBlock - 1) * 8; i < currentBlock * 8; i++) {
                currentCipherText[index] = cipherText[i];
                index++;
            }
            byte[] decrypted = XOR(decryptionAlgorithm.doFinal(currentCipherText), previousCipherText);;
            previousCipherText = currentCipherText;
            for (byte b : decrypted) {
                decryptedMessage[outputIndex++] = b;
            }
            // Moving to the next block.
            currentBlock++;
        }
        // Clearing the padding before returning.
        return clearPadding(decryptedMessage);
    }

    // XOR operation
    public static byte[] XOR(byte[] input1, byte[] input2) {
        byte[] resultArray = new byte[8];
        for (int i = 0; i < 8; i++) {
            resultArray[i] = (byte) (input1[i] ^ input2[i]);
        }
        return resultArray;

    }
    public static byte[] createByteArray(int hashCode) {
        // Creating 2 arrays of size 4 and combining them by using integer value(hash) taken as parameter.
        byte[] firstArray = ByteBuffer.allocate(4).putInt(hashCode).array();
        byte[] secondArray = ByteBuffer.allocate(4).putInt(hashCode * 2).array();
        byte[] finalArray = new byte[8];
        int index = 0;
        // Combining operation
        for (int i = 0; i < 4; i++) {
            finalArray[index++] = firstArray[i];
        }
        for (int j = 0; j < 4; j++) {
            finalArray[index++] = secondArray[j];
        }
        return finalArray;
    }

    // This method generates SecretKey object by using the hashcode of the input string.
    public SecretKey generateKey(String input){
        int hashCode = input.hashCode();
        byte[] keyBytes = createByteArray(hashCode);
        SecretKey key = new SecretKeySpec(keyBytes, "DES");
        return key;
    }

    // This method generates a byte array of size 8 by using the hash value of the input string .
    public static byte[] generateIVorNonce(String input) {
        int hashCode = input.hashCode();
        byte[] bytes = createByteArray(hashCode);
        return bytes;
    }



}
