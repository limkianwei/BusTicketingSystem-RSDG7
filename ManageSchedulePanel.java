
import control.MaintainSchedule;
import control.MaintainBus;
import control.MaintainSeat;
import domain.Schedule;
import domain.Bus;
import domain.Seat;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.table.*;

public class ManageSchedulePanel extends JPanel {

    private MaintainBus busControl;
    private MaintainSchedule scheduleControl;
    private MaintainSeat seatControl;
    //---------------------------Table panels-------------------------------------
    private JPanel p1, tablePanel, buttonPanel;
    //---------------------------Table Objects----------------------------------
    private JScrollPane tableScrollPane;
    private TableRowSorter<TableModel> sorter;
    private JTable recordTable;
    private ListSelectionModel selectedList;
    private Object[] colHead = {"Schedule id", "Departure Date", "Departure Time", "Departure Place", "Destination", "Price (RM)", "No of available seat", "Bus Plate No", "Seat Availability"};
    //--------------------------------Option buttons--------------------------------
    private JButton jbtCreate, jbtUpdate;
    //-------------------------------CreateForm() objects--------------------------
    private JPanel datePanel;
    private JTextField jtfScheduleId, jtfAvailableSeat, jtfSeatAvailability;
    private JTextField jtfScheduleID, jtfBusId, jtfSeatId, jtfSeatStatus, jtfSeatNo;
    private JComboBox priceList, destinationList, placeList, timeList, busList;
    private JButton jbtSubmit, jbtCancel;
    //---------------------------Date objects in form-----------------------------
    private ArrayList<String> calYear = new ArrayList<String>();
    private ArrayList<String> calMonth = new ArrayList<String>();
    private ArrayList<String> calDay = new ArrayList<String>();
    private JComboBox yearList, monthList, dayList;
    private ArrayList<Bus> busArr;
    //------------------- state objects in form-----------------------
    private String price[] = {"6.00", "7.00", "8.00", "9.00", "10.00", "11.00", "12.00", "13.00", "14.00", "15.00"};
    private String destination[] = {"Pudu Sentral", "Seremban", "Ipoh", "Kuala Lumpur"};
    private String departurePlace[] = {"Pudu Sentral", "Seremban", "Ipoh", "Kuala Lumpur"};
    private String departureTime[] = {"8:00:00", "9:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00"};
    //---------------------------MVC objects----------------------------------
    private DefaultTableModel tableModel;
    private DefaultComboBoxModel priceModel = new DefaultComboBoxModel(price);
    private DefaultComboBoxModel destinationModel = new DefaultComboBoxModel(destination);
    private DefaultComboBoxModel departurePlaceModel = new DefaultComboBoxModel(departurePlace);
    private DefaultComboBoxModel departureTimeModel = new DefaultComboBoxModel(departureTime);
    private DefaultComboBoxModel busModel = new DefaultComboBoxModel();

    public ManageSchedulePanel() {
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        scheduleControl = new MaintainSchedule();
        busControl = new MaintainBus();
        seatControl = new MaintainSeat();
        initializeBusModel();
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

            //To let column width auto adjust
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                // +5 to let some space and better read
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width + 40, tableColumn.getPreferredWidth()));
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

        p1 = new JPanel(new BorderLayout());
        p1.add(tablePanel, BorderLayout.CENTER);

        jbtCreate = new JButton("Create new schedule");
        jbtUpdate = new JButton("Update schedule information");

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
                setScheduleForm();
            }
        });

        //adding space between tablePanel and JTabbedPane
        add(Box.createVerticalStrut(20));
        add(p1, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel createForm(final int which) {
        jtfScheduleId = new JTextField();
        jtfAvailableSeat = new JTextField();
        jtfSeatAvailability = new JTextField();
        jbtSubmit = new JButton("Submit");
        jbtCancel = new JButton("Cancel");

        JPanel formPanel = new JPanel(new GridLayout(18, 4, 5, 5));
        datePanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        //Setup year
        Calendar nextYear = Calendar.getInstance();
        final int currentYear = new GregorianCalendar().get(Calendar.YEAR);
        calYear.add("" + currentYear);
        for (int i = currentYear; i < (currentYear + 10); i = nextYear.get(Calendar.YEAR)) {
            nextYear.add(Calendar.YEAR, +1);
            if (nextYear.get(Calendar.YEAR) >= (currentYear + 1)) {
                calYear.add("" + nextYear.get(Calendar.YEAR));
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
        jtfScheduleId.setText(generateScheduleID());
        jtfScheduleId.setEditable(false);

        if (which == 1) {
            formPanel.setBackground(Color.CYAN);
            formPanel.setBorder(BorderFactory.createTitledBorder(null, "Create Schedule",
                    TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
        } else if (which == 2) {
            formPanel.setBackground(Color.PINK);
            formPanel.setBorder(BorderFactory.createTitledBorder(null, "Update Schedule",
                    TitledBorder.LEFT, TitledBorder.LEFT, new Font("Verdana", Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 25)));
        }
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JLabel("Schedule Id"));
        formPanel.add(jtfScheduleId);
        formPanel.add(new JLabel("Departure Date"));
        formPanel.add(datePanel);
        formPanel.add(new JLabel("Departure Time"));
        formPanel.add(timeList = new JComboBox(departureTimeModel));
        formPanel.add(new JLabel("Departure Place"));
        formPanel.add(placeList = new JComboBox(departurePlaceModel));
        formPanel.add(new JLabel("Destination"));
        formPanel.add(destinationList = new JComboBox(destinationModel));
        formPanel.add(new JLabel("Price (RM)"));
        formPanel.add(priceList = new JComboBox(priceModel));
        formPanel.add(new JLabel("Number of available seat"));
        formPanel.add(jtfAvailableSeat);
        formPanel.add(new JLabel("Bus Plate No"));
        formPanel.add(busList = new JComboBox(busModel));
        formPanel.add(new JLabel("Seat availability"));
        formPanel.add(jtfSeatAvailability);
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));
        formPanel.add(new JPanel().add(Box.createVerticalStrut(4)));

        //Set to default
        timeList.setSelectedIndex(0);
        placeList.setSelectedIndex(0);
        destinationList.setSelectedIndex(0);
        priceList.setSelectedIndex(0);
        jtfSeatAvailability.setText("Nothaveseat");
        jtfSeatAvailability.setEditable(false);

        if (which == 1 || which == 2) {
            busList.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    String bus = busList.getSelectedItem().toString();
                    Bus busno = busControl.getRecord(bus);
                    String available = busno.getTotal_seat();
                    if (available.toString().equals("40")) {
                        jtfAvailableSeat.setText(available);
                    } else {
                        jtfAvailableSeat.setText(available);
                    }
                }
            });
        }

        formButtonPanel.add(jbtSubmit);
        formButtonPanel.add(jbtCancel);

        jbtSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (which == 1) {
                    addSchedule();
                } else if (which == 2) {
                    updateSchedule();
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

    public void setScheduleForm() {
        int selectedRowIndex = recordTable.getSelectedRow();
        Object selectedObject = (Object) recordTable.getModel().getValueAt(
                recordTable.convertRowIndexToModel(selectedRowIndex), 0);
        String schedule_id = selectedObject.toString();
        Schedule schedule = scheduleControl.getRecord(schedule_id);
        if (schedule != null) {
            jtfScheduleId.setText(schedule.getId());
            jtfScheduleId.setEditable(false);

            Date departureDate = schedule.getDepartureDate();
            Calendar DateCal = Calendar.getInstance();
            DateCal.setTime(departureDate);
            String year = new SimpleDateFormat("yyyy").format(DateCal.getTime());
            int month = Integer.parseInt(new SimpleDateFormat("MM").format(DateCal.getTime()));
            int day = Integer.parseInt(new SimpleDateFormat("dd").format(DateCal.getTime()));
            yearList.setSelectedItem(year);
            monthList.setSelectedIndex(month - 1);
            dayList.setSelectedIndex(day - 1);

            Date departureTime = schedule.getDepartureTime();
            Calendar TimeCal = Calendar.getInstance();
            TimeCal.setTime(departureTime);
            String time = new SimpleDateFormat("hh:mm:ss").format(TimeCal.getTime());
            timeList.setSelectedItem(time);

            priceList.setSelectedItem(schedule.getPrice());
            placeList.setSelectedItem(schedule.getDeparturePlace());
            destinationList.setSelectedItem(schedule.getDestination());
            jtfAvailableSeat.setText("" + schedule.getAvailableSeat());
            jtfAvailableSeat.setEditable(false);
            jtfSeatAvailability.setText(schedule.getBus_seat_availability());
            jtfSeatAvailability.setEditable(false);
            busList.setEnabled(false);
            busList.setSelectedItem(schedule.getBus().getPlate_no());
        }
    }

    public boolean checkTextField() {
        boolean valid = true;
        if (jtfScheduleId.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Schedule Id cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfScheduleId.requestFocusInWindow();
            valid = false;
        } else if (placeList.getSelectedItem().toString().equals(destinationList.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(null, "Departure place and destination cannot be same.", "Warning", JOptionPane.WARNING_MESSAGE);
            placeList.requestFocusInWindow();
            valid = false;
        } else if (jtfAvailableSeat.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Available Seat cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfAvailableSeat.requestFocusInWindow();
            valid = false;
        } else if (jtfSeatAvailability.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Seat availability cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            jtfSeatAvailability.requestFocusInWindow();
            valid = false;
        }

        return valid;
    }

    public void addSchedule() {
        Schedule schedule = scheduleControl.getRecord(jtfScheduleId.getText());

        if (schedule == null) {
            if (checkTextField()) {
                //Schedule variable
                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
                DateFormat dateFormat2 = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
                DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String time = timeList.getSelectedItem().toString();
                String destination = destinationList.getSelectedItem().toString();
                String place = placeList.getSelectedItem().toString();
                String date = yearList.getSelectedItem().toString() + "-"
                        + monthList.getSelectedItem().toString() + "-"
                        + dayList.getSelectedItem().toString();
                String price = priceList.getSelectedItem().toString();
                Date departureDate = null;
                Date departureTime = null;
                String bus = busList.getSelectedItem().toString();

                //Today Date
                String todayD = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date().getTime());
                Date todayDate = null;

                try {
                    todayDate = dateFormat3.parse(todayD);
                    departureDate = dateFormat1.parse(date);
                    departureTime = dateFormat2.parse(time);
                    double price1 = Double.parseDouble(price);
                    int availableSeat = Integer.parseInt(jtfAvailableSeat.getText());
                    Bus busno = busControl.getRecord(bus);

                    if (departureDate.after(todayDate)) {

                        Schedule newSchedule = new Schedule(jtfScheduleId.getText(), departureDate, departureTime, place, destination, price1, availableSeat, busno, jtfSeatAvailability.getText());
                        scheduleControl.addRecord(newSchedule);
                        JOptionPane.showMessageDialog(null, "New Schedule added successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearPanel();
                        createTable();

                        Schedule schedule1 = scheduleControl.getRecord(jtfScheduleId.getText());
                        String schedule_id = jtfScheduleId.getText();
                        Bus bus1 = busControl.getRecord(bus);

                        jtfScheduleID = new JTextField();
                        jtfBusId = new JTextField();
                        jtfSeatId = new JTextField();
                        jtfSeatStatus = new JTextField();
                        jtfSeatNo = new JTextField();
                        jtfSeatAvailability = new JTextField();
                        Date date1 = schedule1.getDepartureDate();
                        Date time1 = schedule1.getDepartureTime();
                        String place1 = schedule1.getDeparturePlace();
                        String destination1 = schedule1.getDestination();
                        Double price2 = schedule1.getPrice();
                        Integer number_of_seat = schedule1.getAvailableSeat();
                        String available = null;

                        if (bus1.getTotal_seat().equals("20")) {
                            for (int number = 1; number <= 20; ++number) {
                                jtfSeatId.setText(generateSeatID());
                                jtfSeatNo.setText("" + number);
                                jtfSeatStatus.setText("Available");
                                jtfBusId.setText(bus);
                                jtfScheduleID.setText(schedule_id);
                                Bus busno1 = busControl.getRecord(jtfBusId.getText());
                                Schedule scheduleid = scheduleControl.getRecord(jtfScheduleID.getText());
                                Seat newSeat = new Seat(jtfSeatId.getText(), jtfSeatNo.getText(), jtfSeatStatus.getText(), busno, scheduleid);
                                seatControl.addRecord(newSeat);
                                jtfSeatAvailability.setText("Haveseat");
                                available = jtfSeatAvailability.getText();
                                Schedule newSchedule1 = new Schedule(jtfScheduleID.getText(), date1, time1, place1, destination1, price2, number_of_seat, busno1, available);
                                scheduleControl.updateRecord(newSchedule1);
                            }
                        } else if (bus1.getTotal_seat().equals("40")) {
                            for (int number = 1; number <= 40; ++number) {
                                jtfSeatId.setText(generateSeatID());
                                jtfSeatNo.setText("" + number);
                                jtfSeatStatus.setText("Available");
                                jtfBusId.setText(bus);
                                jtfScheduleID.setText(schedule_id);
                                Bus busno1 = busControl.getRecord(jtfBusId.getText());
                                Schedule scheduleid = scheduleControl.getRecord(jtfScheduleID.getText());
                                Seat newSeat = new Seat(jtfSeatId.getText(), jtfSeatNo.getText(), jtfSeatStatus.getText(), busno, scheduleid);
                                seatControl.addRecord(newSeat);
                                jtfSeatAvailability.setText("Haveseat");
                                available = jtfSeatAvailability.getText();
                                Schedule newSchedule2 = new Schedule(jtfScheduleID.getText(), date1, time1, place1, destination1, price2, number_of_seat, busno1, available);
                                scheduleControl.updateRecord(newSchedule2);
                            }
                        }
                        JOptionPane.showMessageDialog(null, "" + jtfScheduleID.getText() + " with " + jtfBusId.getText() + " add " + bus1.getTotal_seat() + " seat successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        new Main().setVisible(true);
                        clearPanel();
                        createTable();

                    } else {
                        JOptionPane.showMessageDialog(null, "You cannot assign today and previous day schedule.",
                                "Failed", JOptionPane.INFORMATION_MESSAGE);
                        clearPanel();
                        createTable();
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(ManageStaffPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else {
            JOptionPane.showMessageDialog(null, "Schedule already exist by according to Schedule Id you entered.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateSchedule() {
        if (checkTextField()) {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
            DateFormat dateFormat2 = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
            DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String time = timeList.getSelectedItem().toString();
            String destination = destinationList.getSelectedItem().toString();
            String place = placeList.getSelectedItem().toString();
            String date = yearList.getSelectedItem().toString() + "-"
                    + monthList.getSelectedItem().toString() + "-"
                    + dayList.getSelectedItem().toString();
            String price = priceList.getSelectedItem().toString();
            Date departureDate = null;
            Date departureTime = null;
            String bus = busList.getSelectedItem().toString();
            String todayD = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date().getTime());
            Date todayDate = null;

            try {
                todayDate = dateFormat3.parse(todayD);
                departureDate = dateFormat1.parse(date);
                departureTime = dateFormat2.parse(time);
                double price1 = Double.parseDouble(price);
                int availableSeat = Integer.parseInt(jtfAvailableSeat.getText());
                Bus busno = busControl.getRecord(bus);

                if (departureDate.after(todayDate)) {
                    Schedule newSchedule = new Schedule(jtfScheduleId.getText(), departureDate, departureTime, place, destination, price1, availableSeat, busno, jtfSeatAvailability.getText());
                    scheduleControl.updateRecord(newSchedule);
                    JOptionPane.showMessageDialog(null, "Schedule updated successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    clearPanel();
                    createTable();
                    new Main().setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "You cannot update today and previous day schedule.",
                            "Failed", JOptionPane.INFORMATION_MESSAGE);
                    clearPanel();
                    createTable();
                }
            } catch (ParseException ex) {
                Logger.getLogger(ManageStaffPanel.class
                        .getName()).log(Level.SEVERE, null, ex);
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
            }
        }
    }

    public String generateScheduleID() {
        String scheduleId = scheduleControl.generateScheduleID();
        int scheduleNo = Integer.parseInt(scheduleId.substring(2, 6));
        String finalGeneratedScheduleID = String.format("H%05d", (scheduleNo + 1));
        return finalGeneratedScheduleID;

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

    private void initializeBusModel() {
        busArr = busControl.getAll();
        for (int i = 0; i < busArr.size(); ++i) {
            busModel.addElement(busArr.get(i).getPlate_no());
        }
    }

    public String generateSeatID() {
        String seatId = seatControl.generateSeatID();
        int seatNo = Integer.parseInt(seatId.substring(1, 5));
        String finalGeneratedSeatID = String.format("E%04d", (seatNo + 1));
        return finalGeneratedSeatID;
    }

}
