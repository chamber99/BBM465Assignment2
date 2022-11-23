import javax.swing.*;

import java.util.Arrays;

public class Verification {
    public boolean verifyUserRegister(JPasswordField passwordField, JTextField username, String[] usernames){
        boolean passwordValid = true;
        boolean usernameValid = true;

        String errorMessage = "";
        String success = "The user "+username.getText()+" has been created successfully!";

        // Password length must be minimum 6
        if(passwordField.getPassword().length < 6){
            passwordValid = false;
            errorMessage += " * Password should be at least 6 characters long.\n";
        }

        // Preventing the creation of a user with a username that already exists in the system.
        if(Arrays.stream(usernames).anyMatch(s -> s.equals(username.getText()))){
            usernameValid = false;
            errorMessage += " * This username already exists on the system!\n";
        }

        // Showing a dialog according to the results.
        if(!(passwordValid && usernameValid)){
            JOptionPane.showMessageDialog(passwordField.getParent(),errorMessage,"Error",JOptionPane.ERROR_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(passwordField.getParent(),success,"Success",JOptionPane.INFORMATION_MESSAGE);
        }
        return passwordValid && usernameValid;
    }


    public boolean verifyRegisterMessage(JTextField passwordTextField, JTextField confirmPasswordTextField, JTextField codenameTextField) {
        boolean passwordValid = true;
        boolean codenameValid = true;

        String errorMessage = "";

        // Verification steps for the input fields of the leave message page.
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
}
