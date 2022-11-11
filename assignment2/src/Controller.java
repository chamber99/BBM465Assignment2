import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller
{
    public ArrayList<User> allUsers;
    public HashMap<Message,User> allMessages;
    public HashMap<User,String> allSalts;
    public DataService service;

    public Controller() throws IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        allUsers = new ArrayList<User>();
        allMessages = new HashMap<Message,User>();
        service = new DataService();
        //allUsers = service.fetchAllUsers();
        allSalts = new HashMap<User,String>();
    }

    public void createNewUser(String username,String password) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] salt = HashingAlgorithm.generateSalt();
        byte[] hashedPassword = HashingAlgorithm.hash(salt,password);
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(HashingAlgorithm.getHexRepresentation(hashedPassword));
        this.allUsers.add(newUser);
        allSalts.put(newUser,new String(salt));

    }
    public void done() throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        this.service.storeUsers(allUsers);
        this.service.storeSalts(allSalts);
    }
    public void fetch() throws IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        this.allUsers = service.fetchAllUsers();
        for(User u : allUsers){
            System.out.println(u.getUsername()+" "+u.getPassword());
        }
        service.fetchSalt();
    }
    public String leaveMessage(String[] data) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        String message;
        if(!confirmPassword(data[1],data[2])){
            message= "The passwords you entered do not match\n";
        }
       /* else if(!confirmMessageID(data[3])){
            message= "Please enter a unique message ID.\n";
        }*/
        else
        {
            byte[] salt = HashingAlgorithm.generateSalt();
            byte[] hashedPassword = HashingAlgorithm.hash(salt,data[1]);
            Message newMessage = new Message();
            User receiver = new User();
            receiver.setUsername(data[0]);
            newMessage.setReceiver(receiver);
            newMessage.setPassword(HashingAlgorithm.getHexRepresentation(hashedPassword));
            newMessage.setMessage_id(data[3]);
            String messageContent = data[4];
            String HMAC = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.HMAC_SHA256(messageContent.getBytes(StandardCharsets.UTF_8),hashedPassword));
            messageContent+= "(&&&&&)"+HMAC;
            newMessage.setContent(messageContent);
            this.service.sendMessage(newMessage);
            message = "Your message has been sent!";
        }
        return message;

    }
    public void print() throws IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        this.service.readMessages();
    }
    public boolean confirmPassword(String password,String confirmPassword){
        String hex1 = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.useSHA256(password.getBytes(StandardCharsets.UTF_8)));
        String hex2 = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.useSHA256(confirmPassword.getBytes(StandardCharsets.UTF_8)));
        return hex1.equals(hex2);
    }
    public boolean confirmMessageID(String candidateID)
    {
        boolean control = true;
        for(Message message : this.allMessages.keySet()){
            if(message.getMessage_id().equals(candidateID)){
                control = false;
                break;
            }
        }
        return control;
    }
    public String[] getUsernames(){
        String[] usernames = new String[allUsers.size()];
        int index = 0;
        for(User u : allUsers){
            usernames[index++] = u.getUsername();
        }
        return usernames;
    }
}
