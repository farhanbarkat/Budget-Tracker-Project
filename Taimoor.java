package xyz;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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

class LimitChecker {
    private Map<String, Integer> setLimits = new HashMap<>();

    public void setLimit(String category, int limit) {
        setLimits.put(category, limit);
    }

    public String checkLimit(String category, int currentExpense) {
        int limit = setLimits.getOrDefault(category, Integer.MAX_VALUE);
        if (currentExpense >= limit) return "❌ Limit Exceeded!";
        else if (currentExpense >= 0.9 * limit) return "⚠️ Near Limit!";
        else return "✅ Safe";
    }
}

public class GUIAppMember3 {
    private JFrame frame;
    private ArrayList<Expense> expenseList = new ArrayList<>();
    private LimitChecker limitChecker = new LimitChecker();
    private DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Amount", "Category", "Date"}, 0);

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GUIAppMember3 window = new GUIAppMember3();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GUIAppMember3() {
        // Sample data
        expenseList.add(new Expense(1000, "Food", "01/06/2025"));
        expenseList.add(new Expense(1200, "Travel", "03/06/2025"));
        expenseList.add(new Expense(800, "Utilities", "02/06/2025"));
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Member 3 - Limit + Sort + Reset");
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(1, 3));

        frame.getContentPane().add(getLimitPanel());
        frame.getContentPane().add(getSortPanel());
        frame.getContentPane().add(getResetPanel());
    }

    private JPanel getLimitPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Limit Checker"));

        JComboBox<String> catBox = new JComboBox<>(new String[]{"Food", "Travel", "Utilities"});
        JTextField limitField = new JTextField();
        JLabel status = new JLabel("Status:");
        JButton check = new JButton("Check Limit");

        check.addActionListener(e -> {
            try {
                String cat = (String) catBox.getSelectedItem();
                int limit = Integer.parseInt(limitField.getText());
                limitChecker.setLimit(cat, limit);
                int spent = expenseList.stream()
                    .filter(x -> x.getCategory().equals(cat))
                    .mapToInt(Expense::getAmount)
                    .sum();
                status.setText("Status: " + limitChecker.checkLimit(cat, spent));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        panel.add(new JLabel("Select Category:"));
        panel.add(catBox);
        panel.add(new JLabel("Set Limit:"));
        panel.add(limitField);
        panel.add(check);
        panel.add(status);
        return panel;
    }

    private JPanel getSortPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Sort Expenses"));
        JTextArea area = new JTextArea();

        JButton sortByAmount = new JButton("Sort by Amount");
        JButton sortByDate = new JButton("Sort by Date");

        sortByAmount.addActionListener(e -> {
            ArrayList<Expense> sorted = new ArrayList<>(expenseList);
            sorted.sort(Comparator.comparingInt(Expense::getAmount));
            area.setText("Sorted by Amount:\n");
            for (Expense exp : sorted) {
                area.append(exp.getCategory() + " - Rs." + exp.getAmount() + " - " + exp.getDate() + "\n");
            }
        });

        sortByDate.addActionListener(e -> {
            ArrayList<Expense> sorted = new ArrayList<>(expenseList);
            sorted.sort(Comparator.comparing(Expense::getDate));
            area.setText("Sorted by Date:\n");
            for (Expense exp : sorted) {
                area.append(exp.getCategory() + " - Rs." + exp.getAmount() + " - " + exp.getDate() + "\n");
            }
        });

        JPanel top = new JPanel(new GridLayout(1, 2));
        top.add(sortByAmount);
        top.add(sortByDate);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getResetPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Reset Data"));
        JButton resetBtn = new JButton("Reset Expenses");

        resetBtn.addActionListener(e -> {
            expenseList.clear();
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(frame, "All expenses cleared!");
        });

        panel.add(resetBtn, BorderLayout.CENTER);
        return panel;
    }
}
