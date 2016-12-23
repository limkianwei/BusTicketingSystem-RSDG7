
import control.MaintainStaff;
import control.MaintainBus;
import control.MaintainSeat;
import control.MaintainSchedule;
import control.MaintainTransaction;
import domain.Staff;
import domain.Bus;
import domain.Seat;
import domain.Schedule;
import domain.Transaction;
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
import java.lang.*;

public class ManageTransactionPanel extends JPanel {

    private MaintainStaff staffControl;
    private MaintainBus busControl;
    private MaintainSeat seatControl;
    private MaintainSchedule scheduleControl;
    private MaintainTransaction transactionControl;
    //---------------------------Table panels-------------------------------------
    private JPanel p1, tablePanel, buttonPanel;
    //---------------------------Table Objects----------------------------------
    private JScrollPane tableScrollPane;
    private TableRowSorter<TableModel> sorter;
    private JTable recordTable;
    private ListSelectionModel selectedList;
    private Object[] colHead = {"Schedule id", "Departure Date", "Departure Time", "Departure Place", "Destination", "Price (RM)", "No of available seat", "Bus Plate No", "Seat Availability"};
    //--------------------------------Option buttons--------------------------------
    private JButton jbtCreate;
    //-------------------------------CreateForm() objects--------------------------
    private JPanel datePanel;
    private JTextField jtfScheduleId, jtfTransactionId, jtfTransactionDate, jtfSeat0, jtfSeat1, jtfSeat2, jtfSeat3, jtfBusId, jtfSeatStatus0, jtfSeatStatus1, jtfSeatStatus2, jtfSeatStatus3, jtfBookingStatus, jtfPaymentStatus;
    SpinnerNumberModel qtyModel = new SpinnerNumberModel(0, 0, 4, 1);
    private JSpinner jsSeatPurchase = new JSpinner(qtyModel);
    private JComboBox destinationList, placeList, timeList, busList, seatList, seat1List, seat2List, seat3List;
    private JButton jbtSubmit, jbtCancel;
    //---------------------------Date objects in form-----------------------------
    private ArrayList<String> calYear = new ArrayList<String>();
    private ArrayList<String> calMonth = new ArrayList<String>();
    private ArrayList<String> calDay = new ArrayList<String>();
    private JComboBox yearList, monthList, dayList;
    //------------------- state objects in form-----------------------
    private String destination[] = {"Pudu Sentral", "Seremban", "Ipoh", "Kuala Lumpur"};
    private String departurePlace[] = {"Seremban", "Pudu Sentral", "Ipoh", "Kuala Lumpur"};
    private String departureTime[] = {"8:00:00", "9:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00"};
    //---------------------------MVC objects----------------------------------
    private DefaultTableModel tableModel;
    private DefaultComboBoxModel destinationModel = new DefaultComboBoxModel(destination);
    private DefaultComboBoxModel departurePlaceModel = new DefaultComboBoxModel(departurePlace);
    private DefaultComboBoxModel departureTimeModel = new DefaultComboBoxModel(departureTime);
    private DefaultComboBoxModel seatModel = new DefaultComboBoxModel();
    private DefaultComboBoxModel seat1Model = new DefaultComboBoxModel();
    private DefaultComboBoxModel seat2Model = new DefaultComboBoxModel();
    private DefaultComboBoxModel seat3Model = new DefaultComboBoxModel();
    private ArrayList<Seat> seatArr;

    public ManageTransactionPanel() {
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        scheduleControl = new MaintainSchedule();
        busControl = new MaintainBus();
        seatControl = new MaintainSeat();
        transactionControl = new MaintainTransaction();
        staffControl = new MaintainStaff();
        createTable();
    }

    public void createTable() {
        int vertical = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int horizontal = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

        tableModel = new DefaultTableModel(scheduleControl.getAllRecord(), colHead);
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
        recordTable.setBackground(Color.getHSBColor(83, 45, 83));

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

        jbtCreate = new JButton("Create new transaction");

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(jbtCreate);
        jbtCreate.setEnabled(false);
        buttonPanel.add(Box.createVerticalStrut(15));
        jbtCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = recordTable.getSelectedRow();
                Object selectedObject = (Object) recordTable.getModel().getValueAt(
                        recordTable.convertRowIndexToModel(selectedRowIndex), 0);
                String schedule_id = selectedObject.toString();
                Schedule schedule = scheduleControl.getRecord(schedule_id);
                Date departureDate = schedule.getDepartureDate();
                int availableSeat = schedule.getAvailableSeat();

                DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String todayD = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date().getTime());
                Date todayDate = null;
                try {
                    todayDate = dateFormat3.parse(todayD);
                    if (departureDate.before(todayDate)) {
                        JOptionPane.showMessageDialog(null, "You cannot make this transaction because out of date.",
                                "Failed", JOptionPane.INFORMATION_MESSAGE);
                        clearPanel();
                        createTable();

                    } else if (availableSeat == 0 || availableSeat < 0) {
                        JOptionPane.showMessageDialog(null, "You cannot make this transaction because Not have available seat.",
                                "Failed", JOptionPane.INFORMATION_MESSAGE);
                        clearPanel();
                        createTable();

                    } else {
                        clearPanel();
                        add(createForm(1));
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(ManageTransactionPanel.class.getName()).log(Level.SEVERE, null, ex);
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
        String schedule_id = selectedObject.toString();
        Schedule schedule = scheduleControl.getRecord(schedule_id);
        String bus = schedule.getBus().getPlate_no();

        seatModel.removeAllElements();
        seat1Model.removeAllElements();
        seat2Model.removeAllElements();
        seat3Model.removeAllElements();

        seatArr = seatControl.getAllSeat(schedule_id, bus);
        for (int i = 0; i < seatArr.size(); ++i) {

            seatModel.addElement(seatArr.get(i).getId());
            seat1Model.addElement(seatArr.get(i).getId());
            seat2Model.addElement(seatArr.get(i).getId());
            seat3Model.addElement(seatArr.get(i).getId());
        }
        if (schedule != null) {

            jtfBusId = new JTextField();
            jtfBusId.setText(schedule.getBus().getPlate_no());

            jtfTransactionId = new JTextField();
            jtfTransactionDate = new JTextField();
            jtfSeat0 = new JTextField();
            jtfSeat1 = new JTextField();
            jtfSeat2 = new JTextField();
            jtfSeat3 = new JTextField();
            jtfScheduleId = new JTextField();
            jtfSeatStatus0 = new JTextField();
            jtfSeatStatus1 = new JTextField();
            jtfSeatStatus2 = new JTextField();
            jtfSeatStatus3 = new JTextField();
            jtfBookingStatus = new JTextField();
            jtfPaymentStatus = new JTextField();

            jbtSubmit = new JButton("Submit");
            jbtCancel = new JButton("Cancel");

            JPanel formPanel = new JPanel(new GridLayout(18, 4, 5, 5));
            JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            if (which == 1) {
                formPanel.setBackground(Color.CYAN);
                formPanel.setBorder(BorderFactory.createTitledBorder(null, "Create Transaction",
                        TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
            } else if (which == 2) {
                formPanel.setBackground(Color.PINK);
                formPanel.setBorder(BorderFactory.createTitledBorder(null, "Update Transaction",
                        TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
            }

            formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
            formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
            formPanel.add(new JLabel("Transaction Id"));
            formPanel.add(jtfTransactionId);
            formPanel.add(new JLabel("Transaction Date"));
            formPanel.add(jtfTransactionDate);
            formPanel.add(new JLabel("Number of Seat Purchase"));
            formPanel.add(jsSeatPurchase);
            formPanel.add(new JLabel("First Seat"));
            formPanel.add(seatList = new JComboBox(seatModel));
            formPanel.add(new JLabel("Second Seat"));
            formPanel.add(seat1List = new JComboBox(seat1Model));
            formPanel.add(new JLabel("Third Seat"));
            formPanel.add(seat2List = new JComboBox(seat2Model));
            formPanel.add(new JLabel("Forth Seat"));
            formPanel.add(seat3List = new JComboBox(seat3Model));
            formPanel.add(new JLabel("Schedule ID"));
            formPanel.add(jtfScheduleId);
            formPanel.add(new JLabel("Bus ID"));
            formPanel.add(jtfBusId);
            formPanel.add(new JLabel("Booking Status"));
            formPanel.add(jtfBookingStatus);
            formPanel.add(new JLabel("Payment Status"));
            formPanel.add(jtfPaymentStatus);
            formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
            formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));

            seatList.setEnabled(false);
            seat1List.setEnabled(false);
            seat2List.setEnabled(false);
            seat3List.setEnabled(false);

            jsSeatPurchase.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (jsSeatPurchase.getValue().toString().equals("1")) {
                        seatList.setEnabled(true);
                        seat1List.setEnabled(false);
                        seat1List.setToolTipText("No available");
                        seat2List.setEnabled(false);
                        seat2List.setToolTipText("No available");
                        seat3List.setEnabled(false);
                        seat3List.setToolTipText("No available");
                    } else if (jsSeatPurchase.getValue().toString().equals("2")) {
                        seatList.setEnabled(true);
                        seat1List.setEnabled(true);
                        seat2List.setEnabled(false);
                        seat2List.setToolTipText("No available");
                        seat3List.setEnabled(false);
                        seat3List.setToolTipText("No available");
                    } else if (jsSeatPurchase.getValue().toString().equals("3")) {
                        seatList.setEnabled(true);
                        seat1List.setEnabled(true);
                        seat2List.setEnabled(true);
                        seat3List.setEnabled(false);
                        seat3List.setToolTipText("No available");
                    } else if (jsSeatPurchase.getValue().toString().equals("4")) {
                        seatList.setEnabled(true);
                        seat1List.setEnabled(true);
                        seat2List.setEnabled(true);
                        seat3List.setEnabled(true);
                    } else if (jsSeatPurchase.getValue().toString().equals("0")) {
                        seatList.setEnabled(false);
                        seatList.setToolTipText("No available");
                        seat1List.setEnabled(false);
                        seat1List.setToolTipText("No available");
                        seat2List.setEnabled(false);
                        seat2List.setToolTipText("No available");
                        seat3List.setEnabled(false);
                        seat3List.setToolTipText("No available");
                    }
                }
            });

            jtfScheduleId.setText(schedule_id);
            jtfScheduleId.setEditable(false);
            jtfTransactionId.setText(generateTransactionID());
            jtfTransactionId.setEditable(false);
            jtfTransactionDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date().getTime()));
            jtfTransactionDate.setEditable(false);
            jtfBookingStatus.setText("InProcess");
            jtfPaymentStatus.setText("HaventPay");
            jtfBusId.setEditable(false);
            jtfPaymentStatus.setEditable(false);
            formButtonPanel.add(jbtSubmit);
            formButtonPanel.add(jbtCancel);

            jbtSubmit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (which == 1) {
                        addTransaction();
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
        String s0 = seatList.getSelectedItem().toString();
        String s1 = null;
        String s2 = null;
        String s3 = null;
        if (jsSeatPurchase.getValue().toString().equals("2")) {
            s1 = seat1List.getSelectedItem().toString();
            if (s1.equals(s0)) {
                JOptionPane.showMessageDialog(null, "The seat number cannot be same.", "Warning", JOptionPane.WARNING_MESSAGE);
                seatList.requestFocusInWindow();
                valid = false;
            }
        } else if (jsSeatPurchase.getValue().toString().equals("3")) {
            s1 = seat1List.getSelectedItem().toString();
            s2 = seat2List.getSelectedItem().toString();
            if (s1.equals(s2) || s1.equals(s0) || s0.equals(s2)) {
                JOptionPane.showMessageDialog(null, "The seat number cannot be same.", "Warning", JOptionPane.WARNING_MESSAGE);
                seatList.requestFocusInWindow();
                valid = false;
            }
        } else if (jsSeatPurchase.getValue().toString().equals("4")) {
            s1 = seat1List.getSelectedItem().toString();
            s2 = seat2List.getSelectedItem().toString();
            s3 = seat3List.getSelectedItem().toString();
            if (s0.equals(s1) || s0.equals(s2) || s0.equals(s3) || s1.equals(s2) || s1.equals(s3) || s2.equals(s3)) {
                JOptionPane.showMessageDialog(null, "The seat number cannot be same.", "Warning", JOptionPane.WARNING_MESSAGE);
                seatList.requestFocusInWindow();
                valid = false;
            }
        }

        if (jtfTransactionId.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Transaction Id cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfScheduleId.requestFocusInWindow();
            valid = false;
        } else if (jsSeatPurchase.getValue().toString().equals("0")) {
            JOptionPane.showMessageDialog(null, "The number of seat cannot be 0.", "Warning", JOptionPane.WARNING_MESSAGE);
            jsSeatPurchase.requestFocusInWindow();
            valid = false;
        }

        return valid;
    }

    public void addTransaction() {
        Transaction transaction = transactionControl.getRecord(jtfTransactionId.getText());
        if (transaction == null) {
            if (checkTextField()) {
                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
                DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String seat0 = null;
                String seat1 = null;
                String seat2 = null;
                String seat3 = null;
                Object seatPurchase = jsSeatPurchase.getValue();
                String qty = seatPurchase.toString();
                int qt = Integer.parseInt(qty);
                Date transactionDate = null;
                String seatStatus0 = null;
                String seatStatus1 = null;
                String seatStatus2 = null;
                String seatStatus3 = null;

                try {
                    Schedule schedule = scheduleControl.getRecord(jtfScheduleId.getText());
                    transactionDate = dateFormat2.parse(jtfTransactionDate.getText());
                    Date date = schedule.getDepartureDate();
                    Date time = schedule.getDepartureTime();
                    String place = schedule.getDeparturePlace();
                    String destination = schedule.getDestination();
                    Double price = schedule.getPrice();
                    String bus = schedule.getBus().getPlate_no();
                    Bus busno = busControl.getRecord(bus);
                    String seatAvailability = schedule.getBus_seat_availability();

                    Integer availableSeat = schedule.getAvailableSeat();
                    Integer updatedSeat = null;
                    if (jsSeatPurchase.getValue().toString().equals("1")) {
                        seat0 = seatList.getSelectedItem().toString();
                        seat1 = seat1List.getToolTipText().toString();
                        seat2 = seat2List.getToolTipText().toString();
                        seat3 = seat3List.getToolTipText().toString();
                    } else if (jsSeatPurchase.getValue().toString().equals("2")) {
                        seat0 = seatList.getSelectedItem().toString();
                        seat1 = seat1List.getSelectedItem().toString();
                        seat2 = seat2List.getToolTipText().toString();
                        seat3 = seat3List.getToolTipText().toString();
                    } else if (jsSeatPurchase.getValue().toString().equals("3")) {
                        seat0 = seatList.getSelectedItem().toString();
                        seat1 = seat1List.getSelectedItem().toString();
                        seat2 = seat2List.getSelectedItem().toString();
                        seat3 = seat3List.getToolTipText().toString();
                    } else if (jsSeatPurchase.getValue().toString().equals("4")) {
                        seat0 = seatList.getSelectedItem().toString();
                        seat1 = seat1List.getSelectedItem().toString();
                        seat2 = seat2List.getSelectedItem().toString();
                        seat3 = seat3List.getSelectedItem().toString();
                    }

                    updatedSeat = availableSeat - qt;
                    if (availableSeat <= 0 || updatedSeat < 0) {
                        JOptionPane.showMessageDialog(null, "The current schedule does not have more available seat.",
                                "Warning", JOptionPane.INFORMATION_MESSAGE);
                        clearPanel();
                        createTable();

                    } else {
                        schedule.setAvailableSeat(updatedSeat);
                        Transaction newTransaction = new Transaction(jtfTransactionId.getText(), transactionDate, qt, seat0, seat1, seat2, seat3, jtfBookingStatus.getText(), jtfPaymentStatus.getText(), schedule);
                        transactionControl.addRecord(newTransaction);
                        Schedule newSchedule = new Schedule(jtfScheduleId.getText(), date, time, place, destination, price, updatedSeat, busno, seatAvailability);
                        scheduleControl.updateRecord(newSchedule);

                        if (jsSeatPurchase.getValue().toString().equals("1")) {
                            seat0 = seatList.getSelectedItem().toString();
                            seat1 = seat1List.getToolTipText().toString();
                            seat2 = seat2List.getToolTipText().toString();
                            seat3 = seat3List.getToolTipText().toString();

                            Seat tseat0 = seatControl.getRecord(seat0);
                            String seatId0 = tseat0.getId();
                            String seatNo0 = tseat0.getNo();
                            jtfSeatStatus0.setText("Unavailable");
                            seatStatus0 = jtfSeatStatus0.getText();

                            Seat newSeat = new Seat(seatId0, seatNo0, seatStatus0, busno, schedule);
                            seatControl.updateRecord(newSeat);

                        } else if (jsSeatPurchase.getValue().toString().equals("2")) {
                            seat0 = seatList.getSelectedItem().toString();
                            seat1 = seat1List.getSelectedItem().toString();
                            seat2 = seat2List.getToolTipText().toString();
                            seat3 = seat3List.getToolTipText().toString();

                            Seat tseat0 = seatControl.getRecord(seat0);
                            String seatId0 = tseat0.getId();
                            String seatNo0 = tseat0.getNo();
                            jtfSeatStatus0.setText("Unavailable");
                            seatStatus0 = jtfSeatStatus0.getText();

                            Seat newSeat = new Seat(seatId0, seatNo0, seatStatus0, busno, schedule);
                            seatControl.updateRecord(newSeat);

                            Seat tseat1 = seatControl.getRecord(seat1);
                            String seatId1 = tseat1.getId();
                            String seatNo1 = tseat1.getNo();
                            jtfSeatStatus1.setText("Unavailable");
                            seatStatus1 = jtfSeatStatus1.getText();

                            Seat newSeat1 = new Seat(seatId1, seatNo1, seatStatus1, busno, schedule);
                            seatControl.updateRecord1(newSeat1);

                        } else if (jsSeatPurchase.getValue().toString().equals("3")) {
                            seat0 = seatList.getSelectedItem().toString();
                            seat1 = seat1List.getSelectedItem().toString();
                            seat2 = seat2List.getSelectedItem().toString();
                            seat3 = seat3List.getToolTipText().toString();

                            Seat tseat0 = seatControl.getRecord(seat0);
                            String seatId0 = tseat0.getId();
                            String seatNo0 = tseat0.getNo();
                            jtfSeatStatus0.setText("Unavailable");
                            seatStatus0 = jtfSeatStatus0.getText();

                            Seat newSeat = new Seat(seatId0, seatNo0, seatStatus0, busno, schedule);
                            seatControl.updateRecord(newSeat);

                            Seat tseat1 = seatControl.getRecord(seat1);
                            String seatId1 = tseat1.getId();
                            String seatNo1 = tseat1.getNo();
                            jtfSeatStatus1.setText("Unavailable");
                            seatStatus1 = jtfSeatStatus1.getText();

                            Seat newSeat1 = new Seat(seatId1, seatNo1, seatStatus1, busno, schedule);
                            seatControl.updateRecord1(newSeat1);

                            Seat tseat2 = seatControl.getRecord(seat2);
                            String seatId2 = tseat2.getId();
                            String seatNo2 = tseat2.getNo();
                            jtfSeatStatus2.setText("Unavailable");
                            seatStatus2 = jtfSeatStatus2.getText();

                            Seat newSeat2 = new Seat(seatId2, seatNo2, seatStatus2, busno, schedule);
                            seatControl.updateRecord2(newSeat2);

                        } else if (jsSeatPurchase.getValue().toString().equals("4")) {
                            seat0 = seatList.getSelectedItem().toString();
                            seat1 = seat1List.getSelectedItem().toString();
                            seat2 = seat2List.getSelectedItem().toString();
                            seat3 = seat3List.getSelectedItem().toString();

                            Seat tseat0 = seatControl.getRecord(seat0);
                            String seatId0 = tseat0.getId();
                            String seatNo0 = tseat0.getNo();
                            jtfSeatStatus0.setText("Unavailable");
                            seatStatus0 = jtfSeatStatus0.getText();

                            Seat newSeat = new Seat(seatId0, seatNo0, seatStatus0, busno, schedule);
                            seatControl.updateRecord(newSeat);

                            Seat tseat1 = seatControl.getRecord(seat1);
                            String seatId1 = tseat1.getId();
                            String seatNo1 = tseat1.getNo();
                            jtfSeatStatus1.setText("Unavailable");
                            seatStatus1 = jtfSeatStatus1.getText();

                            Seat newSeat1 = new Seat(seatId1, seatNo1, seatStatus1, busno, schedule);
                            seatControl.updateRecord1(newSeat1);

                            Seat tseat2 = seatControl.getRecord(seat2);
                            String seatId2 = tseat2.getId();
                            String seatNo2 = tseat2.getNo();
                            jtfSeatStatus2.setText("Unavailable");
                            seatStatus2 = jtfSeatStatus2.getText();

                            Seat newSeat2 = new Seat(seatId2, seatNo2, seatStatus2, busno, schedule);
                            seatControl.updateRecord2(newSeat2);

                            Seat tseat3 = seatControl.getRecord(seat3);
                            String seatId3 = tseat3.getId();
                            String seatNo3 = tseat3.getNo();
                            jtfSeatStatus3.setText("Unavailable");
                            seatStatus3 = jtfSeatStatus3.getText();

                            Seat newSeat3 = new Seat(seatId3, seatNo3, seatStatus3, busno, schedule);
                            seatControl.updateRecord3(newSeat3);
                        }

                        JOptionPane.showMessageDialog(null, "New transaction added successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        new Main().setVisible(true);
                        clearPanel();
                        createTable();
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(ManageStaffPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Transaction already exist by according to Transaction ID you entered.",
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

    public String generateTransactionID() {
        String transaction = transactionControl.generateTransactionID();
        int transactionNo = Integer.parseInt(transaction.substring(2, 5));
        String finalGeneratedTransactionID = String.format("T%04d", (transactionNo + 1));
        return finalGeneratedTransactionID;
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

}
