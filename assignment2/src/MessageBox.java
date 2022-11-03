
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MessageBox {
    private ArrayList<User> users;
    private ArrayList<Message> messages;
    public JFrame frame;


    public MessageBox() {
        final int[] count = {0};

        frame = new JFrame();


        JButton b = new JButton("Click 3 times to exit.");//creating instance of JButton
        b.setBounds(130,100,100, 40);//x axis, y axis, width, height

        frame.add(b);//adding button in JFrame

        frame.setSize(400,500);//400 width and 500 height
        frame.setLayout(null);//using no layout managers
        frame.setVisible(true);//making the frame visible

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count[0]++;
                // Exits the program if clicked 3 times
                if(count[0] == 3){
                    System.exit(0);
                }

            }
        });





    }






}
