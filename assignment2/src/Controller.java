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
    private ArrayList<User> allUsers;
    private ArrayList<Message> allMessages;
    private HashMap<String,String> allSalts;
    private DataService service;
    private boolean isUserCreated;
    private boolean isMessageCreated;

    public Controller() throws IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        this.service = new DataService();
        this.allUsers = service.fetchAllUsers();
        this.allMessages = service.fetchAllMessages();
        this.allSalts = service.fetchAllSalts();
        this.isUserCreated = false;
        this.isMessageCreated = false;
    }

    public void openPage(int pageNumber)
    {
        if(pageNumber == 0){
            new MainPage(this);
        }
        else if(pageNumber == 1){
            new CreateUserPage(this);
        }
        else if(pageNumber == 2){
            new AccessMessagePage(this);
        }
        else if(pageNumber == 3){
            new RegisterFormPage(this);
        }
    }
    public void openPage(String index){
        new ViewMessagePage(this,allMessages.get(Integer.parseInt(index)));
    }
    public void createNewUser(String username,String password) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] salt = HashingAlgorithm.generateSalt();
        byte[] hashedPassword = HashingAlgorithm.hash(salt,password);
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(HashingAlgorithm.getHexRepresentation(hashedPassword));
        this.allUsers.add(newUser);
        this.allSalts.put(username,new String(salt));
        this.isUserCreated = true;

    }
    public void storeUserData() throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        if(isUserCreated){
            this.service.storeUsers(allUsers);
            this.service.storeSalts(allSalts);
            this.isUserCreated = false;
        }
    }
    public void storeMessageData() throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        if(isMessageCreated){
            this.service.storeMessages(allMessages);
            this.isMessageCreated = false;
        }

    }

    public void fetch() throws IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        this.allUsers = service.fetchAllUsers();
        for(User u : allUsers){
            System.out.println(u.getUsername()+" "+u.getPassword());
        }
        //service.fetchSalt();
    }
    public String leaveMessage(String[] data) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        String message;
        if(!confirmPassword(data[1],data[2])){
            message= "The passwords you entered do not match\n";
        }
       else if(!confirmMessageID(data[3])){
            message= "Please enter a unique message ID.\n";
        }
        else
        {
            byte[] hashedPassword = HashingAlgorithm.useSHA256(data[1].getBytes(StandardCharsets.UTF_8));
            Message newMessage = new Message();
            User receiver = new User();
            receiver.setUsername(data[0]);
            newMessage.setReceiver(receiver);
            newMessage.setPassword(HashingAlgorithm.getHexRepresentation(hashedPassword));
            newMessage.setMessage_id(data[3]);
            String messageContent = data[4];
            String HMAC = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.HMAC_SHA256(messageContent.getBytes(StandardCharsets.UTF_8),hashedPassword));
            messageContent+= "####&&&&&####"+HMAC;
            newMessage.setContent(messageContent);
            this.allMessages.add(newMessage);
            this.isMessageCreated = true;
            message = "Your message has been sent!";
        }
        return message;

    }
    public User findUser(String username){
        for(User u : allUsers){
            if(u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }
    public boolean verifyPassword(String username,String enteredPassword,User foundUser){
        byte[] salt = this.allSalts.get(username).getBytes(StandardCharsets.UTF_8);
        if(HashingAlgorithm.getHexRepresentation(HashingAlgorithm.hash(salt,enteredPassword)).equals(foundUser.getPassword())){
            return true;
        }
        return false;
    }
    public Message findMessage(String messageID)
    {
        for(Message message : this.allMessages)
        {
            if(message.getMessage_id().equals(messageID)){
                return message;
            }
        }
        return null;
    }
    public boolean checkMessagePassword(Message message,String enteredPassword)
    {
        String passwordHash = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.useSHA256(enteredPassword.getBytes(StandardCharsets.UTF_8)));
        return message.getPassword().equals(passwordHash);

    }

    public boolean checkHMAC(Message foundMessage,String messagePassword){
        String[] content = foundMessage.getContent().split("####&&&&&####");
        byte[] hashedPassword = HashingAlgorithm.useSHA256(messagePassword.getBytes(StandardCharsets.UTF_8));
        String HMAC = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.HMAC_SHA256(content[0].getBytes(StandardCharsets.UTF_8),hashedPassword));
        return content[1] == HMAC;
    }

    public boolean verifyMessageReceiver(Message foundMessage,User viewer)
    {
        if(foundMessage.getReceiver().equals(viewer)){
            return true;
        }
        return false;
    }

    public String checkData(String[] data,Message[] foundMessage)
    {
        User user = findUser(data[2]);
        if(user == null){
            return "There is no user with this username on the system";
        }
        boolean passwordCheck = verifyPassword(data[2],data[3],user);
        if(!passwordCheck){
            return "Password is not correct!";
        }
        Message message = findMessage(data[0]);
        if(message == null){
            return "There is no message with this message ID on the system";
        }
        foundMessage[0] = message;
        boolean receiverCheck = verifyMessageReceiver(message,user);
        if(!receiverCheck){
            return "You are not authorized to view this message";
        }
        boolean messagePasswordCheck = checkMessagePassword(message,data[1]);
        if(!messagePasswordCheck){
            return "Message password is not correct";
        }
        boolean HMACCheck = checkHMAC(message,data[1]);
        if(!HMACCheck){
            return "There are problems with the integrity or authenticity of the message";
        }
        return "index:";
    }

    public String viewMessage(String[] data) throws IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        this.allMessages = service.fetchAllMessages();
        Message[] foundMessage = new Message[1];
        String message = checkData(data,foundMessage);
        if(message.length() != 0)
        {
            message+= this.allMessages.indexOf(foundMessage);
        }
        return message;
    }

    public void print() throws IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        //this.service.readMessages();
        for(Message m : allMessages){
            System.out.println(m.toString());
        }
    }
    public boolean confirmPassword(String password,String confirmPassword){
        String hex1 = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.useSHA256(password.getBytes(StandardCharsets.UTF_8)));
        String hex2 = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.useSHA256(confirmPassword.getBytes(StandardCharsets.UTF_8)));
        return hex1.equals(hex2);
    }
    public boolean confirmMessageID(String candidateID)
    {
        boolean control = true;
        for(Message message : this.allMessages){
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
