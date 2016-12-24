
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class PaymentStatus extends JPanel {


    //---------------------------Table panels-------------------------------------
    private JPanel p1, tablePanel, filterPanel, buttonPanel;
    //---------------------------Table Objects----------------------------------
    private JScrollPane tableScrollPane;
    private TableRowSorter<TableModel> sorter;
    private JTable recordTable;
    private ListSelectionModel selectedList;
    private Object[] colHead = {"Payment id", "Payment Date", "Payment Type", "Total Amount", "Payment Amount", "Balance", "Card Number", "Card Holder", "Expiry Date", "Card Type", "Bank", "Schedule ID"};
    private JTextField jtfFilter;
    //--------------------------------Option buttons--------------------------------
    private JButton jbtCreate, jbtCreate1;
    //-------------------------------CreateForm() objects--------------------------
    private JTextField jtfBusNo, jtfDepartureDate, jtfDepartureTime, jtfPlace, jtfDestination, jtfSeatNo1, jtfSeatNo2,
            jtfSeatNo3, jtfSeatNo4, jtfTransactionDate, jtfPaymentDate, jtfTotalAmount, jtfPayment, jtfBalance, jtfPaymentId,
            jtfSeatPurchase, jtfSchedulePrice, jtfPaymentType, jtfGST, jtfPrice;
    private JButton jbtSubmit, jbtCancel;
    //---------------------------MVC objects----------------------------------
    private DefaultTableModel tableModel;

    public PaymentStatus() {
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createTable();
    }

    public void createTable() {
        int vertical = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int horizontal = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

        tableModel = new DefaultTableModel(null, colHead);
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
        recordTable.setBackground(Color.getHSBColor(12, 45, 150));

        //Table will show action when list selected
        selectedList = recordTable.getSelectionModel();
        selectedList.addListSelectionListener(new ListSelectedListener());
        recordTable.setSelectionModel(selectedList);

        tableScrollPane = new JScrollPane(recordTable, vertical, horizontal);
        tablePanel = new JPanel(new GridLayout(1, 1));
        tablePanel.add(tableScrollPane);

        //Table filter
        filterPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        jtfFilter = new JTextField();
        filterPanel = new JPanel(new GridLayout(1, 2, 8, 8));
        filterPanel.add(new JLabel("Type any keyword to search the information you needs."), FlowLayout.LEFT);
        filterPanel.add(jtfFilter);

        jtfFilter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter(jtfFilter.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter(jtfFilter.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter(jtfFilter.getText()));
            }
        });

        p1 = new JPanel(new BorderLayout());
        p1.add(filterPanel, BorderLayout.NORTH);
        p1.add(tablePanel, BorderLayout.CENTER);

        jbtCreate = new JButton("Generate Ticket");
        jbtCreate1 = new JButton("Generate Receipt");

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(jbtCreate);
        buttonPanel.add(jbtCreate1);
        jbtCreate.setEnabled(false);
        jbtCreate1.setEnabled(false);
        buttonPanel.add(Box.createVerticalStrut(15));

        jbtCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPanel();
                add(createForm());
            }
        });

        jbtCreate1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPanel();
                add(createForm1());
            }
        });

        //adding space between tablePanel and JTabbedPane
        add(Box.createVerticalStrut(20));
        add(p1, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel createForm() {
        return new JPanel();
    }

    public JPanel createForm1() {
        return new JPanel();
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
                    jbtCreate1.setEnabled(true);
                }
            }
            //Get action
            if (btnCount != 1) {
                buttonPanel.add(jbtCreate);
                buttonPanel.add(jbtCreate1);

            }
        }
    }

}
