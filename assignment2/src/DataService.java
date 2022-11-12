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
    public String allSent = "";

    public DataService() throws FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        securityProvider = new SecurityProvider();
    }

    public boolean storeMessages(ArrayList<Message> allMessages) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        FileOutputStream messageWriter  = new FileOutputStream("message.data");
        String messageData = "";
        for(int i=0;i<allMessages.size();i++){
            if(i == allMessages.size()-1){
                messageData+= allMessages.get(i).toString();
            }
            else{
                messageData+= allMessages.get(i).toString()+"#msgend#";
            }
        }
        byte[] byteArray = messageData.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = securityProvider.CBCEncryption(byteArray);
        messageWriter.write(encrypted);
        messageWriter.close();
        return true;
    }
    public void readMessages() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream inputMessage = new FileInputStream("message.data");
        byte[] inputArray = new byte[inputMessage.available()];
        inputMessage.read(inputArray);
        byte[] decryptedData = securityProvider.CBCDecryption(inputArray);
        System.out.println(new String(decryptedData));

    }
    public ArrayList<Message> fetchAllMessages() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        ArrayList<Message> allMessages = new ArrayList<>();
        FileInputStream inputMessage = new FileInputStream("message.data");
        int bytes = inputMessage.available();
        if(bytes > 2){
            byte[] inputArray = new byte[inputMessage.available()];
            inputMessage.read(inputArray);
            byte[] decryptedData = securityProvider.CBCDecryption(inputArray);
            String messages = new String(decryptedData);
            String[] split = messages.split("#msgend#");
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
    public HashMap<String,String> fetchAllSalts() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        HashMap<String,String> allSalts = new HashMap<>();
        FileInputStream inputStream = new FileInputStream("salt.data");
        byte[] inputArray = new byte[inputStream.available()];
        inputStream.read(inputArray);
        byte[] decryptedData = securityProvider.CBCDecryption(inputArray);
        String salts = new String(decryptedData);
        String[] split = salts.split("\n");
        for(String s : split){
            String[] saltData = s.split("\\s+");
            String username = saltData[0];
            String salt = saltData[1];
            allSalts.put(username,salt);
        }
        return allSalts;
    }
}
