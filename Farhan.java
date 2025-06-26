 Member 1 Farhan: Add Income & Expense Panels only

package xyz;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

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

public class GUIAppMember1 {
    private JFrame frame;
    private ArrayList<Income> incomeList = new ArrayList<>();
    private ArrayList<Expense> expenseList = new ArrayList<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GUIAppMember1 window = new GUIAppMember1();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GUIAppMember1() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Member 1 - Income & Expense Panel");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(2, 1));

        frame.getContentPane().add(getIncomePanel());
        frame.getContentPane().add(getExpensePanel());
    }

    private JPanel getIncomePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Add Income"));

        JTextField amountField = new JTextField();
        JComboBox<String> sourceBox = new JComboBox<>(new String[]{"Salary", "Freelance", "Other"});
        JButton saveBtn = new JButton("Save Income");

        saveBtn.addActionListener(e -> {
            try {
                int amt = Integer.parseInt(amountField.getText());
                String src = (String) sourceBox.getSelectedItem();
                incomeList.add(new Income(amt, src));
                JOptionPane.showMessageDialog(frame, "Income saved!");
                amountField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount");
            }
        });

        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Source:"));
        panel.add(sourceBox);
        panel.add(new JLabel(""));
        panel.add(saveBtn);

        return panel;
    }

    private JPanel getExpensePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Add Expense"));

        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField();
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"Food", "Travel", "Utilities"});
        JButton saveBtn = new JButton("Save Expense");

        saveBtn.addActionListener(e -> {
            try {
                int amt = Integer.parseInt(amountField.getText());
                String cat = (String) categoryBox.getSelectedItem();
                String date = dateField.getText();
                expenseList.add(new Expense(amt, cat, date));
                JOptionPane.showMessageDialog(frame, "Expense saved!");
                amountField.setText("");
                dateField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Date (dd/mm/yyyy):"));
        panel.add(dateField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);
        panel.add(new JLabel(""));
        panel.add(saveBtn);

        return panel;
    }
}
