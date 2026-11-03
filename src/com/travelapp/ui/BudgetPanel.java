package com.travelapp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BudgetPanel extends JPanel {
    private JTextField txtBudget;
    private JButton btnSuggest;
    private JTextArea txtResult;

    public BudgetPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Phần trên: Nhập ngân sách
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.add(new JLabel("Nhập ngân sách tối đa của bạn (VND):"));
        txtBudget = new JTextField(15);
        topPanel.add(txtBudget);
        
        btnSuggest = new JButton("Gợi ý Lịch trình (Knapsack)");
        btnSuggest.setBackground(new Color(46, 204, 113)); // Màu xanh lá
        btnSuggest.setForeground(Color.WHITE);
        topPanel.add(btnSuggest);

        add(topPanel, BorderLayout.NORTH);

        // Phần dưới: Hiển thị kết quả
        txtResult = new JTextArea();
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtResult.setBorder(BorderFactory.createTitledBorder("Kết quả gợi ý tốt nhất"));
        
        JScrollPane scrollPane = new JScrollPane(txtResult);
        add(scrollPane, BorderLayout.CENTER);
    }

    public String getBudgetText() {
        return txtBudget.getText().trim();
    }

    public void setResultText(String text) {
        txtResult.setText(text);
    }

    public void addSuggestListener(ActionListener listener) {
        btnSuggest.addActionListener(listener);
    }
}