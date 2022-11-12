import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewMessagePage implements ActionListener {
    private JFrame frame;
    private Container container;

    private JTextArea messageArea;

    private JButton returnButton;

    private Controller controller;

    public ViewMessagePage(Controller controller, Message message) {
        this.controller = controller;
        this.frame = new JFrame("Message");

        this.frame.setResizable(false);
        this.frame.setLayout(null);
        this.frame.setBounds(300, 90, 600, 600);
        this.container = this.frame.getContentPane();

        this.messageArea = new JTextArea();
        this.messageArea.setFont(new Font("Arial", Font.PLAIN, 15));
        this.messageArea.setSize(400, 350);
        this.messageArea.setLocation(100, 50);
        this.messageArea.setEditable(false);

        this.messageArea.setText(message.getContent());
        this.messageArea.setBorder(BorderFactory.createLineBorder(Color.black,1));

        this.container.add(this.messageArea);


        this.returnButton = new JButton("Return");
        this.returnButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.returnButton.setSize(140, 40);
        this.returnButton.setLocation(230, 450);
        this.returnButton.addActionListener(this);

        this.container.add(this.returnButton);

        this.frame.setVisible(true);

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
