
import model.Message;
import model.User;
import views.AccessView;

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

        User a = new User();
        a.setUsername("Ahmet");

        User b1 = new User();
        b1.setUsername("Mehmet");

        JLabel label = new JLabel("Welcome to MessageBox");
        label.setBounds(130, 0, 100, 40);

        JComboBox<User> userBox = new JComboBox<>();
        userBox.setBounds(130, 50, 100, 40);
        userBox.addItem(a);
        userBox.addItem(b1);

        frame.add(userBox);

        AccessView accessView = new AccessView();

        frame.add(accessView);

        frame.add(label);


        JButton b = new JButton("Click 3 times to exit.");//creating instance of JButton
        b.setBounds(130, 100, 100, 40);//x axis, y axis, width, height

        frame.add(b);//adding button in JFrame

        frame.setSize(400, 500);//400 width and 500 height
        frame.setLayout(null);//using no layout managers
        frame.setVisible(true);//making the frame visible

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count[0]++;
                // Exits the program if clicked 3 times
                if (count[0] == 3) {
                    System.out.println(userBox.getSelectedItem().toString());
                    System.exit(0);
                }

            }
        });


    }


}
