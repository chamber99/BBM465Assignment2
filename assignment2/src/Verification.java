import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Verification extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        System.out.println(input.getParent().getName());

        //bu şekilde çalışıyor.
        if(input.getClass().equals(JPasswordField.class)){
            if(((JPasswordField) input).getPassword().length < 8){
                JOptionPane.showMessageDialog(input.getParent(),"Password length should be at least 8 chars.","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } else if (input.getClass().equals(JTextField.class)) {

            return true;
        }else if(input.getClass().equals(JTextArea.class)){
            System.out.println("textarea");
            return true;
        }else{
            System.out.println("failed");
            return false;
        }
    }

    public boolean verifyUserRegister(JPasswordField passwordField, JTextField username, String[] usernames){
        boolean passwordValid = true;
        boolean usernameValid = true;

        String errorMessage = "";

        if(passwordField.getPassword().length < 6){
            passwordValid = false;
            errorMessage += " * Password should be at least 6 characters long.\n";
        }

        if(Arrays.stream(usernames).anyMatch(s -> s.equals(username.getText()))){
            usernameValid = false;
            errorMessage += " * This username already exists on the system!\n";
        }

        if(!(passwordValid && usernameValid)){
            JOptionPane.showMessageDialog(passwordField.getParent(),errorMessage,"Error",JOptionPane.ERROR_MESSAGE);
        }

        return passwordValid && usernameValid;
    }


    public boolean verifyRegisterMessage(JTextField passwordTextField, JTextField confirmPasswordTextField, JTextField codenameTextField) {
        boolean passwordValid = true;
        boolean codenameValid = true;

        String errorMessage = "";

        if(passwordTextField.getText().length() < 5){
            passwordValid = false;
            errorMessage += " * Message password should be at least 5 characters.\n";
        }

        if(!passwordTextField.getText().equals(confirmPasswordTextField.getText())){
            passwordValid = false;
            errorMessage += " * Password and Confirm password does not match!\n";
        }

        if(codenameTextField.getText().length() < 5){
            codenameValid = false;
            errorMessage += " * Codename for the message should be at least 5 characters.\n";
        }

        if(!(codenameValid && passwordValid)){
            JOptionPane.showMessageDialog(passwordTextField.getParent(),errorMessage,"Error",JOptionPane.ERROR_MESSAGE);
        }

        return passwordValid && codenameValid;
    }

    public boolean verifyUserAccess(boolean[] user, boolean[] message){
        String error = "";
        boolean userFound = user[0];
        boolean passwordValid = user[1];

        boolean messageFound = message[0];
        boolean messagePass = message[1];

        if(!userFound){
            error += " * There is no user with that username on the system! \n";
        }
        if(!passwordValid){
            error += " * The password for the user is wrong! \n";
        }
        if(!messageFound){
            error += " * No message was found with that codename. \n";
        }
        if(!messagePass){
            error += " * Wrong password for the message. \n";
        }


        return userFound && passwordValid && messageFound && messagePass;
    }

}
