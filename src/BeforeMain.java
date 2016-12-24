
import javax.swing.*;
import java.awt.event.*;

public class BeforeMain extends JFrame {

    private JButton main;

    public BeforeMain() {
        

        main = new JButton("Main Page");
        add(main);

        
        main.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Main().setVisible(true);
                dispose();
            }
        });

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new BeforeMain();
    }
    

}
