
import control.MaintainStaff;
import domain.Staff;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageStaffPanel extends JPanel {
    private MaintainStaff staffControl;
    //---------------------------Table panels-------------------------------------
    private JPanel p1, tablePanel, buttonPanel;
    //---------------------------Table Objects----------------------------------
    private JScrollPane tableScrollPane;
    private TableRowSorter<TableModel> sorter;
    private JTable recordTable;
    private ListSelectionModel selectedList;
    private Object[] colHead = {"Staff ID", "Name", "IC", "Email", "DOB", "Contact No"};
    //--------------------------------Option buttons--------------------------------
    private JButton jbtCreate, jbtUpdate, jbtDelete;
    //-------------------------------CreateForm() objects--------------------------
    private JPanel datePanel;
    private JTextField jtfID, jtfName, jtfIC, jtfEmail, jtfContactNo;
    private JButton jbtSubmit, jbtCancel;
    //---------------------------Date objects in form-----------------------------
    private ArrayList<String> calYear = new ArrayList<String>();
    private ArrayList<String> calMonth = new ArrayList<String>();
    private ArrayList<String> calDay = new ArrayList<String>();
    private JComboBox yearList, monthList, dayList;
    //---------------------------MVC objects----------------------------------
    private DefaultTableModel tableModel;
    private String host = "jdbc:derby://localhost:1527/kmb";
    private String user = "nbuser";
    private String password = "123";

    public ManageStaffPanel() {
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        staffControl = new MaintainStaff();
        createTable();
    }

    public void createTable() {
        int vertical = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int horizontal = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

        tableModel = new DefaultTableModel(staffControl.getAllRecord(), colHead);
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
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width + 5, tableColumn.getPreferredWidth()));
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
        recordTable.setBackground(Color.getHSBColor(255, 164, 101));

        //Table will show action when list selected
        selectedList = recordTable.getSelectionModel();
        selectedList.addListSelectionListener(new ListSelectedListener());
        recordTable.setSelectionModel(selectedList);

        tableScrollPane = new JScrollPane(recordTable, vertical, horizontal);
        tablePanel = new JPanel(new GridLayout(1, 1));
        tablePanel.add(tableScrollPane);


        p1 = new JPanel(new BorderLayout());
        p1.add(tablePanel, BorderLayout.CENTER);

        jbtCreate = new JButton("Create a new staff");
        jbtUpdate = new JButton("Update staff data");
        jbtDelete = new JButton("Delete current staff");

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
                setStaffForm();
            }
        });
        jbtDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStaff();
            }
        });

        //adding space between tablePanel and JTabbedPane
        add(Box.createVerticalStrut(20));
        add(p1, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel createForm(final int which) {
        jtfID = new JTextField();
        jtfName = new JTextField();
        jtfIC = new JTextField();
        jtfEmail = new JTextField();
        jtfContactNo = new JTextField();
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
        jtfID.setText(generateStaffID());
        jtfContactNo.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (getLength() + str.length() <= 12) {
                    super.insertString(offs, str, a);
                }
            }
        });
        jtfName.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (getLength() + str.length() <= 50) {
                    super.insertString(offs, str, a);
                }
            }
        });
        jtfIC.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (getLength() + str.length() <= 14) {
                    super.insertString(offs, str, a);
                }
            }
        });

        jtfEmail.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (getLength() + str.length() <= 50) {
                    super.insertString(offs, str, a);
                }
            }
        });

        if (which == 1) {
            formPanel.setBackground(Color.CYAN);
            formPanel.setBorder(BorderFactory.createTitledBorder(null, "Create Staff",
                    TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
        } else if (which == 2) {
            formPanel.setBackground(Color.PINK);
            formPanel.setBorder(BorderFactory.createTitledBorder(null, "Update Staff",
                    TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
        }
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JLabel("Staff ID"));
        formPanel.add(jtfID);
        formPanel.add(new JLabel("Staff Name"));
        formPanel.add(jtfName);
        formPanel.add(new JLabel("Staff IC"));
        formPanel.add(jtfIC);
        formPanel.add(new JLabel("Email"));
        formPanel.add(jtfEmail);
        formPanel.add(new JLabel("Birthday"));
        formPanel.add(datePanel);
        formPanel.add(new JLabel("Contact number"));
        formPanel.add(jtfContactNo);
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));

        formButtonPanel.add(jbtSubmit);
        formButtonPanel.add(jbtCancel);

        jbtSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (which == 1) {
                    addStaff();
                } else if (which == 2) {
                    updateStaff();
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

    public void setStaffForm() {
        int selectedRowIndex = recordTable.getSelectedRow();
        Object selectedObject = (Object) recordTable.getModel().getValueAt(
                recordTable.convertRowIndexToModel(selectedRowIndex), 0);
        String staffID = selectedObject.toString();
        Staff staff = staffControl.getRecord(staffID);
        if (staff != null) {
            jtfID.setText(staff.getId());
            jtfID.setEditable(false);
            jtfName.setText(staff.getName());
            jtfName.setEditable(false);
            jtfIC.setText(staff.getIc());
            jtfIC.setEditable(false);

            //Setup birthday
            Date dob = staff.getDob();
            Calendar dobCal = Calendar.getInstance();
            dobCal.setTime(dob);
            String year = new SimpleDateFormat("yyyy").format(dobCal.getTime());
            int month = Integer.parseInt(new SimpleDateFormat("MM").format(dobCal.getTime()));
            int day = Integer.parseInt(new SimpleDateFormat("dd").format(dobCal.getTime()));
            yearList.setSelectedItem(year);
            monthList.setSelectedIndex(month - 1);
            dayList.setSelectedIndex(day - 1);

            jtfContactNo.setText(staff.getContactNo());
            jtfEmail.setText(staff.getEmail());
        }
    }

    public boolean checkTextField() {
        boolean valid = true;
        if (jtfID.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Staff ID cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfID.requestFocusInWindow();
            valid = false;
        } else if (jtfName.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Staff Name cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfName.requestFocusInWindow();
            valid = false;
        } else if (jtfIC.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Staff IC cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfIC.requestFocusInWindow();
            valid = false;
        } else if (!jtfIC.getText().matches("\\d{6}-\\d{2}-\\d{4}")) {
            JOptionPane.showMessageDialog(null, "Staff IC invalid format.For example:960101-01-0101", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfIC.requestFocusInWindow();
            valid = false;
        } else if (jtfEmail.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Email cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfEmail.requestFocusInWindow();
            valid = false;
        } else if (!jtfEmail.getText().matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{3}")) {
            JOptionPane.showMessageDialog(null, "Email invalid format. For example: xxxx@xxx.com", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfEmail.requestFocusInWindow();
            valid = false;
        } else if (jtfContactNo.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Contact Number cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfContactNo.requestFocusInWindow();
            valid = false;
        } else if (!jtfContactNo.getText().matches("0\\d{1,2}-\\d{7,8}")) {
            JOptionPane.showMessageDialog(null, "Contact Number invalid format.For example:0XX-XXXXXXX", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfContactNo.requestFocusInWindow();
            valid = false;
        } 
        return valid;
    }

    public void addStaff() {
        Staff staff = staffControl.getRecord(jtfID.getText());
        if (staff != null) {

            JOptionPane.showMessageDialog(null, "Staff already exist by according to Staff ID you entered.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (checkTextField()) {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String dob = yearList.getSelectedItem().toString() + "-"
                    + monthList.getSelectedItem().toString() + "-"
                    + dayList.getSelectedItem().toString();
            Date birthday = null;
            String name = jtfName.getText();
            name = name.toUpperCase();
            String ic = jtfIC.getText();

            try {

                Connection conn = DriverManager.getConnection(host, user, password);
                Statement stmt = conn.createStatement();

                PreparedStatement statement = null;
                ResultSet rs = null;
                String queryStr = "SELECT NAME FROM STAFF WHERE NAME = '" + name + "'";
                statement = conn.prepareStatement(queryStr);
                rs = statement.executeQuery();
                
                PreparedStatement statement1 = null;
                ResultSet rs1 = null;
                String queryStr1 = "SELECT IC FROM STAFF WHERE IC = '" + ic + "'";
                statement1 = conn.prepareStatement(queryStr1);
                rs1 = statement1.executeQuery();

                if (rs.next()) {
                    String Name = rs.getString("NAME");
                    if (Name.equalsIgnoreCase(name)) {
                        JOptionPane.showMessageDialog(null, name + " is already exists ");
                    }
                } else if (rs1.next()) {
                    String IC = rs1.getString("IC");
                    if (IC.equalsIgnoreCase(ic)) {
                        JOptionPane.showMessageDialog(null, ic + " is already exists ");
                    }
                } else {
                    try {
                        birthday = dateFormat1.parse(dob);
                        Staff newStaff = new Staff(jtfID.getText(), jtfName.getText().toUpperCase(),
                                jtfIC.getText(), jtfEmail.getText(), birthday, jtfContactNo.getText());
                        staffControl.addRecord(newStaff);
                        JOptionPane.showMessageDialog(null, "Staff added successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
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

    public void updateStaff() {
       if (checkTextField()) {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String dob = yearList.getSelectedItem().toString() + "-"
                    + monthList.getSelectedItem().toString() + "-"
                    + dayList.getSelectedItem().toString();
            Date birthday = null;
            try {
                birthday = dateFormat1.parse(dob);
                Staff newStaff = new Staff(jtfID.getText(), jtfName.getText().toUpperCase(),
                        jtfIC.getText(), jtfEmail.getText(), birthday, jtfContactNo.getText());
                staffControl.updateRecord(newStaff);
                JOptionPane.showMessageDialog(null, "Staff updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearPanel();
                createTable();
            } catch (ParseException ex) {
                Logger.getLogger(ManageStaffPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void deleteStaff() {
        int selectedRowIndex = recordTable.getSelectedRow();
        Object selectedObject = (Object) recordTable.getModel().getValueAt(
                recordTable.convertRowIndexToModel(selectedRowIndex), 0);
        String staffID = selectedObject.toString();
        Staff staff = staffControl.getRecord(staffID);
        if (staff != null) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Confirm delete " + staff.getName() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                staffControl.deleteRecord(staff);
                JOptionPane.showMessageDialog(null, staff.getName() + " is deleted.",
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

    public String generateStaffID() {
        String staffID = staffControl.generateStaffID();
        int staffIDNum = Integer.parseInt(staffID.substring(2, 5));
        String finalGeneratedStaffID = String.format("S%04d", (staffIDNum + 1));
        return finalGeneratedStaffID;
    }

}
