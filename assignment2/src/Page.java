import javax.swing.*;
import java.awt.*;

public class Page
{
    private JFrame jFrame;

    public void closePage(){
        jFrame.setVisible(false);
    }
    public void openPage(){
        jFrame.setVisible(true);
    }

    public JFrame getJFrame() {
        return jFrame;
    }

    public void setJFrame(JFrame jFrame) {
        this.jFrame = jFrame;
    }
}
