Member 3(Taimoor) Code: Limit Checker + Reset

package xyz;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

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
    public void setLimit(String category, int limit) { setLimits.put(category, limit); }
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
        // Sample expenses
        expenseList.add(new Expense(1000, "Food", "01/06/2025"));
        expenseList.add(new Expense(1200, "Travel", "02/06/2025"));
        expenseList.add(new Expense(800, "Utilities", "03/06/2025"));
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Member 3 - Limit Checker + Reset");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(2, 1));

        frame.getContentPane().add(getLimitPanel());
        frame.getContentPane().add(getResetPanel());
    }

    private JPanel getLimitPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Limit Checker"));

        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"Food", "Travel", "Utilities"});
        JTextField limitField = new JTextField();
        JLabel statusLabel = new JLabel("Status:");
        JButton checkBtn = new JButton("Check Limit");

        checkBtn.addActionListener(e -> {
            try {
                String cat = (String) categoryBox.getSelectedItem();
                int limit = Integer.parseInt(limitField.getText());
                limitChecker.setLimit(cat, limit);

                int spent = expenseList.stream()
                    .filter(x -> x.getCategory().equals(cat))
                    .mapToInt(Expense::getAmount)
                    .sum();

                String status = limitChecker.checkLimit(cat, spent);
                statusLabel.setText("Status: " + status);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input!");
            }
        });

        panel.add(new JLabel("Select Category:"));
        panel.add(categoryBox);
        panel.add(new JLabel("Set Limit:"));
        panel.add(limitField);
        panel.add(checkBtn);
        panel.add(statusLabel);

        return panel;
    }

    private JPanel getResetPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Reset Data"));
        JButton resetBtn = new JButton("Reset Expenses");

        resetBtn.addActionListener(e -> {
            expenseList.clear();
            JOptionPane.showMessageDialog(frame, "All expenses cleared!");
        });

        panel.add(resetBtn);
        return panel;
    }
}
