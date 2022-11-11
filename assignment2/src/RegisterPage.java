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

public class RegisterPage implements ActionListener
{
    JFrame frame;
    JLabel username;
    Container container;
    JLabel title;
    JTextField userTextField;
    JLabel password;
    JTextField passwordTextField;
    JButton resetButton;
    JButton submitButton;
    Controller action;

    public RegisterPage(Controller controller)
    {
        this.action = controller;
        frame = new JFrame("Create User");
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setBounds(300,90,600,600);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                try {
                    action.done();
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
        container = frame.getContentPane();
        title = new JLabel("Create User");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 50);
        title.setLocation(215, 100);
        container.add(title);
        username = new JLabel("Username");
        username.setFont(new Font("Arial", Font.PLAIN, 20));
        username.setSize(100, 20);
        username.setLocation(100, 175);
        container.add(username);
        userTextField = new JTextField();
        userTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        userTextField.setSize(190, 30);
        userTextField.setLocation(225, 175);
        container.add(userTextField);
        password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.PLAIN, 20));
        password.setSize(100, 20);
        password.setLocation(100, 225);
        container.add(password);
        passwordTextField = new JPasswordField();
        passwordTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordTextField.setSize(190, 30);
        passwordTextField.setLocation(225, 225);
        container.add(passwordTextField);

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 15));
        resetButton.setSize(100, 20);
        resetButton.setLocation(205, 320);
        resetButton.addActionListener(this);
        container.add(resetButton);

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 15));
        submitButton.setSize(100, 20);
        submitButton.setLocation(315, 320);
        submitButton.addActionListener(this);
        container.add(submitButton);
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        try {
            if(e.getSource() == this.submitButton){
                action.createNewUser(userTextField.getText(),passwordTextField.getText());
                userTextField.setText("");
                passwordTextField.setText("");
            }
            else{
                userTextField.setText("");
                passwordTextField.setText("");
                action.fetch();
            }
        } catch (IOException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException ioException) {
            ioException.printStackTrace();
        }
    }

}
