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

public class GUIApp {
    private JFrame frame;
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private ArrayList<Income> incomeList = new ArrayList<>();
    private ArrayList<Expense> expenseList = new ArrayList<>();
    private LimitChecker limitChecker = new LimitChecker();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            try {
                GUIApp window = new GUIApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GUIApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Personal Budget Tracker");
        frame.setBounds(100, 100, 750, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel navPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] btnLabels = {"Add Income", "Add Expense", "Balance", "Report", "Limit", "Reset", "Summary", "Exit"};
        JButton[] buttons = new JButton[btnLabels.length];

        for (int i = 0; i < btnLabels.length; i++) {
            buttons[i] = new JButton(btnLabels[i]);
            buttons[i].setFont(new Font("Segoe UI", Font.BOLD, 13));
            buttons[i].setBackground(new Color(245, 224, 66));
            navPanel.add(buttons[i]);
        }

        cardPanel.add(getIncomePanel(), "income");
        cardPanel.add(getExpensePanel(), "expense");
        cardPanel.add(getBalancePanel(), "balance");
        cardPanel.add(getReportPanel(), "report");
        cardPanel.add(getLimitPanel(), "limit");

        buttons[0].addActionListener(e -> cardLayout.show(cardPanel, "income"));
        buttons[1].addActionListener(e -> cardLayout.show(cardPanel, "expense"));
        buttons[2].addActionListener(e -> cardLayout.show(cardPanel, "balance"));
        buttons[3].addActionListener(e -> cardLayout.show(cardPanel, "report"));
        buttons[4].addActionListener(e -> cardLayout.show(cardPanel, "limit"));
        buttons[5].addActionListener(e -> {
            incomeList.clear();
            expenseList.clear();
            JOptionPane.showMessageDialog(frame, "All data has been reset!");
        });
        buttons[6].addActionListener(e -> {
            int totalIncome = incomeList.stream().mapToInt(Income::getAmount).sum();
            int totalExpense = expenseList.stream().mapToInt(Expense::getAmount).sum();
            String topCategory = expenseList.stream()
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingInt(Expense::getAmount)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("None");
            JOptionPane.showMessageDialog(frame, "Session Summary:\nIncome: Rs. " + totalIncome +
                "\nExpense: Rs. " + totalExpense +
                "\nBalance: Rs. " + (totalIncome - totalExpense) +
                "\nTop Category: " + topCategory);
        });
        buttons[7].addActionListener(e -> System.exit(0));

        frame.getContentPane().add(navPanel, BorderLayout.NORTH);
        frame.getContentPane().add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel getIncomePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(new Color(240, 248, 255));

        JLabel lblAmt = new JLabel("Amount:");
        lblAmt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField amountField = new JTextField();

        JLabel lblSrc = new JLabel("Source:");
        lblSrc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> sourceBox = new JComboBox<>(new String[]{"Salary", "Freelance", "Other"});

        JButton saveBtn = new JButton("Save Income");
        saveBtn.setBackground(new Color(60, 179, 113));
        saveBtn.setForeground(new Color(0,0,0));
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

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

        panel.add(lblAmt); panel.add(amountField);
        panel.add(lblSrc); panel.add(sourceBox);
        panel.add(new JLabel("")); panel.add(saveBtn);
        return panel;
    }

    private JPanel getExpensePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(new Color(255, 250, 240));

        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField();
        JComboBox<String> catBox = new JComboBox<>(new String[]{"Food", "Travel", "Utilities"});
        JButton saveBtn = new JButton("Save Expense");

        saveBtn.setBackground(new Color(255, 99, 71));
        saveBtn.setForeground(Color.BLACK);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(new JLabel("Amount:")); panel.add(amountField);
        panel.add(new JLabel("Date (dd/mm/yyyy):")); panel.add(dateField);
        panel.add(new JLabel("Category:")); panel.add(catBox);
        panel.add(new JLabel("")); panel.add(saveBtn);

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
        return panel;
    }

    private JPanel getBalancePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(new Color(240, 255, 240));

        JLabel incomeLbl = new JLabel("Total Income: ");
        JLabel expenseLbl = new JLabel("Total Expense: ");
        JLabel balanceLbl = new JLabel("Balance: ");
        JButton calcBtn = new JButton("Calculate");

        calcBtn.setBackground(new Color(70, 130, 180));
        calcBtn.setForeground(Color.BLACK);
        calcBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(incomeLbl);
        panel.add(expenseLbl);
        panel.add(balanceLbl);
        panel.add(calcBtn);

        calcBtn.addActionListener(e -> {
            int income = incomeList.stream().mapToInt(Income::getAmount).sum();
            int expense = expenseList.stream().mapToInt(Expense::getAmount).sum();
            incomeLbl.setText("Total Income: Rs. " + income);
            expenseLbl.setText("Total Expense: Rs. " + expense);
            balanceLbl.setText("Balance: Rs. " + (income - expense));
        });
        return panel;
    }

    private JPanel getReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 255, 255));

        JTextArea area = new JTextArea();
        area.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JButton generate = new JButton("Generate Report");
        generate.setBackground(new Color(128, 0, 128));
        generate.setForeground(Color.BLACK);

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

    private JPanel getLimitPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(new Color(255, 248, 220));

        JComboBox<String> catBox = new JComboBox<>(new String[]{"Food", "Travel", "Utilities"});
        JTextField limitField = new JTextField();
        JLabel status = new JLabel("Status:");
        JButton check = new JButton("Check Limit");

        check.setBackground(new Color(255, 140, 0));
        check.setForeground(Color.BLACK);
        check.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(new JLabel("Select Category:"));
        panel.add(catBox);
        panel.add(new JLabel("Set Limit:"));
        panel.add(limitField);
        panel.add(check);
        panel.add(status);

        check.addActionListener(e -> {
            try {
                String cat = (String) catBox.getSelectedItem();
                int limit = Integer.parseInt(limitField.getText());
                limitChecker.setLimit(cat, limit);
                int spent = expenseList.stream().filter(x -> x.getCategory().equals(cat)).mapToInt(Expense::getAmount).sum();
                status.setText("Status: " + limitChecker.checkLimit(cat, spent));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });
        return panel;
    }
}
