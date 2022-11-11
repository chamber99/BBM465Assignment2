import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DataService
{
    public SecurityProvider securityProvider;
    FileInputStream inputMessage = new FileInputStream("message.data");

    public DataService() throws FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        securityProvider = new SecurityProvider();
    }

    public boolean sendMessage(Message newMessage) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        FileOutputStream messageWriter  = new FileOutputStream("message.data",true);
        String messageData = newMessage.getMessage_id() + " "+ newMessage.getContent() + " "+ newMessage.getPassword()+" "+newMessage.getReceiver().getUsername()+"\n";
        byte[] byteArray = messageData.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = securityProvider.CBCEncryption(byteArray);
        messageWriter.write(encrypted);
        messageWriter.close();
        return true;
    }
    public void readMessages() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] inputArray = new byte[inputMessage.available()];
        inputMessage.read(inputArray);
        byte[] decryptedData = securityProvider.CBCDecryption(inputArray);
        String users = new String(decryptedData);
        System.out.println(users);
    }
    public void fetchAllMessages() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] inputArray = new byte[inputMessage.available()];
        inputMessage.read(inputArray);
        byte[] decryptedData = securityProvider.CBCDecryption(inputArray);
        String allMessages = new String(decryptedData);
        String[] split = allMessages.split("\n");
        for(String line : split){
            String[] messageComponents = line.split("\\s+");
            int length = messageComponents.length;
            String messageID = messageComponents[0];

            String content = "";
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
            System.out.println(messageID);
            System.out.println(content);
            System.out.println(password);
            System.out.println(receiverUsername);
            System.out.println("----------------------------------");

        }


    }

    public void storeUsers(ArrayList<User> allUsers) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        String userData = "";
        for(int i=0;i<allUsers.size();i++){
            if(i == allUsers.size()-1){
                userData+= allUsers.get(i).getUsername() + " " + allUsers.get(i).getPassword();
            }
            else {
                userData+= allUsers.get(i).getUsername() + " " + allUsers.get(i).getPassword() +"\n";
            }
        }
        writeData(userData.getBytes(StandardCharsets.UTF_8),1);
    }
    public void storeSalts(HashMap<User,String> allSalts) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        String saltData = "";
        int length = allSalts.entrySet().size();
        int count = 0;
        for(Map.Entry<User,String> entry : allSalts.entrySet()){
            if (count == length-1){
                saltData+= entry.getKey().getUsername()+" "+entry.getValue();
            }
            else{
                saltData+= entry.getKey().getUsername()+" "+entry.getValue() + "\n";
            }
        }
        writeData(saltData.getBytes(StandardCharsets.UTF_8),2);
    }
    public boolean writeData(byte[] data,int destination) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] encryptedData = securityProvider.CBCEncryption(data);
        System.out.println(new String(encryptedData));
        if(destination == 1)
        {
            FileOutputStream fos = new FileOutputStream("user.data");
            fos.write(encryptedData);
            //fos.close();
        }
        else if(destination == 2)
        {
            FileOutputStream fos = new FileOutputStream("salt.data",true);
            fos.write(encryptedData);
            //fos.close();
        }
        return true;
    }
    public ArrayList<User> fetchAllUsers() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        ArrayList<User> allUsers = new ArrayList<User>();
        FileInputStream inputStream = new FileInputStream("user.data");
        if(inputStream.available() != 0){
            byte[] inputArray = new byte[inputStream.available()];
            inputStream.read(inputArray);
            byte[] decryptedData = securityProvider.CBCDecryption(inputArray);
            String users = new String(decryptedData);
            String[] split  = users.split("\n");
            for(String s : split){
                String[] userData = s.split("\\s+");
                String username = userData[0];
                String hashedPassword = userData[1];
                User user = new User();
                user.setUsername(username);
                user.setPassword(hashedPassword);
                allUsers.add(user);
            }
            inputStream.close();
            return allUsers;
        }
       return  allUsers;
    }
    public void fetchSalt() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream inputStream = new FileInputStream("salt.data");
        System.out.println(inputStream.available());
        byte[] inputArray = new byte[inputStream.available()];
        inputStream.read(inputArray);
        byte[] decryptedData = securityProvider.CBCDecryption(inputArray);
        String users = new String(decryptedData);
        System.out.println(users);

    }
}
