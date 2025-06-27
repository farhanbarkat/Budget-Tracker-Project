package xyz;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;
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
        frame = new JFrame("Member 1 - Income, Expense, Summary, Exit");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 1, 10, 10));

        frame.add(getIncomePanel());
        frame.add(getExpensePanel());
        frame.add(getSummaryPanel());
        frame.add(getExitPanel());
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
                String source = (String) sourceBox.getSelectedItem();
                incomeList.add(new Income(amt, source));
                JOptionPane.showMessageDialog(frame, "Income saved!");
                amountField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        panel.add(new JLabel("Amount:")); panel.add(amountField);
        panel.add(new JLabel("Source:")); panel.add(sourceBox);
        panel.add(new JLabel("")); panel.add(saveBtn);
        return panel;
    }

    private JPanel getExpensePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Add Expense"));

        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField();
        JComboBox<String> catBox = new JComboBox<>(new String[]{"Food", "Travel", "Utilities"});
        JButton saveBtn = new JButton("Save Expense");

        saveBtn.addActionListener(e -> {
            try {
                int amt = Integer.parseInt(amountField.getText());
                String cat = (String) catBox.getSelectedItem();
                String date = dateField.getText();
                expenseList.add(new Expense(amt, cat, date));
                JOptionPane.showMessageDialog(frame, "Expense saved!");
                amountField.setText("");
                dateField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        panel.add(new JLabel("Amount:")); panel.add(amountField);
        panel.add(new JLabel("Date (dd/mm/yyyy):")); panel.add(dateField);
        panel.add(new JLabel("Category:")); panel.add(catBox);
        panel.add(new JLabel("")); panel.add(saveBtn);
        return panel;
    }

    private JPanel getSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Session Summary"));

        JButton summaryBtn = new JButton("Show Summary");
        summaryBtn.addActionListener(e -> {
            int totalIncome = incomeList.stream().mapToInt(Income::getAmount).sum();
            int totalExpense = expenseList.stream().mapToInt(Expense::getAmount).sum();
            String topCategory = expenseList.stream()
                    .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingInt(Expense::getAmount)))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse("None");

            JOptionPane.showMessageDialog(frame,
                    "Session Summary:\nIncome: Rs. " + totalIncome +
                            "\nExpense: Rs. " + totalExpense +
                            "\nBalance: Rs. " + (totalIncome - totalExpense) +
                            "\nTop Category: " + topCategory);
        });

        panel.add(summaryBtn);
        return panel;
    }

    private JPanel getExitPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Exit Application"));

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(exitBtn);
        return panel;
    }
}
