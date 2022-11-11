import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage implements ActionListener
{
    JFrame frame;
    public MainPage ()
    {
        frame = new JFrame("Main Page");
        frame.setSize(600,600);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,1));
        JPanel headingPanel = new JPanel();
        JLabel heading = new JLabel("Welcome to MessageBox");
        heading.setFont(new Font("Arial", Font.PLAIN, 30));
        headingPanel.add(heading);
        mainPanel.add(headingPanel);
        JPanel buttonsPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS);
        buttonsPanel.setLayout(boxlayout);
        /*headingPanel.setBackground(Color.BLUE);
        buttonsPanel.setBackground(Color.red);*/
        headingPanel.setBorder(BorderFactory.createEmptyBorder(200,0,0,0));
        JButton firstButton = new JButton("Access");
        JButton secondButton = new JButton("Leave a Message");
        JButton thirdButton = new JButton("Create User");
        firstButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        firstButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        firstButton.addActionListener(this);
        secondButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        secondButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        thirdButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        thirdButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(firstButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonsPanel.add(secondButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonsPanel.add(thirdButton);
        mainPanel.add(buttonsPanel);
        /*mainPanel.add(heading);
        JButton jb1 = new JButton("Button 1");
        jb1.setBackground(Color.white);
        mainPanel.add(jb1);
        mainPanel.add(new JButton("Button 2"));
        mainPanel.add(new JButton("Button 3"));*/

        frame.add(mainPanel);
        /*JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JLabel heading = new JLabel("Welcome to MessageBox");
        heading.setFont(new Font("Arial", Font.PLAIN, 30));
        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        BoxLayout boxlayout2 = new BoxLayout(panel3, BoxLayout.Y_AXIS);
        panel.setLayout(boxlayout);
        panel3.setLayout(boxlayout2);
        panel.add(jb1);
        panel2.add(heading);
        panel3.add(panel2);
        panel3.add(panel);
        //panel.setBorder(BorderFactory.createEmptyBorder(100,100,0,0));
        frame.add(panel3);
        /*JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        listPane.add(new Button("Hello"));
        listPane.add(new Button("Hi"));*/
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setVisible(false);
        //RegisterPage page = new RegisterPage();
        return;
    }
}
