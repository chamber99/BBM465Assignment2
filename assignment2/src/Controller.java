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
        this.isUserCreated = false;
        this.isMessageCreated = false;
    }

    // This method opens the page that is indicated by the taken parameter.
    public void openPage(int pageNumber)
    {
        //Each page is represented by a constant integer value.
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
    // Overloaded method to open the view message page. While this page is being opened,a message is needed to open it.
    public void openPage(String index){
        Message viewedMessage = allMessages.get(Integer.parseInt(index));
        // Before opening the page,replacing all newlines with \n
        viewedMessage.setContent(viewedMessage.getContent().replaceAll("###---newline---###","\n"));
        new ViewMessagePage(this,viewedMessage);
    }
    // This method creates a new user by using the input values that come from the view.
    public void createNewUser(String username,String password) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        // Creating password hash.
        byte[] hashedPassword = HashingAlgorithm.useSHA256(password.getBytes(StandardCharsets.UTF_8));
        // Creating a new user.
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(HashingAlgorithm.getHexRepresentation(hashedPassword));
        // Adding new user to the user list.
        this.allUsers.add(newUser);
        // Inform the system for future updates of the database.
        this.isUserCreated = true;

    }
    public void storeUserData() throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        // If new users are registered to the system,database is updated in this method by using the service.
        if(isUserCreated){
            this.service.storeUsers(allUsers);
            this.isUserCreated = false;
        }
    }
    public void storeMessageData() throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        // If new messages are created,they are stored by using the service.
        if(isMessageCreated){
            this.service.storeMessages(allMessages);
            this.isMessageCreated = false;
        }

    }

    public String leaveMessage(String[] data) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
        String message;
        // Checking password that user entered.
        if(!confirmPassword(data[1],data[2])){
            message= "The passwords you entered do not match\n";
        }
        // Checking the new message ID.
       else if(!confirmMessageID(data[3])){
            message= "Please enter a unique message ID.\n";
        }
        else
        {
            // Creating the hash of the password.
            byte[] hashedPassword = HashingAlgorithm.useSHA256(data[1].getBytes(StandardCharsets.UTF_8));
            // Creating the message and the receiver.
            Message newMessage = new Message();
            User receiver = new User();
            receiver.setUsername(data[0]);
            newMessage.setReceiver(receiver);
            newMessage.setPassword(HashingAlgorithm.getHexRepresentation(hashedPassword));
            newMessage.setMessage_id(data[3]);
            String messageContent = data[4];
            messageContent = messageContent.replaceAll("\n","###---newline---###");
            newMessage.setContent(messageContent);
            // Adding new message to the message list.
            this.allMessages.add(newMessage);
            // Informing the system for database updates.
            this.isMessageCreated = true;
            message = "Your message has been sent!";
        }
        return message;

    }
    public User findUser(String username)
    {
        // This method searches for the user with the specified username.
        for(User u : allUsers){
            if(u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }
    public boolean verifyPassword(String enteredPassword,User foundUser){
        // Comparing hash value of the entered password and the hash of the user password.
        if(HashingAlgorithm.getHexRepresentation(HashingAlgorithm.useSHA256(enteredPassword.getBytes(StandardCharsets.UTF_8))).equals(foundUser.getPassword())){
            return true;
        }
        return false;
    }
    public Message findMessage(String messageID)
    {
        // This method searches the messages with the specified messageID.
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
        // This method compares the has of the entered password and the hash of the message password.
        String passwordHash = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.useSHA256(enteredPassword.getBytes(StandardCharsets.UTF_8)));
        return message.getPassword().equals(passwordHash);

    }

    public boolean verifyMessageReceiver(Message foundMessage,User viewer)
    {
        // This method compares the username of the viewer and the username of the message receiver.
        if(foundMessage.getReceiver().getUsername().equals(viewer.getUsername())){
            return true;
        }
        return false;
    }

    public String checkData(String[] data,Message[] foundMessage)
    {
        User user = findUser(data[2]);
        // Checking if a user with the specified username exists in the system.
        if(user == null){
            return "There is no such user with this username on the system.";
        }
        // Checking if the user has entered correct password.
        boolean passwordCheck = verifyPassword(data[3],user);
        if(!passwordCheck){
            return "User password is not correct!";
        }
        // Checking if a message with the entered messageID exists in the system.
        Message message = findMessage(data[0]);
        if(message == null){
            return "There is no message with this message ID on the system.";
        }
        // Checking if user is authorized to view this message.
        foundMessage[0] = message;
        boolean receiverCheck = verifyMessageReceiver(message,user);
        if(!receiverCheck){
            return "You are not authorized to view this message.";
        }
        // Checking if user has entered the correct password for the message.
        boolean messagePasswordCheck = checkMessagePassword(message,data[1]);
        if(!messagePasswordCheck){
            return "Message password is not correct!";
        }
        // If all checking process is done and program reaches this line,index of the found message is returned here.
        return "index:";
    }

    public String viewMessage(String[] data) throws IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        // Fetching all messages from the database.
        this.allMessages = service.fetchAllMessages();
        Message[] foundMessage = new Message[1];
        // Calling check function to verify the data provided by the user.
        String message = checkData(data,foundMessage);
        if(message.startsWith("index:"))
        {
            // If all data are verified,index of the related message is appended to the message text.
            message+= this.allMessages.indexOf(foundMessage[0]);
        }
        return message;
    }

    public boolean confirmPassword(String password,String confirmPassword){
        // Checking the hash values of the passwords.
        String hex1 = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.useSHA256(password.getBytes(StandardCharsets.UTF_8)));
        String hex2 = HashingAlgorithm.getHexRepresentation(HashingAlgorithm.useSHA256(confirmPassword.getBytes(StandardCharsets.UTF_8)));
        return hex1.equals(hex2);
    }
    public boolean confirmMessageID(String candidateID)
    {
        // Checking the system for the candidate message ID.
        boolean control = true;
        for(Message message : this.allMessages){
            // If ID already exists on the system, returning false.
            if(message.getMessage_id().equals(candidateID)){
                control = false;
                break;
            }
        }
        return control;
    }
    public String[] getUsernames()
    {
        // This function returns all users to the views by creating a string array.
        String[] usernames = new String[allUsers.size()];
        int index = 0;
        for(User u : allUsers){
            usernames[index++] = u.getUsername();
        }
        return usernames;
    }
}
