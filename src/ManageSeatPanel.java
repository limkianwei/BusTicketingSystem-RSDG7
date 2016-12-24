
import control.MaintainSeat;
import control.MaintainBus;
import control.MaintainSchedule;
import domain.Seat;
import domain.Bus;
import domain.Schedule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;

public class ManageSeatPanel extends JPanel {
    private MaintainBus busControl;
    private MaintainSeat seatControl;
    private MaintainSchedule scheduleControl;
    //---------------------------Table panels-------------------------------------
    private JPanel p1, tablePanel, buttonPanel;
    //---------------------------Table Objects----------------------------------
    private JScrollPane tableScrollPane;
    private TableRowSorter<TableModel> sorter;
    private JTable recordTable;
    private ListSelectionModel selectedList;
    private Object[] colHead = {"Seat id", "Seat Number", "Seat Status", "Bus Plate No", "Schedule ID"};
    //--------------------------------Option buttons--------------------------------
    private JButton jbtUpdate, jbtDelete;
    //-------------------------------CreateForm() objects--------------------------
    private JPanel datePanel;
    private JTextField jtfSeatId, jtfBusPlateNo;
    private JComboBox statusList, seatList, busList, scheduleList;
    private JButton jbtSubmit, jbtCancel;

    //------------------- state objects in form-----------------------
    private String seatStatus[] = {"Available", "Unavailable"};
    private String seatNumber[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40"};
    //---------------------------MVC objects----------------------------------
    private DefaultTableModel tableModel;
    private DefaultComboBoxModel statusModel = new DefaultComboBoxModel(seatStatus);
    private DefaultComboBoxModel seatModel = new DefaultComboBoxModel(seatNumber);
    private DefaultComboBoxModel busModel = new DefaultComboBoxModel();
    private DefaultComboBoxModel scheduleModel = new DefaultComboBoxModel(); 

    public ManageSeatPanel() {
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        seatControl = new MaintainSeat();
        busControl = new MaintainBus();
        scheduleControl = new MaintainSchedule();
        initializeBusModel();
        initializeScheduleModel();
        createTable();
    }

    public void createTable() {
        int vertical = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int horizontal = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

        tableModel = new DefaultTableModel(seatControl.getAllRecord(), colHead);
        recordTable = new JTable(tableModel) {
            //To disbale table editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            //To let column width auto adjust
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                // +5 to let some space and better read
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width + 150, tableColumn.getPreferredWidth()));
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
        recordTable.setBackground(Color.green);

        //Table will show action when list selected
        selectedList = recordTable.getSelectionModel();
        selectedList.addListSelectionListener(new ListSelectedListener());
        recordTable.setSelectionModel(selectedList);

        tableScrollPane = new JScrollPane(recordTable, vertical, horizontal);
        tablePanel = new JPanel(new GridLayout(1, 1));
        tablePanel.add(tableScrollPane);


        p1 = new JPanel(new BorderLayout());
        p1.add(tablePanel, BorderLayout.CENTER);

        jbtUpdate = new JButton("Update seat information");
        jbtDelete = new JButton("Delete current seat");
//        jbtAuto = new JButton("Auto Create Seat");

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(jbtUpdate);
        buttonPanel.add(Box.createVerticalStrut(15));

//        jbtAuto.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                clearPanel();
//               add(autoForm(1));
//            }
//        });

        jbtUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPanel();
                add(createForm(2));
                setSeatForm();
            }
        });
        jbtDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSeat();
            }
        });

        //adding space between tablePanel and JTabbedPane
        add(Box.createVerticalStrut(20));
        add(p1, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

//    public JPanel autoForm(final int which) {
//        jtfBusPlateNo = new JTextField();
//        jtfBusId = new JTextField();
//        jtfSeatId = new JTextField();
//        jtfSeatStatus = new JTextField();
//        jtfSeatNo = new JTextField();
//        jbtSubmit = new JButton("Submit");
//        jbtCancel = new JButton("Cancel");
//        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//
//        //Setup JTextField limit input
//        formPanel.add(new JLabel("Bus Plate No"));
//        formPanel.add(busList = new JComboBox(busModel));
//        
//        formButtonPanel.add(jbtSubmit);
//        formButtonPanel.add(jbtCancel);
//
//        
//        jbtSubmit.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Bus bus = busControl.getRecord(busList.getSelectedItem().toString());
//
//                if (bus.getTotal_seat().equals("20")) {
//                    for (int number = 1; number <= 20; ++number) {
//                        jtfSeatId.setText(generateSeatID());
//                        jtfSeatNo.setText("" + number);
//                        jtfSeatStatus.setText("Available");
//                        jtfBusId.setText(busList.getSelectedItem().toString());
//                        Bus busno = busControl.getRecord(jtfBusId.getText());
//                        Seat newSeat = new Seat(jtfSeatId.getText(), jtfSeatNo.getText(), jtfSeatStatus.getText(), busno);
//                        seatControl.addRecord(newSeat);
//                    }
//                } else if(bus.getTotal_seat().equals("40")) {
//                    for (int number = 1; number <= 40; ++number) {
//                        jtfSeatId.setText(generateSeatID());
//                        jtfSeatNo.setText("" + number);
//                        jtfSeatStatus.setText("Available");
//                        jtfBusId.setText(busList.getSelectedItem().toString());
//                        Bus busno = busControl.getRecord(jtfBusId.getText());
//                        Seat newSeat = new Seat(jtfSeatId.getText(), jtfSeatNo.getText(), jtfSeatStatus.getText(), busno);
//                        seatControl.addRecord(newSeat);
//                    }
//                }
//                JOptionPane.showMessageDialog(null, ""+jtfBusId.getText()+" add "+ bus.getTotal_seat() +" seat successfully.",
//                            "Success", JOptionPane.INFORMATION_MESSAGE);
//                clearPanel();
//                createTable();
//            }
//        });
//
//        jbtCancel.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                clearPanel();
//                createTable();
//            }
//        });
//
////        add(Box.createVerticalStrut(10), BorderLayout.NORTH);
//        add(formPanel, BorderLayout.NORTH);
//        add(formButtonPanel, BorderLayout.CENTER);
//
//        return new JPanel();
//    }

    public JPanel createForm(final int which) {
        jtfSeatId = new JTextField();
        jtfBusPlateNo = new JTextField();
        jbtSubmit = new JButton("Submit");
        jbtCancel = new JButton("Cancel");

        JPanel formPanel = new JPanel(new GridLayout(18, 4, 5, 5));
        datePanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        //Setup JTextField limit input
        jtfSeatId.setText(generateSeatID());
        jtfSeatId.setEditable(false);

        jtfBusPlateNo.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (getLength() + str.length() <= 5) {
                    super.insertString(offs, str, a);
                }
            }
        });

        if (which == 1) {
            formPanel.setBackground(Color.CYAN);
            formPanel.setBorder(BorderFactory.createTitledBorder(null, "Create Seat",
                    TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
        } else if (which == 2) {
            formPanel.setBackground(Color.PINK);
            formPanel.setBorder(BorderFactory.createTitledBorder(null, "Update Seat",
                    TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
        }
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JLabel("Seat Id"));
        formPanel.add(jtfSeatId);
        formPanel.add(new JLabel("Seat Number"));
        formPanel.add(seatList = new JComboBox(seatModel));
        formPanel.add(new JLabel("Seat Status"));
        formPanel.add(statusList = new JComboBox(statusModel));
        formPanel.add(new JLabel("Bus Plate No"));
        formPanel.add(busList = new JComboBox(busModel));
        formPanel.add(new JLabel("Schedule Id"));
        formPanel.add(scheduleList = new JComboBox(scheduleModel));
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));

        statusList.setEnabled(false);

        //Set to default
        statusList.setSelectedIndex(0);
        seatList.setSelectedIndex(0);
        formButtonPanel.add(jbtSubmit);
        formButtonPanel.add(jbtCancel);

        jbtSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (which == 1) {
                    addSeat();
                } else if (which == 2) {
                    updateSeat();
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

        return new JPanel();
    }

    public void setSeatForm() {
       int selectedRowIndex = recordTable.getSelectedRow();
        Object selectedObject = (Object) recordTable.getModel().getValueAt(
                recordTable.convertRowIndexToModel(selectedRowIndex), 0);
        String seat_id = selectedObject.toString();
        Seat seat = seatControl.getRecord(seat_id);
        if (seat != null) {
            jtfSeatId.setText(seat.getId());
            jtfSeatId.setEditable(false);
            jtfBusPlateNo.setText(seat.getBus().getPlate_no());
            jtfBusPlateNo.setEditable(false);
            busList.setSelectedItem(seat.getBus().getPlate_no());
            busList.setEnabled(false);
            statusList.setEnabled(true);
            statusList.setSelectedItem(seat.getStatus());
            seatList.setEnabled(false);
            seatList.setSelectedItem(seat.getNo());
            scheduleList.setEnabled(false);
            scheduleList.setSelectedItem(seat.getSchedule().getId());
        }
    }

    public boolean checkTextField() {
        boolean valid = true;
        if (jtfSeatId.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Seat Id cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfSeatId.requestFocusInWindow();
            valid = false;
        }
        return valid;
    }

    public void addSeat() {
       Seat seat = seatControl.getRecord(jtfSeatId.getText());
        if (seat == null) {
            if (checkTextField()) {
                String status = statusList.getSelectedItem().toString();
                String lseat = seatList.getSelectedItem().toString();
                String bus = busList.getSelectedItem().toString();
                String schedule = scheduleList.getSelectedItem().toString();
                try {
                    Bus busno = busControl.getRecord(bus);
                    Schedule scheduleId = scheduleControl.getRecord(schedule);
                    Seat newSeat = new Seat(jtfSeatId.getText(), lseat, status, busno, scheduleId);
                    seatControl.addRecord(newSeat);
                    JOptionPane.showMessageDialog(null, "New Seat added successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearPanel();
                    createTable();
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(null, "The bus plate number is not exist.",
                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                }

            }

        } else {
            JOptionPane.showMessageDialog(null, "Seat already exist by according to Seat Id you entered.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateSeat() {
      if (checkTextField()) {
            String status = statusList.getSelectedItem().toString();
            String lseat = seatList.getSelectedItem().toString();
            String bus = busList.getSelectedItem().toString();
            String schedule = scheduleList.getSelectedItem().toString();
            try {
                Bus busno = busControl.getRecord(bus);
                Schedule scheduleId = scheduleControl.getRecord(schedule);
                    Seat newSeat = new Seat(jtfSeatId.getText(), lseat, status, busno, scheduleId);
                seatControl.updateRecord(newSeat);
                JOptionPane.showMessageDialog(null, "Seat updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearPanel();
                createTable();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "The data cannot be null.",
                        "Warning", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void deleteSeat() {
       int selectedRowIndex = recordTable.getSelectedRow();
        Object selectedObject = (Object) recordTable.getModel().getValueAt(
                recordTable.convertRowIndexToModel(selectedRowIndex), 0);
        String seatId = selectedObject.toString();
        Seat seat = seatControl.getRecord(seatId);
        if (seat != null) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Confirm delete " + seat.getId() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                seatControl.deleteRecord(seat);
                JOptionPane.showMessageDialog(null, seat.getId() + " is deleted.",
                        "Successful", JOptionPane.INFORMATION_MESSAGE);
                clearPanel();
                createTable();
            }
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
                }
            }
            //Get action
            if (btnCount != 3) {
                buttonPanel.add(jbtDelete);
            }
        }
    }

    private void initializeBusModel() {
        
    }
    
    private void initializeScheduleModel() {
        
    }
    
    

    public String generateSeatID() {
        String seatId = seatControl.generateSeatID();
        int seatNo = Integer.parseInt(seatId.substring(1, 5));
        String finalGeneratedSeatID = String.format("E%04d", (seatNo + 1));
        return finalGeneratedSeatID;
    }

//    public String generateSeatNo() {
//        String seatNo = seatControl.generateSeatNo();
//        int seat = Integer.parseInt(seatNo.substring(1, 2));
//
//        if (seat <= 40) {
//            String finalGenerateSeatNo = String.format("%02d", (seat + 1));
//
//        }
//        return finalGenerateSeatNo;
//        else{
//            
//        }
//        return finalGenerateSeatNo;
//    }
}
