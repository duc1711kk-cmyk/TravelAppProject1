package com.travelapp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cbCategory;
    private JButton btnSearch;

    public SearchPanel() {
        // Sử dụng FlowLayout để các thành phần nằm ngang hàng nhau
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // 1. Ô nhập từ khóa
        add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(20); // Ô nhập có độ rộng 20 ký tự
        add(txtSearch);

        // 2. Hộp chọn Danh mục
        add(new JLabel("Danh mục:"));
        String[] categories = {"Tất cả", "Thiên nhiên", "Văn hóa", "Giải trí", "Mua sắm", "Nghỉ dưỡng"};
        cbCategory = new JComboBox<>(categories);
        add(cbCategory);

        // 3. Nút Tìm kiếm
        btnSearch = new JButton("Lọc & Tìm kiếm");
        btnSearch.setBackground(new Color(0, 153, 204)); // Tô màu xanh dương
        btnSearch.setForeground(Color.WHITE); // Chữ màu trắng
        add(btnSearch);
    }

    // Các hàm để MainFrame có thể lấy thông tin từ Panel này
    public String getSearchKeyword() {
        return txtSearch.getText().trim();
    }

    public String getSelectedCategory() {
        return (String) cbCategory.getSelectedItem();
    }

    // Hàm để MainFrame có thể gắn sự kiện click cho nút
    public void addSearchListener(ActionListener listener) {
        btnSearch.addActionListener(listener);
    }
}