import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.InvalidKeyException;

public class CreateUserPage implements ActionListener
{
    private JLabel username;
    private Container container;
    private JLabel title;
    private JTextField userTextField;
    private JLabel password;
    private JPasswordField passwordTextField;
    private JButton homeButton;
    private JButton resetButton;
    private JButton submitButton;
    private Controller controller;
    private JFrame jFrame;

    Verification verification;

    // Page for creating new users.
    public CreateUserPage(Controller controller)
    {
        this.verification = new Verification();
        this.controller = controller;
        this.jFrame = new JFrame("Create User");
        this.jFrame.setResizable(false);
        this.jFrame.setLayout(null);
        this.jFrame.setBounds(300,90,600,600);
        this.jFrame.setLocationRelativeTo(null);
        // Creating window listener to store data when user closes window.
        this.jFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                try {
                    // If user closes the windows,database is updated if necessary.
                    controller.storeUserData();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        });
        container =  this.jFrame.getContentPane();
        this.container.setName("createUserPage");
        title = new JLabel("Create User");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 50);
        title.setLocation(210, 100);
        container.add(title);
        username = new JLabel("Username");
        username.setFont(new Font("Arial", Font.PLAIN, 20));
        username.setSize(100, 20);
        username.setLocation(115, 175);
        container.add(username);
        // Text field to take username.
        userTextField = new JTextField();
        userTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        userTextField.setSize(195, 30);
        userTextField.setLocation(240, 175);
        container.add(userTextField);
        password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.PLAIN, 20));
        password.setSize(100, 20);
        password.setLocation(115, 225);
        container.add(password);
        // Password field to take user password
        passwordTextField = new JPasswordField();
        passwordTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordTextField.setSize(195, 30);
        passwordTextField.setLocation(240, 225);
        container.add(passwordTextField);

        // Creating home button to go back the main page.
        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.PLAIN, 15));
        homeButton.setSize(100, 20);
        homeButton.setLocation(115, 320);
        homeButton.addActionListener(this);
        container.add(homeButton);

        // Creating reset button to clear input fields.
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 15));
        resetButton.setSize(100, 20);
        resetButton.setLocation(225, 320);
        resetButton.addActionListener(this);
        container.add(resetButton);

        // Creating submit button to trigger controller.
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 15));
        submitButton.setSize(100, 20);
        submitButton.setLocation(335, 320);
        submitButton.addActionListener(this);
        container.add(submitButton);
        this.jFrame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        try {
            if(e.getSource() == this.submitButton){
                // If user clicks the submit button and input fields are verified,controller is used here to create user.
                if(this.verification.verifyUserRegister(passwordTextField,userTextField,controller.getUsernames())){
                    controller.createNewUser(userTextField.getText(), String.valueOf(passwordTextField.getPassword()));
                    userTextField.setText("");
                    passwordTextField.setText("");
                }
            }
            else if(e.getSource() == this.homeButton)
            {
                // If user go backs to the main page and new users are created,database is updated by controller.
                this.jFrame.setVisible(false);
                controller.openPage(0);
                controller.storeUserData();
            }
            else{
                // Reset button clears all input fields.
                userTextField.setText("");
                passwordTextField.setText("");
            }
        } catch (IOException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException ioException) {
            ioException.printStackTrace();
        }
    }

}

