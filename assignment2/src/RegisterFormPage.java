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

public class RegisterFormPage extends Page implements ActionListener
{
    private Container container;
    private JLabel targetUsername;
    private JComboBox allUsers;
    private JLabel password;
    private JTextField passwordTextField;
    private JLabel confirmPassword;
    private JTextField confirmPasswordTextField;
    private JLabel messageCodeName;
    private JTextField codenameTextField;
    private JLabel messageEntry;
    private JTextArea messageEntryTextField;
    private JButton homeButton;
    private JButton sendButton;
    private JLabel result;
    private Controller controller;

    public RegisterFormPage(Controller controller)
    {
        this.controller = controller;
        setJFrame(new JFrame("Register Form"));
        getJFrame().setResizable(false);
        getJFrame().setLayout(null);
        getJFrame().setBounds(300,90,600,600);
        this.container = getJFrame().getContentPane();
        getJFrame().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                try {
                    controller.storeMessageData();
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

        this.targetUsername = new JLabel("Auth.username*");
        this.targetUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        this.targetUsername.setSize(150, 20);
        this.targetUsername.setLocation(15, 40);
        this.container.add(this.targetUsername);
        this.allUsers = new JComboBox(this.controller.getUsernames());
        this.allUsers.setSize(100,30);
        this.allUsers.setLocation(170,35);
        this.container.add(this.allUsers);
        this.password = new JLabel("Password*");
        this.password.setFont(new Font("Arial", Font.PLAIN, 14));
        this.password.setSize(150, 20);
        this.password.setLocation(15, 90);
        this.container.add(password);
        this.passwordTextField = new JPasswordField();
        this.passwordTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        this.passwordTextField.setSize(100, 30);
        this.passwordTextField.setLocation(170, 85);
        this.container.add(passwordTextField);
        this.confirmPassword = new JLabel("Confirm Password*");
        this.confirmPassword .setFont(new Font("Arial", Font.PLAIN, 14));
        this.confirmPassword .setSize(150, 20);
        this.confirmPassword .setLocation(300, 90);
        this.container.add(confirmPassword);
        this.confirmPasswordTextField = new JPasswordField();
        this.confirmPasswordTextField .setFont(new Font("Arial", Font.PLAIN, 15));
        this.confirmPasswordTextField .setSize(100, 30);
        this.confirmPasswordTextField .setLocation(450, 85);
        this.container.add(confirmPasswordTextField );
        this.messageCodeName = new JLabel("Message Codename*");
        this.messageCodeName .setFont(new Font("Arial", Font.PLAIN, 14));
        this.messageCodeName.setSize(220, 20);
        this.messageCodeName.setLocation(15, 150);
        this.container.add(messageCodeName);
        this.codenameTextField = new JTextField();
        this.codenameTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        this.codenameTextField.setSize(100, 30);
        this.codenameTextField.setLocation(170, 145);
        this.container.add(this.codenameTextField);
        this.messageEntry = new JLabel("ENTER YOUR MESSAGE*");
        this.messageEntry.setFont(new Font("Arial", Font.PLAIN, 15));
        this.messageEntry.setSize(200, 20);
        this.messageEntry.setLocation(15, 300);
        this.container.add(this.messageEntry);
        this.messageEntryTextField = new JTextArea();
        this.messageEntryTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        this.messageEntryTextField.setBounds(250,220,300,200);
        this.messageEntryTextField.setBorder(BorderFactory.createLineBorder(Color.black,1));
        this.container.add(messageEntryTextField);
        this.homeButton = new JButton("Home");
        this.homeButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.homeButton.setSize(150, 40);
        this.homeButton.setLocation(120, 450);
        this.homeButton.addActionListener(this);
        container.add(this.homeButton);

        this.sendButton = new JButton("Create Message");
        this.sendButton .setFont(new Font("Arial", Font.PLAIN, 15));
        this.sendButton .setSize(150, 40);
        this.sendButton .setLocation(300, 450);
        this.sendButton .addActionListener(this);
        container.add(this.sendButton);

        this.result = new JLabel("");
        this.result.setForeground(Color.red);
        this.result .setFont(new Font("Arial", Font.PLAIN, 12));
        this.result .setSize(300, 20);
        this.result .setLocation(15, 330);
        this.container.add(this.result );
        getJFrame().setVisible(true);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.sendButton)
        {
            String targetUsername = (String)this.allUsers.getItemAt(this.allUsers.getSelectedIndex());
            String password = this.passwordTextField.getText();
            String confirmPassword = this.confirmPasswordTextField.getText();
            String messageID = this.codenameTextField.getText();
            String messageContent = this.messageEntryTextField.getText();
            String[] data = {targetUsername,password,confirmPassword,messageID,messageContent};
            String message = "";
            try {
                message = controller.leaveMessage(data);
                this.result.setText(message);
            } catch (IllegalBlockSizeException illegalBlockSizeException) {
                illegalBlockSizeException.printStackTrace();
            } catch (BadPaddingException badPaddingException) {
                badPaddingException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InvalidKeyException invalidKeyException) {
                invalidKeyException.printStackTrace();
            }
        }
        else if(e.getSource() == this.homeButton){
            getJFrame().setVisible(false);
            controller.openPage(0);
            try {
                controller.storeMessageData();
            } catch (IllegalBlockSizeException illegalBlockSizeException) {
                illegalBlockSizeException.printStackTrace();
            } catch (BadPaddingException badPaddingException) {
                badPaddingException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InvalidKeyException invalidKeyException) {
                invalidKeyException.printStackTrace();
            }
        }
        else{
            try {
                controller.print();
            } catch (IllegalBlockSizeException illegalBlockSizeException) {
                illegalBlockSizeException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (BadPaddingException badPaddingException) {
                badPaddingException.printStackTrace();
            } catch (InvalidKeyException invalidKeyException) {
                invalidKeyException.printStackTrace();
            }

        }

    }
}
