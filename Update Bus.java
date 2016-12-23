
import control.MaintainBus;
import domain.Bus;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;

public class ManageBusPanel extends JPanel {

    private MaintainBus busControl;
    //---------------------------Table panels-------------------------------------
    private JPanel p1, tablePanel, buttonPanel;
    //---------------------------Table Objects----------------------------------
    private JScrollPane tableScrollPane;
    private TableRowSorter<TableModel> sorter;
    private JTable recordTable;
    private ListSelectionModel selectedList;
    private Object[] colHead = {"Plate No", "Bus Type", "Status", "Model", "Total Seat", "Purchase Date", "Bus Number"};
    //--------------------------------Option buttons--------------------------------
    private JButton jbtCreate, jbtUpdate, jbtDelete;
    //-------------------------------CreateForm() objects--------------------------
    private JPanel datePanel;
    private JTextField jtfPlateNo, jtfModel, jtfTotalSeat, jtfPurchaseDate, jtfBusNumber;
    private JComboBox statusList, typeList;
    private JButton jbtSubmit, jbtCancel;
    //---------------------------Date objects in form-----------------------------
    private ArrayList<String> calYear = new ArrayList<String>();
    private ArrayList<String> calMonth = new ArrayList<String>();
    private ArrayList<String> calDay = new ArrayList<String>();
    private JComboBox yearList, monthList, dayList;
    //------------------- state objects in form-----------------------
    private String busStatus[] = {"Available", "Maintenance"};
    private String busType[] = {"Premium", "Economic"};
    //---------------------------MVC objects----------------------------------
    private DefaultTableModel tableModel;
    private DefaultComboBoxModel busModel = new DefaultComboBoxModel(busStatus);
    private DefaultComboBoxModel typeModel = new DefaultComboBoxModel(busType);
    private String host = "jdbc:derby://localhost:1527/kmb";
    private String user = "nbuser";
    private String password = "123";

    public ManageBusPanel() {
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        busControl = new MaintainBus();
        createTable();
    }

    public void createTable() {
        int vertical = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int horizontal = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

        tableModel = new DefaultTableModel(busControl.getAllRecord(), colHead);
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
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width + 75, tableColumn.getPreferredWidth()));
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
        recordTable.setBackground(Color.getHSBColor(305, 41, 80));

        //Table will show action when list selected
        selectedList = recordTable.getSelectionModel();
        selectedList.addListSelectionListener(new ListSelectedListener());
        recordTable.setSelectionModel(selectedList);

        tableScrollPane = new JScrollPane(recordTable, vertical, horizontal);
        tablePanel = new JPanel(new GridLayout(1, 1));
        tablePanel.add(tableScrollPane);

        p1 = new JPanel(new BorderLayout());
        p1.add(tablePanel, BorderLayout.CENTER);

        jbtCreate = new JButton("Create new bus");
        jbtUpdate = new JButton("Update bus information");
        jbtDelete = new JButton("Delete current bus");

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(jbtCreate);
        buttonPanel.add(Box.createVerticalStrut(15));

        jbtCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPanel();
                add(createForm(1));
            }
        });
        jbtUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPanel();
                add(createForm(2));
                setBusForm();
            }
        });
        jbtDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBus();
            }
        });

        //adding space between tablePanel and JTabbedPane
        add(Box.createVerticalStrut(20));
        add(p1, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel createForm(final int which) {
        jtfPlateNo = new JTextField();
        jtfModel = new JTextField();
        jtfTotalSeat = new JTextField();
        jtfPurchaseDate = new JTextField();
        jtfBusNumber = new JTextField();
        jbtSubmit = new JButton("Submit");
        jbtCancel = new JButton("Cancel");

        JPanel formPanel = new JPanel(new GridLayout(18, 4, 5, 5));
        datePanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        //Setup year
        Calendar prevYear = Calendar.getInstance();
        final int currentYear = new GregorianCalendar().get(Calendar.YEAR);
        for (int i = currentYear; i > (currentYear - 55); i = prevYear.get(Calendar.YEAR)) {
            prevYear.add(Calendar.YEAR, -1);
            if (prevYear.get(Calendar.YEAR) <= (currentYear - 18)) {
                calYear.add("" + prevYear.get(Calendar.YEAR));
            }
        }
        datePanel.add(yearList = new JComboBox(calYear.toArray()));

        //Setup months
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length - 1; i++) {
            calMonth.add(months[i]);
        }
        datePanel.add(monthList = new JComboBox(calMonth.toArray()));

        //Setup day in month
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(yearList.getSelectedItem().toString()));
        cal.set(Calendar.MONTH, monthList.getSelectedIndex());
        final int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < maxDay; i++) {
            String day = "" + (i + 1);
            if (maxDay < 10) {
                calDay.add("0" + day);
            } else {
                calDay.add(day);
            }
        }
        datePanel.add(dayList = new JComboBox(calDay.toArray()));
        yearList.addItemListener(new DayComboBoxListener());
        monthList.addItemListener(new DayComboBoxListener());

        //Setup JTextField limit input
        jtfPlateNo.setText(generateBusID());
        jtfPlateNo.setEditable(false);

        jtfModel.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (getLength() + str.length() <= 50) {
                    super.insertString(offs, str, a);
                }
            }
        });

        if (which == 1) {
            formPanel.setBackground(Color.CYAN);
            formPanel.setBorder(BorderFactory.createTitledBorder(null, "Create Bus",
                    TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
        } else if (which == 2) {
            formPanel.setBackground(Color.PINK);
            formPanel.setBorder(BorderFactory.createTitledBorder(null, "Update Bus",
                    TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
        }
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JLabel("Bus Plate No"));
        formPanel.add(jtfPlateNo);
        formPanel.add(new JLabel("Bus Type"));
        formPanel.add(typeList = new JComboBox(typeModel));
        formPanel.add(new JLabel("Bus Status"));
        formPanel.add(statusList = new JComboBox(busModel));
        formPanel.add(new JLabel("Bus Model"));
        formPanel.add(jtfModel);
        formPanel.add(new JLabel("Bus Total Seat"));
        formPanel.add(jtfTotalSeat);
        formPanel.add(new JLabel("Purchase Date"));
        formPanel.add(jtfPurchaseDate);
        formPanel.add(new JLabel("Bus Number"));
        formPanel.add(jtfBusNumber);
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));

        jtfTotalSeat.setEditable(false);
        typeList.setSelectedIndex(0);
        jtfTotalSeat.setText("20");
        typeList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (typeList.getSelectedItem().toString().equals("Economic")) {
                    jtfTotalSeat.setText("40");
                } else {
                    jtfTotalSeat.setText("20");
                }
            }
        });
        statusList.setEnabled(false);
        jtfPurchaseDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date().getTime()));
        jtfBusNumber.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (getLength() + str.length() <= 7) {
                    super.insertString(offs, str, a);
                }
            }
        });
        statusList.setSelectedIndex(0);

        formButtonPanel.add(jbtSubmit);
        formButtonPanel.add(jbtCancel);

        jbtSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (which == 1) {
                    addBus();
                } else if (which == 2) {
                    updateBus();
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

    public void setBusForm() {
        int selectedRowIndex = recordTable.getSelectedRow();
        Object selectedObject = (Object) recordTable.getModel().getValueAt(
                recordTable.convertRowIndexToModel(selectedRowIndex), 0);
        String plate_no = selectedObject.toString();
        Bus bus = busControl.getRecord(plate_no);
        if (bus != null) {
            jtfPlateNo.setText(bus.getPlate_no());
            jtfPlateNo.setEditable(false);
            typeList.setEnabled(false);
            jtfModel.setText(bus.getModel());
            statusList.setEnabled(true);
            statusList.setSelectedItem(bus.getStatus());
            jtfTotalSeat.setText(bus.getTotal_seat());
            jtfTotalSeat.setEditable(false);
            jtfBusNumber.setText(bus.getBus_number());
            if (bus.getType().equals("Economic")) {
                typeList.setSelectedItem("Economic");
            } else if (bus.getType().equals("Premium")) {
                typeList.setSelectedItem("Premium");
            }
            //Setup purchase date
            Date purchaseDate = bus.getDate_purchase();
            Calendar purchaseCal = Calendar.getInstance();
            purchaseCal.setTime(purchaseDate);
            jtfPurchaseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(purchaseCal.getTime()));
            typeList.setSelectedItem(bus.getType());
        }
    }

    public boolean checkTextField() {
        boolean valid = true;
        if (jtfPlateNo.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Bus Plate Number cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfPlateNo.requestFocusInWindow();
            valid = false;
        } else if (jtfModel.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Bus Model cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfModel.requestFocusInWindow();
            valid = false;
        } else if (jtfTotalSeat.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Bus Seat cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfTotalSeat.requestFocusInWindow();
            valid = false;
        } else if (jtfBusNumber.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Bus Number cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfBusNumber.requestFocusInWindow();
            valid = false;
        } else if (jtfTotalSeat.getText().equals("0")) {
            JOptionPane.showMessageDialog(null, "Total Seat cannot be 0.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfTotalSeat.requestFocusInWindow();
            valid = false;
        }
        return valid;
    }

    public void addBus() {
        Bus bus = busControl.getRecord(jtfPlateNo.getText());
        if (bus != null) {

            JOptionPane.showMessageDialog(null, "Bus already exist by according to Bus Plate No you entered.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            if (checkTextField()) {
                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
                DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String status = statusList.getSelectedItem().toString();
                String type = typeList.getSelectedItem().toString();
                String number = jtfBusNumber.getText();
                number = number.toUpperCase();
                Date purchaseDate = null;
                try {

                    Connection conn = DriverManager.getConnection(host, user, password);
                    Statement stmt = conn.createStatement();

                    PreparedStatement statement = null;
                    ResultSet rs = null;
                    String queryStr = "SELECT BUS_NUMBER FROM BUS WHERE BUS_NUMBER = '" + number + "'";
                    statement = conn.prepareStatement(queryStr);
                    rs = statement.executeQuery();

                    if (rs.next()) {
                        String plate = rs.getString("BUS_NUMBER");
                        if (plate.equalsIgnoreCase(number)) {
                            JOptionPane.showMessageDialog(null, number + " is already exists");
                        }
                    } else {
                        try {
                            purchaseDate = dateFormat2.parse(jtfPurchaseDate.getText());
                            Bus newBus = new Bus(jtfPlateNo.getText(), type, status,
                                    jtfModel.getText(), jtfTotalSeat.getText(), purchaseDate, number);
                            busControl.addRecord(newBus);
                            JOptionPane.showMessageDialog(null, "New Bus added successfully.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            new Main().setVisible(true);
                            clearPanel();
                            createTable();
                        } catch (ParseException ex) {
                            Logger.getLogger(ManageStaffPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }

            }
        }
    }

    public void updateBus() {
        if (checkTextField()) {
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String status = statusList.getSelectedItem().toString();
            String type = typeList.getSelectedItem().toString();
            Date purchaseDate = null;
            try {
                purchaseDate = dateFormat2.parse(jtfPurchaseDate.getText());
                Bus newBus = new Bus(jtfPlateNo.getText(), type, status,
                        jtfModel.getText(), jtfTotalSeat.getText(), purchaseDate, jtfBusNumber.getText());
                busControl.updateRecord(newBus);
                JOptionPane.showMessageDialog(null, "Bus updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                new Main().setVisible(true);
                clearPanel();
                createTable();
            } catch (ParseException ex) {
                Logger.getLogger(ManageBusPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void deleteBus() {
        int selectedRowIndex = recordTable.getSelectedRow();
        Object selectedObject = (Object) recordTable.getModel().getValueAt(
                recordTable.convertRowIndexToModel(selectedRowIndex), 0);
        String busPlateNo = selectedObject.toString();
        Bus bus = busControl.getRecord(busPlateNo);
        if (bus != null) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Confirm delete " + bus.getPlate_no() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                busControl.deleteRecord(bus);
                JOptionPane.showMessageDialog(null, bus.getPlate_no() + " is deleted.",
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
                buttonPanel.add(jbtUpdate);
                buttonPanel.add(jbtDelete);

            }
        }
    }

    public String generateBusID() {
        String plateNo = busControl.generateBusID();
        int busPlateNo = Integer.parseInt(plateNo.substring(2, 5));
        String finalGeneratedBusID = String.format("B%04d", (busPlateNo + 1));
        return finalGeneratedBusID;
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
