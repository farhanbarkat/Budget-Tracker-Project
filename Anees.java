Member 2 (Anees Ahmad) Code: Balance + Summary

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

public class GUIAppMember2 {
    private JFrame frame;
    private ArrayList<Income> incomeList = new ArrayList<>();
    private ArrayList<Expense> expenseList = new ArrayList<>();

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
        // Sample data for demo purpose
        incomeList.add(new Income(5000, "Salary"));
        incomeList.add(new Income(2000, "Freelance"));
        expenseList.add(new Expense(1000, "Food", "01/06/2025"));
        expenseList.add(new Expense(1200, "Travel", "02/06/2025"));
        expenseList.add(new Expense(800, "Utilities", "03/06/2025"));
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Member 2 - Balance and Summary");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(2, 1));

        frame.getContentPane().add(getBalancePanel());
        frame.getContentPane().add(getSummaryPanel());
    }

    private JPanel getBalancePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createTitledBorder("View Balance"));

        JLabel incomeLbl = new JLabel("Total Income: ");
        JLabel expenseLbl = new JLabel("Total Expense: ");
        JLabel balanceLbl = new JLabel("Balance: ");
        JButton calcBtn = new JButton("Calculate Balance");

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

    private JPanel getSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Session Summary"));
        JTextArea textArea = new JTextArea();
        JButton showSummary = new JButton("Show Summary");

        showSummary.addActionListener(e -> {
            int totalIncome = incomeList.stream().mapToInt(Income::getAmount).sum();
            int totalExpense = expenseList.stream().mapToInt(Expense::getAmount).sum();
            String topCategory = expenseList.stream()
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingInt(Expense::getAmount)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("None");

            String summary = "Session Summary:\n"
                    + "Total Income: Rs. " + totalIncome + "\n"
                    + "Total Expense: Rs. " + totalExpense + "\n"
                    + "Balance: Rs. " + (totalIncome - totalExpense) + "\n"
                    + "Top Spending Category: " + topCategory;

            textArea.setText(summary);
        });

        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(showSummary, BorderLayout.SOUTH);

        return panel;
    }
}
