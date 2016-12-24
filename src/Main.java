
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

public class Main extends JFrame {

    //Java Swing Initialiation
    private JPanel optionPanel, statusPanel, imagePanel;
    private JTabbedPane tabMenu;
    //Menu Bar Initialization
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem exitMenu, refreshMenu;
    //Status Panel JLabel
    private JLabel dateLabel, timeLabel;
    //System Modules Panel
    private ManageStaffPanel staffPanel = new ManageStaffPanel();
    private ManageBusPanel busPanel = new ManageBusPanel();
    private ManageSeatPanel seatPanel = new ManageSeatPanel();
    private ManageSchedulePanel schedulePanel = new ManageSchedulePanel();
    private ManageTransactionPanel transactionPanel = new ManageTransactionPanel();
    private ManagePaymentPanel paymentPanel = new ManagePaymentPanel();
    private PaymentStatus mePanel = new PaymentStatus();
    //Date&Time Initialization
    private Calendar cal;
    ImageIcon buslogo = new ImageIcon(getClass().getResource("/image/buslogo.jpg"));
    JLabel blogo = new JLabel("", buslogo, SwingConstants.LEFT);
    ImageIcon buslogo1 = new ImageIcon(getClass().getResource("/image/buslogo1.jpg"));
    JLabel blogo1 = new JLabel("", buslogo1, SwingConstants.LEFT);

    public Main() {

        setTitle("KMB Bus System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        refreshMenu = new JMenuItem("Refresh");
        exitMenu = new JMenuItem("Exit");

        menuBar.add(fileMenu);
        fileMenu.add(refreshMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitMenu);

        setJMenuBar(menuBar);
        refreshMenu.addActionListener(new MenuItemListener());
        exitMenu.addActionListener(new MenuItemListener());

        optionPanel = new JPanel(new GridLayout(1, 1));
        statusPanel = new JPanel(new GridLayout(1, 3));
        imagePanel = new JPanel(new GridLayout(1, 2));

        dateLabel = new JLabel("", JLabel.CENTER);
        dateLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        timeLabel = new JLabel("", JLabel.CENTER);
        timeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        Thread time = new Thread() {
            @Override
            public void run() {
                //non-stop loop
                while (true) {
                    cal = new GregorianCalendar(); //calendar format
                    int year = cal.get(Calendar.YEAR);
                    String month = new SimpleDateFormat("MMM").format(cal.getTime());
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    String weekday = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
                    int hour = cal.get(Calendar.HOUR);
                    int minute = cal.get(Calendar.MINUTE);
                    int second = cal.get(Calendar.SECOND);

                    dateLabel.setText("Date: " + padElement(day, '0') + " "
                            + month + " " + year + " ( " + weekday + " )");
                    timeLabel.setText("Time: " + padElement(hour, '0') + ":"
                            + padElement(minute, '0') + ":" + padElement(second, '0'));

                    try {
                        sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        time.start();

        statusPanel.add(dateLabel);
        statusPanel.add(timeLabel);

        tabMenu = new JTabbedPane(JTabbedPane.TOP);
                tabMenu.addTab("Staff", null, staffPanel, "To handle all staff information.");
                tabMenu.addTab("Bus", null, busPanel, "To maintain all the bus in company.");
//                tabMenu.addTab("Seat", null, seatPanel, "To maintain all the seat in bus.");
                tabMenu.addTab("Bus Schedule", null, schedulePanel, "To manage the schedule.");
                tabMenu.addTab("Booking", null, transactionPanel, "To manage all the transaction.");
                tabMenu.addTab("Payment", null, paymentPanel, "To manage all the transaction.");
                staffPanel.setBackground(Color.getHSBColor(255, 164, 101));
                busPanel.setBackground(Color.getHSBColor(305, 41, 80));
                seatPanel.setBackground(Color.green);
                schedulePanel.setBackground(Color.getHSBColor(83, 45, 83));
                transactionPanel.setBackground(Color.getHSBColor(83, 45, 83));
                paymentPanel.setBackground(Color.getHSBColor(140, 275, 23));
                mePanel.setBackground(Color.getHSBColor(12, 45, 150));
                optionPanel.add(tabMenu);

        imagePanel.add(blogo);
        imagePanel.add(blogo1);

        add(imagePanel, BorderLayout.NORTH);
        add(optionPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    //convert single digit form to two digits form
    private String padElement(int digit, char padChar) {
        String result = "";
        if (digit < 10) {
            result = result.concat(String.valueOf(padChar));
        }
        result = result.concat(String.valueOf(digit));
        return result;
    }

    private class MenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exitMenu) {
                int exitOption = JOptionPane.showConfirmDialog(null, "Confirm exit?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (exitOption == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            } else if (e.getSource() == refreshMenu) {
                new Main().setVisible(true);;
                dispose();
            } 

        }
    }
}
