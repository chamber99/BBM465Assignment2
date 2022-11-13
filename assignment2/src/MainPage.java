import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends Page implements ActionListener
{
    Container container;
    Controller controller;
    JLabel title;
    JButton createUser;
    JButton access;
    JButton leaveMessage;

    public MainPage (Controller controller)
    {
        this.controller = controller;
        setJFrame(new JFrame("Main Page"));
        getJFrame().setLayout(null);
        getJFrame().setBounds(650,250,600,600);
        container = getJFrame().getContentPane();
        title = new JLabel("Welcome to MessageBox");
        title.setFont(new Font("Arial", Font.PLAIN, 40));
        title.setSize(500, 50);
        title.setLocation(70, 100);
        container.add(title);
        this.getJFrame().setResizable(false);

        createUser = new JButton("Create User");
        createUser.setFont(new Font("Arial", Font.PLAIN, 20));
        createUser.setSize(200, 50);
        createUser.setLocation(195, 200);
        createUser.addActionListener(this);
        container.add(createUser);

        access = new JButton("Access");
        access.setFont(new Font("Arial", Font.PLAIN, 20));
        access.setSize(200, 50);
        access.setLocation(195, 270);
        access.addActionListener(this);
        container.add(access);

        leaveMessage = new JButton("Leave a Message");
        leaveMessage.setFont(new Font("Arial", Font.PLAIN, 20));
        leaveMessage.setSize(200, 50);
        leaveMessage.setLocation(195, 340);
        leaveMessage.addActionListener(this);
        container.add(leaveMessage);
        getJFrame().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getJFrame().setVisible(false);
        if(e.getSource() == this.createUser){
            controller.openPage(1);
        }
        else if(e.getSource() == this.access){
            controller.openPage(2);
        }
        else if(e.getSource() == this.leaveMessage){
            controller.openPage(3);
        }

    }
}
