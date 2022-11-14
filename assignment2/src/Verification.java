import javax.swing.*;

public class Verification extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        System.out.println(input.getParent().getName());

        //bu şekilde çalışıyor.
        if(input.getClass().equals(JPasswordField.class)){
            System.out.println("passwordfield");

            //input.getParent().getName(); ile check yapılır.
            //kendime notlar alıyorum.
            return true;
        } else if (input.getClass().equals(JTextField.class)) {
            System.out.println("textfield");
            return true;
        }else if(input.getClass().equals(JTextArea.class)){
            System.out.println("textarea");
            return true;
        }else{
            System.out.println("failed");
            return false;
        }
    }
}
