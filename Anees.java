package xyz;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Income {
    private int amount;
    private String source;

    public Income(int amount, String source) {
        this.amount = amount;
        this.source = source;
    }

    public int getAmount() { return amount; }
    public String getSource() { return source; }
}

class Expense {
    private int amount;
    private String category;
    private String date;

    public Expense(int amount, String category, String date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
}

public class GUIAppMember2 {
    private JFrame frame;
    private ArrayList<Income> incomeList = new ArrayList<>();
    private ArrayList<Expense> expenseList = new ArrayList<>();
    private DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Type", "Amount", "Category/Source", "Date"}, 0);

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GUIAppMember2 window = new GUIAppMember2();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GUIAppMember2() {
        // Sample data
        incomeList.add(new Income(5000, "Salary"));
        expenseList.add(new Expense(1000, "Food", "01/06/2025"));
        expenseList.add(new Expense(800, "Travel", "02/06/2025"));

        initialize();
    }

    private void initialize() {
        frame = new JFrame("Member 2 - View Balance, Report, Manage");
        frame.setBounds(100, 100, 750, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(1, 3));

        frame.getContentPane().add(getBalancePanel());
        frame.getContentPane().add(getReportPanel());
        frame.getContentPane().add(getManagePanel());
    }

    private JPanel getBalancePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Balance"));

        JLabel incomeLbl = new JLabel("Total Income: ");
        JLabel expenseLbl = new JLabel("Total Expense: ");
        JLabel balanceLbl = new JLabel("Balance: ");
        JButton calcBtn = new JButton("Calculate");

        calcBtn.addActionListener(e -> {
            int income = incomeList.stream().mapToInt(Income::getAmount).sum();
            int expense = expenseList.stream().mapToInt(Expense::getAmount).sum();
            incomeLbl.setText("Total Income: Rs. " + income);
            expenseLbl.setText("Total Expense: Rs. " + expense);
            balanceLbl.setText("Balance: Rs. " + (income - expense));
        });

        panel.add(incomeLbl);
        panel.add(expenseLbl);
        panel.add(balanceLbl);
        panel.add(calcBtn);
        return panel;
    }

    private JPanel getReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Category-wise Report"));

        JTextArea area = new JTextArea();
        JButton generate = new JButton("Generate Report");

        generate.addActionListener(e -> {
            Map<String, Integer> report = new HashMap<>();
            for (Expense exp : expenseList) {
                report.put(exp.getCategory(), report.getOrDefault(exp.getCategory(), 0) + exp.getAmount());
            }
            StringBuilder sb = new StringBuilder("Category-wise Report:\n");
            report.forEach((k, v) -> sb.append(k).append(": Rs. ").append(v).append("\n"));
            area.setText(sb.toString());
        });

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(generate, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getManagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JButton deleteBtn = new JButton("Delete Selected");

        // Populate sample data in table
        tableModel.addRow(new Object[]{"Income", 5000, "Salary", "-"});
        tableModel.addRow(new Object[]{"Expense", 1000, "Food", "01/06/2025"});
        tableModel.addRow(new Object[]{"Expense", 800, "Travel", "02/06/2025"});

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String type = (String) tableModel.getValueAt(row, 0);
                int amount = (int) tableModel.getValueAt(row, 1);
                String label = (String) tableModel.getValueAt(row, 2);
                tableModel.removeRow(row);
                if (type.equals("Income")) {
                    incomeList.removeIf(i -> i.getAmount() == amount && i.getSource().equals(label));
                } else {
                    expenseList.removeIf(i -> i.getAmount() == amount && i.getCategory().equals(label));
                }
            }
        });

        panel.setBorder(BorderFactory.createTitledBorder("Manage Records"));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(deleteBtn, BorderLayout.SOUTH);
        return panel;
    }
}
