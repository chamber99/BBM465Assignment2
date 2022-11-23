import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewMessagePage implements ActionListener {
    private Container container;

    private JTextArea messageArea;

    private JButton returnButton;

    private Controller controller;

    private JFrame jFrame;

    // Page for viewing the content of the accessed message.
    public ViewMessagePage(Controller controller, Message message) {
        this.controller = controller;
        this.jFrame = new JFrame("Message");
        this.jFrame.setResizable(false);
        this.jFrame.setLayout(null);
        this.jFrame.setBounds(300, 90, 600, 600);
        this.jFrame.setLocationRelativeTo(null);
        this.container = this.jFrame.getContentPane();

        // Creating text area to show the content of the message.
        this.messageArea = new JTextArea();
        this.messageArea.setFont(new Font("Arial", Font.PLAIN, 15));
        this.messageArea.setSize(400, 350);
        this.messageArea.setLocation(100, 50);
        this.messageArea.setEditable(false);
        this.messageArea.setWrapStyleWord(true);
        this.messageArea.setLineWrap(true);
        this.messageArea.setCaretPosition(0);

        // Setting the message content by using the input message.
        this.messageArea.setText(message.getContent());
        this.messageArea.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        //this.container.add(this.messageArea);

        // Creating a button to go back to the main page.
        this.returnButton = new JButton("Return");
        this.returnButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.returnButton.setSize(140, 40);
        this.returnButton.setLocation(230, 450);
        this.returnButton.addActionListener(this);

        this.messageArea.setAutoscrolls(true);

        this.container.add(this.returnButton);
        // Making message area scrollable to be able to show long messages both vertically and horizontally.

        JScrollPane scroll = new JScrollPane(messageArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setSize(400, 350);
        scroll.setLocation(100, 50);

        JScrollBar scrollbar = scroll.getVerticalScrollBar();
        int min = scrollbar.getMinimum();
        scrollbar.setValue(min);

        this.messageArea.requestFocus();
        this.messageArea.setCaretPosition(0);


        this.jFrame.add(scroll);

        this.jFrame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If user click the return button,controller opens the main page.
        controller.openPage(0);
        this.jFrame.setVisible(false);
    }
}
