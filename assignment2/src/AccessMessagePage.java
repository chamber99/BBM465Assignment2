import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;

public class AccessMessagePage extends Page implements ActionListener {
    Container container;
    Controller controller;

    Verification verification;

    JTextField messageCodeName;
    JLabel codeNameLabel;

    JPasswordField messagePassword;
    JLabel messagePasswordLabel;

    JTextField userName;
    JLabel userNameLabel;

    JPasswordField userPassword;
    JLabel userPasswordLabel;

    JSeparator separator;

    JCheckBox togglePasswordVisibility;

    JButton viewButton;

    JButton resetButton;

    JButton homeButton;


    public AccessMessagePage(Controller controller) {
        this.verification = new Verification();
        this.controller = controller;
        setJFrame(new JFrame("Message View"));

        getJFrame().setResizable(false);
        getJFrame().setLayout(null);
        getJFrame().setBounds(300, 90, 600, 600);
        this.container = getJFrame().getContentPane();

        this.codeNameLabel = new JLabel("Message Codename");
        this.codeNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.codeNameLabel.setSize(150, 20);
        this.codeNameLabel.setLocation(60, 40);

        this.container.add(this.codeNameLabel);

        this.messageCodeName = new JTextField();
        this.messageCodeName.setInputVerifier(this.verification);
        this.messageCodeName.setFont(new Font("Arial", Font.PLAIN, 15));
        this.messageCodeName.setSize(200, 30);
        this.messageCodeName.setLocation(250, 40);

        this.container.add(messageCodeName);

        this.messagePasswordLabel = new JLabel("Message Password");
        this.messagePasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.messagePasswordLabel.setSize(150, 20);
        this.messagePasswordLabel.setLocation(60, 100);

        this.container.add(this.messagePasswordLabel);

        this.messagePassword = new JPasswordField();
        this.messagePassword.setInputVerifier(this.verification);
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

        this.userPassword = new JPasswordField();
        this.userPassword.setFont(new Font("Arial", Font.PLAIN, 15));
        this.userPassword.setSize(200, 30);
        this.userPassword.setLocation(250, 240);

        this.container.add(this.userPassword);

        this.togglePasswordVisibility = new JCheckBox("Show Password");
        this.togglePasswordVisibility.setSize(170,25);
        this.togglePasswordVisibility.setLocation(250,300);


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

        this.viewButton = new JButton("View");
        this.viewButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.viewButton.setSize(150, 40);
        this.viewButton.setLocation(130, 370);
        this.viewButton.addActionListener(this);

        this.container.add(this.viewButton);

        this.resetButton = new JButton("Reset");
        this.resetButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.resetButton.setSize(150, 40);
        this.resetButton.setLocation(320, 370);
        this.resetButton.addActionListener(this);

        this.container.add(this.resetButton);
        this.homeButton = new JButton("Home");
        this.homeButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.homeButton.setSize(150, 40);
        this.homeButton.setLocation(220, 430);
        this.homeButton.addActionListener(this);


        this.container.add(homeButton);
        getJFrame().setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.homeButton){
            this.getJFrame().setVisible(false);
            controller.openPage(0);
        }
        else if(e.getSource() == this.viewButton){
            String[] data = {messageCodeName.getText(),new String(messagePassword.getPassword()),userName.getText(),new String(userPassword.getPassword())};
            try {
                String message = controller.viewMessage(data);
                if(message.startsWith("index")){
                    this.getJFrame().setVisible(false);
                    String index = message.split(":")[1];
                    controller.openPage(index);
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
        }

    }
}
