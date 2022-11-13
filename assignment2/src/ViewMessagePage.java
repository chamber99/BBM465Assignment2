import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewMessagePage extends Page implements ActionListener {
    private Container container;

    private JTextArea messageArea;

    private JButton returnButton;

    private Controller controller;

    private Message message;

    public ViewMessagePage(Controller controller, Message message) {
        this.controller = controller;
        setJFrame(new JFrame("Message"));
        getJFrame().setResizable(false);
        getJFrame().setLayout(null);
        getJFrame().setBounds(300, 90, 600, 600);
        this.container = getJFrame().getContentPane();

        this.messageArea = new JTextArea();
        this.messageArea.setFont(new Font("Arial", Font.PLAIN, 15));
        this.messageArea.setSize(400, 350);
        this.messageArea.setLocation(100, 50);
        this.messageArea.setEditable(false);

        this.messageArea.setText(message.getContent());
        this.messageArea.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        this.container.add(this.messageArea);


        this.returnButton = new JButton("Return");
        this.returnButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.returnButton.setSize(140, 40);
        this.returnButton.setLocation(230, 450);
        this.returnButton.addActionListener(this);

        this.messageArea.setAutoscrolls(true);


        this.container.add(this.returnButton);


        JScrollPane scroll = new JScrollPane(messageArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setSize(400, 350);
        scroll.setLocation(100, 50);

        getJFrame().add(scroll);


        getJFrame().setVisible(true);

    }

    public void setMessage(Message message) {
        this.messageArea.setText(message.getContent());
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
