
import control.MaintainStaff;
import control.MaintainBus;
import control.MaintainSeat;
import control.MaintainSchedule;
import control.MaintainTransaction;
import control.MaintainPayment;
import domain.Staff;
import domain.Bus;
import domain.Seat;
import domain.Schedule;
import domain.Transaction;
import domain.Payment;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;

public class ManagePaymentPanel extends JPanel {

    private MaintainStaff staffControl;
    private MaintainBus busControl;
    private MaintainSeat seatControl;
    private MaintainSchedule scheduleControl;
    private MaintainTransaction transactionControl;
    private MaintainPayment paymentControl;
    //---------------------------Table panels-------------------------------------
    private JPanel p1, tablePanel, buttonPanel, cardPanel;
    //---------------------------Table Objects----------------------------------
    private JScrollPane tableScrollPane;
    private TableRowSorter<TableModel> sorter;
    private JTable recordTable;
    private ListSelectionModel selectedList;
    private Object[] colHead = {"Transaction id", "Transaction Date", "Seat Purchase", "Seat 1", "Seat 2", "Seat 3", "Seat 4", "Booking Status", "Payment Status", "Schedule id"};
    //--------------------------------Option buttons--------------------------------
    private JButton jbtCreate, jbtUpdate;
    //-------------------------------CreateForm() objects--------------------------
    private JPanel datePanel;
    private JTextField jtfScheduleId, jtfTransactionId, jtfPaymentId, jtfPaymentDate, jtfTotalAmount, jtfPaymentAmount,
            jtfBalance, jtfCardNo, jtfCardHolder, jtfBank, jtfCardType, jtfExpiryDate, jtfTicketAmount, jtfGST, jtfBookingStatus, jtfPaymentStatus;
    private JButton jbtSubmit, jbtCancel;
    private ButtonGroup radioGroup, cardGroup;
    private JRadioButton cashButton, cardButton;
    private JRadioButton visaButton, masterButton;
    //---------------------------Date objects in form-----------------------------
    private ArrayList<String> calYear = new ArrayList<String>();
    private ArrayList<String> calMonth = new ArrayList<String>();
    private ArrayList<String> calDay = new ArrayList<String>();
    private JComboBox yearList, monthList, dayList, bankList;
    //------------------- state objects in form-----------------------
    private String bank[] = {"Public Bank", "MayBank", "Hong Leong Bank", "Standard Chartered Bank", "OCBC Bank", "RHB Bank", "HSBC Bank", "CIMB Bank", "AmBank"};
    //---------------------------MVC objects----------------------------------
    private DefaultTableModel tableModel;
    private DefaultComboBoxModel bankModel = new DefaultComboBoxModel(bank);

    public ManagePaymentPanel() {
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        scheduleControl = new MaintainSchedule();
        busControl = new MaintainBus();
        seatControl = new MaintainSeat();
        transactionControl = new MaintainTransaction();
        staffControl = new MaintainStaff();
        paymentControl = new MaintainPayment();
        createTable();
    }

    public void createTable() {
        int vertical = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int horizontal = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

        tableModel = new DefaultTableModel(transactionControl.getAllRecord(), colHead);
        recordTable = new JTable(tableModel) {
            //To disbale table editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

//            To let column width auto adjust
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                // +5 to let some space and better read
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width + 50, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        //Table sorter

        sorter = new TableRowSorter<TableModel>(tableModel);
        recordTable.setRowSorter(sorter);

        //Auto resize off and show scroll bar at bottom
        recordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Only single selection in this table
        recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recordTable.setBackground(Color.getHSBColor(140, 275, 23));

        //Table will show action when list selected
        selectedList = recordTable.getSelectionModel();
        selectedList.addListSelectionListener(new ListSelectedListener());
        recordTable.setSelectionModel(selectedList);

        tableScrollPane = new JScrollPane(recordTable, vertical, horizontal);
        tablePanel = new JPanel(new GridLayout(1, 1));
        tablePanel.add(tableScrollPane);

        //Table filter
        datePanel = new JPanel(new GridLayout(1, 3, 5, 5));

        p1 = new JPanel(new BorderLayout());
        p1.add(tablePanel, BorderLayout.CENTER);

        jbtCreate = new JButton("Create new payment");
        jbtUpdate = new JButton("Cancel transaction");
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(jbtCreate);
        buttonPanel.add(jbtUpdate);
        jbtCreate.setEnabled(false);
        buttonPanel.add(Box.createVerticalStrut(15));

        jbtCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = recordTable.getSelectedRow();
                Object selectedObject = (Object) recordTable.getModel().getValueAt(
                        recordTable.convertRowIndexToModel(selectedRowIndex), 0);
                String transaction_id = selectedObject.toString();
                Transaction transaction = transactionControl.getRecord(transaction_id);
                if (transaction.getPayment_status().matches("AlreadyPay")) {
                    JOptionPane.showMessageDialog(null, "" + transaction_id + " already got payment.",
                            "Failed", JOptionPane.INFORMATION_MESSAGE);
                    clearPanel();
                    createTable();
                } else {
                    clearPanel();
                    add(createForm(1));
                }

            }
        });

        jbtUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = recordTable.getSelectedRow();
                Object selectedObject = (Object) recordTable.getModel().getValueAt(
                        recordTable.convertRowIndexToModel(selectedRowIndex), 0);
                String transaction_id = selectedObject.toString();
                Transaction transaction = transactionControl.getRecord(transaction_id);
                if (transaction.getPayment_status().matches("HaventPay") && transaction.getBooking_status().matches("InProcess")) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Did you want to make booking cancelation?",
                            "", JOptionPane.INFORMATION_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        cancelBooking();
                        clearPanel();
                        createTable();
                    } else {
                        clearPanel();
                        createTable();
                    }
                }
                else if (transaction.getPayment_status().matches("HaventPay") && transaction.getBooking_status().matches("Cancel")) {
                    JOptionPane.showMessageDialog(null, "The booking is already cancel.",
                            "", JOptionPane.INFORMATION_MESSAGE);
            } else if (transaction.getPayment_status().matches("AlreadyPay") && transaction.getBooking_status().matches("Complete")) {
                    JOptionPane.showMessageDialog(null, "The booking is already finished make payment.",
                            "", JOptionPane.INFORMATION_MESSAGE);
            }
            }
        });

        //adding space between tablePanel and JTabbedPane
        add(Box.createVerticalStrut(20));
        add(p1, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel createForm(final int which) {
        int selectedRowIndex = recordTable.getSelectedRow();
        Object selectedObject = (Object) recordTable.getModel().getValueAt(
                recordTable.convertRowIndexToModel(selectedRowIndex), 0);
        String transaction_id = selectedObject.toString();
        Transaction transaction = transactionControl.getRecord(transaction_id);
        if (transaction != null) {

            DecimalFormat patternFormatter = new DecimalFormat("#, ###.00");
            double ticket = transaction.getSchedule().getPrice();
            int numberSeat = transaction.getSeatPurchase();
            double totalAmount = ticket * numberSeat;
            double gst = totalAmount * 0.06;
            double totalInclude = totalAmount + gst;

            jtfTransactionId = new JTextField();
            jtfPaymentId = new JTextField();
            jtfPaymentDate = new JTextField();
            jtfTicketAmount = new JTextField();
            jtfGST = new JTextField();
            jtfTotalAmount = new JTextField();
            jtfPaymentAmount = new JTextField();
            jtfBalance = new JTextField();
            jtfCardNo = new JTextField();
            jtfCardHolder = new JTextField();
            jtfExpiryDate = new JTextField();
            jtfCardType = new JTextField();
            jtfBank = new JTextField();
            jtfBookingStatus = new JTextField();
            jtfPaymentStatus = new JTextField();

            jbtSubmit = new JButton("Submit");
            jbtCancel = new JButton("Cancel");

            JPanel formPanel = new JPanel(new GridLayout(18, 4, 5, 5));
            JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            //Setup payment type
            cashButton = new JRadioButton("Cash");
            cashButton.setActionCommand("Cash");
            cashButton.setSelected(true);
            cardButton = new JRadioButton("Payment Card");
            cardButton.setActionCommand("Card");
            radioGroup = new ButtonGroup();
            radioGroup.add(cashButton);
            radioGroup.add(cardButton);
            radioPanel.add(cashButton);
            radioPanel.add(cardButton);

            visaButton = new JRadioButton("VISA");
            visaButton.setActionCommand("Visa");
            visaButton.setSelected(true);
            masterButton = new JRadioButton("MASTER");
            masterButton.setActionCommand("Master");
            cardGroup = new ButtonGroup();
            cardGroup.add(visaButton);
            cardGroup.add(masterButton);
            cardPanel.add(visaButton);
            cardPanel.add(masterButton);

            if (which == 1) {
                formPanel.setBackground(Color.CYAN);
                formPanel.setBorder(BorderFactory.createTitledBorder(null, "Create Payment",
                        TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
            }

            formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
            formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
            formPanel.add(new JLabel("Transaction ID"));
            formPanel.add(jtfTransactionId);
            formPanel.add(new JLabel("Payment Id"));
            formPanel.add(jtfPaymentId);
            formPanel.add(new JLabel("Payment Date"));
            formPanel.add(jtfPaymentDate);
            formPanel.add(new JLabel("Payment Type"));
            formPanel.add(radioPanel);
            formPanel.add(new JLabel("Ticket Amount"));
            formPanel.add(jtfTicketAmount);
            formPanel.add(new JLabel("GST"));
            formPanel.add(jtfGST);
            formPanel.add(new JLabel("Total Amount"));
            formPanel.add(jtfTotalAmount);
            formPanel.add(new JLabel("Payment Amount"));
            formPanel.add(jtfPaymentAmount);
            formPanel.add(new JLabel("Balance"));
            formPanel.add(jtfBalance);
            formPanel.add(new JLabel("Card Number"));
            formPanel.add(jtfCardNo);
            formPanel.add(new JLabel("Card Holder"));
            formPanel.add(jtfCardHolder);
            formPanel.add(new JLabel("Card Expiry Date"));
            formPanel.add(jtfExpiryDate);
            formPanel.add(new JLabel("Card Type"));
            formPanel.add(jtfCardType);
            formPanel.add(new JLabel(""));
            formPanel.add(cardPanel);
            formPanel.add(new JLabel("Bank"));
            formPanel.add(bankList = new JComboBox(bankModel));
            formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
            formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));

            jtfCardNo.setEditable(false);
            jtfCardNo.setText("No available");
            jtfCardHolder.setEditable(false);
            jtfCardHolder.setText("No available");
            jtfExpiryDate.setEditable(false);
            jtfExpiryDate.setText("No available");
            jtfCardType.setEditable(false);
            jtfCardType.setText("No available");
            jtfBank.setEditable(false);
            jtfBank.setText("No available");
            visaButton.setEnabled(false);
            masterButton.setEnabled(false);
            bankList.setEnabled(false);

            jtfTransactionId.setText(transaction_id);
            jtfTransactionId.setEditable(false);
            jtfPaymentId.setText(generatePaymentID());
            jtfPaymentId.setEditable(false);
            jtfPaymentDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date().getTime()));
            jtfPaymentDate.setEditable(false);
            jtfTicketAmount.setText("" + patternFormatter.format(totalAmount));
            jtfTicketAmount.setEditable(false);
            jtfGST.setText("" + patternFormatter.format(gst));
            jtfGST.setEditable(false);
            jtfTotalAmount.setText("" + patternFormatter.format(totalInclude));
            jtfTotalAmount.setEditable(false);
            jtfPaymentAmount.setText("");

            PaymentType paymentListener = new PaymentType();
            cashButton.addActionListener(paymentListener);
            cardButton.addActionListener(paymentListener);

            jtfPaymentAmount.setDocument(new PlainDocument() {
                public void insertString(int offs, String str, AttributeSet a)
                        throws BadLocationException {
                    if (getLength() + str.length() <= 10) {
                        super.insertString(offs, str, a);
                    }
                }
            });

            jtfPaymentAmount.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent documentEvent) {
                    onUpdate();
                }

                public void insertUpdate(DocumentEvent documentEvent) {
                    onUpdate();
                }

                public void removeUpdate(DocumentEvent documentEvent) {
                    onUpdate();
                }
            });

            formButtonPanel.add(jbtSubmit);
            formButtonPanel.add(jbtCancel);

            jbtSubmit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (which == 1) {
                        addPayment();
                    } else if (which == 2) {
                        //updateTransaction();
                    }
                }
            });

            jbtCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clearPanel();
                    createTable();
                }
            });

            add(Box.createVerticalStrut(10), BorderLayout.NORTH);
            add(new JScrollPane(formPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
            add(formButtonPanel, BorderLayout.SOUTH);
        }
        return new JPanel();
    }

    public boolean checkTextField() {
        boolean valid = true;

        if (jtfTransactionId.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Transaction Id cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfScheduleId.requestFocusInWindow();
            valid = false;
        } else if (jtfPaymentId.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Payment Id cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfPaymentId.requestFocusInWindow();
            valid = false;
        } else if (jtfPaymentAmount.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Payment Amount cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfPaymentAmount.requestFocusInWindow();
            valid = false;
        } else if (jtfBalance.getText().equals("")) {
            JOptionPane.showMessageDialog(null, " Balance cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfBalance.requestFocusInWindow();
            valid = false;
        }

        return valid;
    }

    public boolean checkTextField1() {
        boolean valid = true;

        if (jtfCardNo.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Card Number cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfCardNo.requestFocusInWindow();
            valid = false;
        }
        if (!jtfCardNo.getText().matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
            JOptionPane.showMessageDialog(null, "Card Number invalid format.For example, 9999-9999-9999-9999", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfCardNo.requestFocusInWindow();
            valid = false;
        } else if (jtfCardHolder.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Card Holder cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfCardHolder.requestFocusInWindow();
            valid = false;
        } else if (jtfExpiryDate.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Expiry Date cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfExpiryDate.requestFocusInWindow();
            valid = false;
        } else if (!jtfExpiryDate.getText().matches("\\d{4}-\\d{2}")) {
            JOptionPane.showMessageDialog(null, "Expiry Date invalid format. The date must be in format yyyy-MM. For example, 2017-10", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfExpiryDate.requestFocusInWindow();
            valid = false;
        }

        return valid;
    }

    public void addPayment() {
        Transaction transaction = transactionControl.getRecord(jtfTransactionId.getText());
        Payment payment = paymentControl.getRecord(jtfPaymentId.getText());

        if (payment == null) {
            if (checkTextField()) {
                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
                DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date paymentDate = null;

                String total_amount = jtfTotalAmount.getText();
                String payment_amount = jtfPaymentAmount.getText();
                String balance = jtfBalance.getText();
                try {

                    paymentDate = dateFormat2.parse(jtfPaymentDate.getText());
                    String payment_type = radioGroup.getSelection().getActionCommand();
                    String cardType = cardGroup.getSelection().getActionCommand();
                    double price1 = Double.parseDouble(total_amount);
                    double price2 = Double.parseDouble(payment_amount);
                    double price3 = Double.parseDouble(balance);
                    Date date = transaction.getDate();
                    Integer seatPurchase = transaction.getSeatPurchase();
                    String seat1 = transaction.getSeat();
                    String seat2 = transaction.getSeat1();
                    String seat3 = transaction.getSeat2();
                    String seat4 = transaction.getSeat3();
                    String schedule = transaction.getSchedule().getId();
                    Schedule sc = scheduleControl.getRecord(schedule);
                    String bookingStatus = transaction.getBooking_status();
                    String paymentStatus = transaction.getPayment_status();
                    jtfBookingStatus.setText("Complete");
                    jtfPaymentStatus.setText("AlreadyPay");

                    if (radioGroup.getSelection().getActionCommand().equals("Cash")) {
                        if (price2 < price1) {
                            JOptionPane.showMessageDialog(null, "The balance cannot be negative.",
                                    "Failed", JOptionPane.INFORMATION_MESSAGE);

                        } else {
                            Payment newPayment = new Payment(jtfPaymentId.getText(), paymentDate, payment_type, price1, price2, price3, jtfCardNo.getText(), jtfCardHolder.getText(),
                                    jtfExpiryDate.getText(), jtfCardType.getText(), jtfBank.getText(), transaction);
                            paymentControl.addRecord(newPayment);
                            paymentStatus = jtfPaymentStatus.getText();
                            bookingStatus = jtfBookingStatus.getText();
                            Transaction newTransaction = new Transaction(jtfTransactionId.getText(), date, seatPurchase, seat1, seat2, seat3, seat4, bookingStatus, paymentStatus, sc);
                            transactionControl.updateRecord(newTransaction);

                            JOptionPane.showMessageDialog(null, "New Payment added successfully.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            clearPanel();
                            createTable();
                        }

                    } else if (radioGroup.getSelection().getActionCommand().equals("Card")) {
                        if (checkTextField1()) {
                            String bank = bankList.getSelectedItem().toString();
                            Payment newPayment = new Payment(jtfPaymentId.getText(), paymentDate, payment_type, price1, price2, price3, jtfCardNo.getText(), jtfCardHolder.getText(),
                                    jtfExpiryDate.getText(), cardType, bank, transaction);
                            paymentControl.addRecord(newPayment);
                            paymentStatus = jtfPaymentStatus.getText();
                            Transaction newTransaction = new Transaction(jtfTransactionId.getText(), date, seatPurchase, seat1, seat2, seat3, seat4, bookingStatus, paymentStatus, sc);
                            transactionControl.updateRecord(newTransaction);
                            JOptionPane.showMessageDialog(null, "New Payment added successfully.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            clearPanel();
                            createTable();
                        }
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(ManageStaffPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Payment already exist by according to Payment ID you entered.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void cancelBooking() {
        int selectedRowIndex = recordTable.getSelectedRow();
        Object selectedObject = (Object) recordTable.getModel().getValueAt(
                recordTable.convertRowIndexToModel(selectedRowIndex), 0);
        String transaction_id = selectedObject.toString();
        Transaction transaction = transactionControl.getRecord(transaction_id);
        if (transaction != null) {
            Date transactionDate = transaction.getDate();
            int qt = transaction.getSeatPurchase();
            String seat0 = transaction.getSeat();
            String seat1 = transaction.getSeat1();
            String seat2 = transaction.getSeat2();
            String seat3 = transaction.getSeat3();
            String bookingStatus = transaction.getBooking_status();
            String paymentStatus = transaction.getPayment_status();
            Schedule schedule = transaction.getSchedule();
            String transactionId = transaction.getId();
            jtfBookingStatus = new JTextField();
            jtfBookingStatus.setText("Cancel");

            bookingStatus = jtfBookingStatus.getText();

            Transaction newTransaction = new Transaction(transactionId, transactionDate, qt, seat0, seat1, seat2, seat3, bookingStatus, paymentStatus, schedule);
            transactionControl.updateRecord(newTransaction);
            JOptionPane.showMessageDialog(null, "Booking cancel successfully.",
                                    "Success", JOptionPane.WARNING_MESSAGE);
            
        } else {
            JOptionPane.showMessageDialog(null, "",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    public void clearPanel() {
        removeAll();
        updateUI();
        repaint();
    }

    private class ListSelectedListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            Component[] component = buttonPanel.getComponents();
            int btnCount = 0;
            //Check hidden button exist or not
            for (int i = 0; i < component.length; i++) {
                if (component[i] instanceof JButton) {
                    btnCount++;
                    jbtCreate.setEnabled(true);
                }
            }
            //Get action
            if (btnCount != 1) {
                buttonPanel.add(jbtCreate);

            }
        }
    }

    public String generatePaymentID() {
        String payment = paymentControl.generatePaymentID();
        int paymentNo = Integer.parseInt(payment.substring(2, 5));
        String finalGeneratedPaymentID = String.format("P%04d", (paymentNo + 1));
        return finalGeneratedPaymentID;
    }

    private class DayComboBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.parseInt(yearList.getSelectedItem().toString()));
            cal.set(Calendar.MONTH, monthList.getSelectedIndex());
            calDay.clear();
            final int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = 0; i < maxDay; i++) {
                String day = "" + (i + 1);
                if (maxDay < 10) {
                    calDay.add("0" + day);
                } else {
                    calDay.add(day);
                }
            }
            datePanel.remove(dayList);
            datePanel.add(dayList = new JComboBox(calDay.toArray()));
        }
    }

    private class PaymentType implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cardButton) {

                visaButton.setEnabled(true);
                masterButton.setEnabled(true);
                CardType cardListener = new CardType();
                visaButton.addActionListener(cardListener);
                masterButton.addActionListener(cardListener);

                jtfCardNo.setDocument(new PlainDocument() {
                    public void insertString(int offs, String str, AttributeSet a)
                            throws BadLocationException {
                        if (getLength() + str.length() <= 19) {
                            super.insertString(offs, str, a);
                        }
                    }
                });

                jtfExpiryDate.setDocument(new PlainDocument() {
                    public void insertString(int offs, String str, AttributeSet a)
                            throws BadLocationException {
                        if (getLength() + str.length() <= 20) {
                            super.insertString(offs, str, a);
                        }
                    }
                });
                jtfCardNo.setText("");
                jtfCardHolder.setText("");
                jtfExpiryDate.setText("");
                jtfCardType.setText("Visa");
                jtfBank.setText("");
                jtfPaymentAmount.setText(jtfTotalAmount.getText());
                jtfPaymentAmount.setEditable(false);
                jtfBalance.setEditable(false);
                jtfCardNo.setEditable(true);
                jtfCardHolder.setEditable(true);
                jtfExpiryDate.setEditable(true);
                jtfCardType.setEditable(false);
                bankList.setEnabled(true);
                bankList.setSelectedIndex(0);

            } else if (e.getSource() == cashButton) {

                jtfCardNo.setEditable(false);
                jtfCardNo.setText("No available");
                jtfCardHolder.setEditable(false);
                jtfCardHolder.setText("No available");
                jtfExpiryDate.setText("No available");
                jtfExpiryDate.setEditable(false);
                jtfCardType.setEditable(false);
                jtfCardType.setText("No available");
                jtfBank.setEditable(false);
                jtfBank.setText("No available");
                jtfPaymentAmount.setText("");
                jtfPaymentAmount.setEditable(true);
                bankList.setEnabled(false);
                visaButton.setEnabled(false);
                masterButton.setEnabled(false);

            }
        }
    }

    private class CardType implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == visaButton) {
                visaButton.setEnabled(true);
                String card_type = cardGroup.getSelection().getActionCommand();
                jtfCardType.setText(card_type);
                jtfCardType.setEditable(false);

            } else if (e.getSource() == masterButton) {
                masterButton.setEnabled(true);
                String card_type = cardGroup.getSelection().getActionCommand();
                jtfCardType.setText(card_type);
                jtfCardType.setEditable(false);

            }
        }
    }

    public void onUpdate() {

        if (jtfPaymentAmount.getText().equals("")) {
            jtfBalance.setText("");
        } else if (jtfPaymentAmount.getText().matches("[0-9]+\\.[0-9]{2}")) {
            double balance = Double.parseDouble(jtfPaymentAmount.getText()) - Double.parseDouble(jtfTotalAmount.getText());
            if (balance < 0) {
                jtfBalance.setText("" + balance);
            } else if (balance >= 0) {
                DecimalFormat patternFormatter = new DecimalFormat("#, ###.00");
                jtfBalance.setText("" + patternFormatter.format(balance));
            }
        } else if (jtfPaymentAmount.getText().matches("[.]")) {
            JOptionPane.showMessageDialog(null, "Cannot start with dot.", "Warning", JOptionPane.WARNING_MESSAGE);
            clearPanel();
            createTable();
        } else if (jtfPaymentAmount.getText().matches("[a-zA-Z_%+-]")) {
            JOptionPane.showMessageDialog(null, "Payment cannot be alphabet.", "Warning", JOptionPane.WARNING_MESSAGE);
            clearPanel();
            createTable();
        } else if (!jtfPaymentAmount.getText().equals("")) {

            double balance = Double.parseDouble(jtfPaymentAmount.getText()) - Double.parseDouble(jtfTotalAmount.getText());
            if (balance < 0) {
                jtfBalance.setText("" + balance);
            } else if (balance >= 0) {
                DecimalFormat patternFormatter = new DecimalFormat("#, ###.00");
                jtfBalance.setText("" + patternFormatter.format(balance));
            }

        }
    }

}
