import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DataService
{
    public SecurityProvider securityProvider;

    public DataService() throws FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        securityProvider = new SecurityProvider();
    }

    public boolean storeMessages(ArrayList<Message> allMessages) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        // Converting contents of the messages to the string and calling the writer method.
        String messageData = "";
        for(int i=0;i<allMessages.size();i++){
            if(i == allMessages.size()-1){
                messageData+= allMessages.get(i).toString();
            }
            else{
                // #msgend# is used here instead of \n
                messageData+= allMessages.get(i).toString()+"#msgend#";
            }
        }
        // Converting data to the byte array.
        byte[] byteArray = messageData.getBytes(StandardCharsets.UTF_8);
        writeData(byteArray,2);
        return true;
    }

    public ArrayList<Message> fetchAllMessages() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Reading all messages from the message database.
        ArrayList<Message> allMessages = new ArrayList<>();
        FileInputStream inputMessage = new FileInputStream("message.data");
        int bytes = inputMessage.available();
        if(bytes > 2){
            byte[] inputArray = new byte[bytes];
            inputMessage.read(inputArray);
            // Decrypting data
            byte[] decryptedData = securityProvider.CBCDecryption(inputArray);
            String messages = new String(decryptedData);
            String[] split = messages.split("#msgend#");
            for(String line : split)
            {
                String[] messageComponents = line.split("\\s+");
                int length = messageComponents.length;
                String messageID = messageComponents[0];
                String content = "";
                // Creating the message content.
                for(int i=1;i<length-2;i++){
                    if(i == length-3){
                        content+= messageComponents[i];
                    }

                    else{
                        content+= messageComponents[i]+" ";
                    }
                }
                String password = messageComponents[length-2];
                String receiverUsername = messageComponents[length-1];
                // Creating the message object and adding it to the list.
                Message message = new Message();
                User receiver = new User();
                receiver.setUsername(receiverUsername);
                message.setMessage_id(messageID);
                message.setContent(content);
                message.setPassword(password);
                message.setReceiver(receiver);
                allMessages.add(message);
            }
        }

        return allMessages;
    }

    public void storeUsers(ArrayList<User> allUsers) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        // User data are converted to string and writer is called in this method.
        String userData = "";
        for(int i=0;i<allUsers.size();i++){
            if(i == allUsers.size()-1){
                userData+= allUsers.get(i).getUsername() + " " + allUsers.get(i).getPassword();
            }
            else {
                userData+= allUsers.get(i).getUsername() + " " + allUsers.get(i).getPassword() +"\n";
            }
        }
        // Calling the writer function to write data to the destination file.
        writeData(userData.getBytes(StandardCharsets.UTF_8),1);
    }
    public boolean writeData(byte[] data,int destination) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        // Encrypting all data before storing them.
        byte[] encryptedData = securityProvider.CBCEncryption(data);
        if(destination == 1)
        {
            // Writing user data to the user database.
            FileOutputStream fos = new FileOutputStream("user.data");
            fos.write(encryptedData);
        }
        else if(destination == 2)
        {
            // Writing message data to the message database.
            FileOutputStream fos = new FileOutputStream("message.data");
            fos.write(encryptedData);
        }
        return true;
    }
    public ArrayList<User> fetchAllUsers() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        // This methods fetches all users from the database and stores them in an arraylist.
        ArrayList<User> allUsers = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream("user.data");
        int length = inputStream.available();
        if(length != 0){
            byte[] inputArray = new byte[length];
            inputStream.read(inputArray);
            // Decrypting data.
            byte[] decryptedData = securityProvider.CBCDecryption(inputArray);
            String users = new String(decryptedData);
            String[] split  = users.split("\n");
            for(String s : split){
                // Creating user objects.
                String[] userData = s.split("\\s+");
                String username = userData[0];
                String hashedPassword = userData[1];
                User user = new User();
                user.setUsername(username);
                user.setPassword(hashedPassword);
                // Adding them to the list.
                allUsers.add(user);
            }
            inputStream.close();
            return allUsers;
        }
       return  allUsers;
    }
}
