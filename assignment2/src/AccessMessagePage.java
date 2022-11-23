import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;

// Page for accessing messages.
public class AccessMessagePage implements ActionListener {
    private JFrame jFrame;
    private Container container;
    private Controller controller;
    private Verification verification;
    private JTextField messageCodeName;
    private JLabel codeNameLabel;
    private JPasswordField messagePassword;
    private JLabel messagePasswordLabel;
    private JTextField userName;
    private JLabel userNameLabel;
    private JPasswordField userPassword;
    private JLabel userPasswordLabel;
    private JSeparator separator;
    private JCheckBox togglePasswordVisibility;
    private JButton viewButton;
    private JButton resetButton;
    private JButton homeButton;


    public AccessMessagePage(Controller controller) {
        this.verification = new Verification();
        this.controller = controller;
        this.jFrame = new JFrame("Message View");

        this.jFrame.setResizable(false);
        this.jFrame.setLayout(null);
        this.jFrame.setBounds(300, 90, 600, 600);
        this.jFrame.setLocationRelativeTo(null);

        this.container = this.jFrame.getContentPane();
        this.container.setName("accessMessagePage");

        this.codeNameLabel = new JLabel("Message Codename");
        this.codeNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.codeNameLabel.setSize(150, 20);
        this.codeNameLabel.setLocation(60, 40);

        this.container.add(this.codeNameLabel);

        // Creating a text field to take codename of the message
        this.messageCodeName = new JTextField();

        this.messageCodeName.setFont(new Font("Arial", Font.PLAIN, 15));
        this.messageCodeName.setSize(200, 30);
        this.messageCodeName.setLocation(250, 40);

        this.container.add(messageCodeName);

        // Creating password fields to take password of the message.
        this.messagePasswordLabel = new JLabel("Message Password");
        this.messagePasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.messagePasswordLabel.setSize(150, 20);
        this.messagePasswordLabel.setLocation(60, 100);

        this.container.add(this.messagePasswordLabel);

        this.messagePassword = new JPasswordField();

        this.messagePassword.setFont(new Font("Arial", Font.PLAIN, 15));
        this.messagePassword.setSize(200, 30);
        this.messagePassword.setLocation(250, 100);


        this.container.add(this.messagePassword);

        this.separator = new JSeparator();
        this.separator.setOrientation(0);
        this.separator.setSize(500, 130);

        this.separator.setLocation(50, 150);


        this.container.add(this.separator);

        this.userNameLabel = new JLabel("Username");
        this.userNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.userNameLabel.setSize(150, 20);
        this.userNameLabel.setLocation(60, 180);

        this.container.add(userNameLabel);

        // Creating text field to take username of the user.
        this.userName = new JTextField();
        this.userName.setFont(new Font("Arial", Font.PLAIN, 15));
        this.userName.setSize(200, 30);
        this.userName.setLocation(250, 180);

        this.container.add(userName);

        this.userPasswordLabel = new JLabel("User Password");
        this.userPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.userPasswordLabel.setSize(150, 20);
        this.userPasswordLabel.setLocation(60, 240);

        this.container.add(this.userPasswordLabel);
        // Creating password field to take the password of the user.
        this.userPassword = new JPasswordField();
        this.userPassword.setFont(new Font("Arial", Font.PLAIN, 15));
        this.userPassword.setSize(200, 30);
        this.userPassword.setLocation(250, 240);

        this.container.add(this.userPassword);

        // Creating checkbox and a listener to manipulate password visibility
        this.togglePasswordVisibility = new JCheckBox("Show Password");
        this.togglePasswordVisibility.setSize(170, 25);
        this.togglePasswordVisibility.setLocation(250, 300);


        togglePasswordVisibility.addChangeListener(changeEvent -> {
            if (togglePasswordVisibility.isSelected()) {
                userPassword.setEchoChar((char) 0);
                messagePassword.setEchoChar((char) 0);
            } else {
                userPassword.setEchoChar('\u25CF');
                messagePassword.setEchoChar('\u25CF');
            }
        });

        this.container.add(this.togglePasswordVisibility);

        // Creating a button to view message content.
        this.viewButton = new JButton("View");
        this.viewButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.viewButton.setSize(150, 40);
        this.viewButton.setLocation(130, 370);
        this.viewButton.addActionListener(this);

        this.container.add(this.viewButton);

        // Creating a reset button to clear all input fields.
        this.resetButton = new JButton("Reset");
        this.resetButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.resetButton.setSize(150, 40);
        this.resetButton.setLocation(320, 370);
        this.resetButton.addActionListener(this);
        this.container.add(this.resetButton);

        // Creating a button to go back to the main page.
        this.homeButton = new JButton("Home");
        this.homeButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.homeButton.setSize(150, 40);
        this.homeButton.setLocation(220, 430);
        this.homeButton.addActionListener(this);


        this.container.add(homeButton);
        this.jFrame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.homeButton) {
            // If home button is clicked,controller opens the main page.
            controller.openPage(0);
            this.jFrame.setVisible(false);
        } else if (e.getSource() == this.viewButton) {
            // If view button is clicked,all data is collected and sent to the controller.
            String[] data = {messageCodeName.getText(), new String(messagePassword.getPassword()), userName.getText(), new String(userPassword.getPassword())};

            try {
                String message = controller.viewMessage(data);
                // If user is authorized to view the message,controller returns the index of the message.
                if (message.startsWith("index")) {
                    this.jFrame.setVisible(false);
                    String index = message.split(":")[1];
                    // Controller opens the message content with the provided message.
                    controller.openPage(index);
                } else {
                    // If an error was occurred,a message dialog is showed on the screen.
                    JOptionPane.showMessageDialog(container, message, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalBlockSizeException illegalBlockSizeException) {
                illegalBlockSizeException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (BadPaddingException badPaddingException) {
                badPaddingException.printStackTrace();
            } catch (InvalidKeyException invalidKeyException) {
                invalidKeyException.printStackTrace();
            }

        } else {
            // If reset button was clicked,all input fields are cleared here.
            this.messagePassword.setText("");
            this.messageCodeName.setText("");
            this.userName.setText("");
            this.userPassword.setText("");
        }

    }
}
