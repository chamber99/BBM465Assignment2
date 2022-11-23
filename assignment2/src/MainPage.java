import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage implements ActionListener
{
    private Container container;
    private Controller controller;
    private JLabel title;
    private JButton createUser;
    private JButton access;
    private JButton leaveMessage;
    private JFrame jFrame;

    /*This page is the main page of the program.
    It acts as a navigation by providing buttons for 3 separate use cases to the user.
    These use cases are create user,access and leave a message.
    */
    public MainPage (Controller controller)
    {
        // Controller is used to perform business logic of the app. Every page is connected to the same controller.
        this.controller = controller;
        this.jFrame = new JFrame("Main Page");
        this.jFrame .setLayout(null);
        this.jFrame .setBounds(300,90,600,600);
        this.jFrame .setLocationRelativeTo(null);
        this.jFrame .setResizable(false);
        container = this.jFrame .getContentPane();
        this.container.setName("mainPage");
        title = new JLabel("Welcome to MessageBox");
        title.setFont(new Font("Arial", Font.PLAIN, 40));
        title.setSize(500, 50);
        title.setLocation(70, 100);
        container.add(title);

        // Creating a button to switch to the Create User Page
        createUser = new JButton("Create User");
        createUser.setFont(new Font("Arial", Font.PLAIN, 20));
        createUser.setSize(200, 50);
        createUser.setLocation(195, 200);
        createUser.setFocusPainted(false);
        // Adding action listener to trigger controller.
        createUser.addActionListener(this);
        container.add(createUser);

        // Creating a button to switch to the Access Message Page
        access = new JButton("Access");
        access.setFont(new Font("Arial", Font.PLAIN, 20));
        access.setSize(200, 50);
        access.setLocation(195, 270);
        // Adding action listener to trigger controller.
        access.addActionListener(this);
        container.add(access);

        // Creating a button to switch to the Register Form Page
        leaveMessage = new JButton("Leave a Message");
        leaveMessage.setFont(new Font("Arial", Font.PLAIN, 20));
        leaveMessage.setSize(200, 50);
        leaveMessage.setLocation(195, 340);
        // Adding action listener to trigger controller.
        leaveMessage.addActionListener(this);
        container.add(leaveMessage);
        this.jFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Controlling which button was clicked and opening the required page.
        if(e.getSource() == this.createUser){
            controller.openPage(1);
        }
        else if(e.getSource() == this.access){
            controller.openPage(2);
        }
        else if(e.getSource() == this.leaveMessage){
            controller.openPage(3);
        }
        // When a button is clicked,this frame is closed after performing related action.
        this.jFrame .setVisible(false);

    }
}
