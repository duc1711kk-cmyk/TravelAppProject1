package com.travelapp.ui;

import com.travelapp.model.DayPlan;
import com.travelapp.model.Destination;
import com.travelapp.model.Itinerary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/** Tab lịch trình: quản lý các ngày và địa điểm trong từng ngày. */
public class ItineraryPanel extends JPanel {
    private final DefaultListModel<DayPlan> dayListModel = new DefaultListModel<>();
    private final JList<DayPlan> dayList = new JList<>(dayListModel);
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField txtBudget = new JTextField(10);
    private final JLabel lblTotal = new JLabel("Tổng chi phí: 0 VND");
    private final JButton btnAddDay = new JButton("➕ Thêm ngày");
    private final JButton btnRemoveDay = new JButton("🗑 Xóa ngày");
    private final JButton btnRemoveDest = new JButton("❌ Bỏ địa điểm");
    private final JButton btnAutoPlan = new JButton("✨ Tự lập từ ngân sách");
    private final JButton btnExport = new JButton("💾 Xuất file");

    public ItineraryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Trên: ngân sách + nút tự lập + xuất
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.add(new JLabel("Ngân sách (VND, tùy chọn):"));
        top.add(txtBudget);
        btnAutoPlan.setBackground(new Color(46, 204, 113));
        btnAutoPlan.setForeground(Color.WHITE);
        top.add(btnAutoPlan);
        top.add(btnExport);
        add(top, BorderLayout.NORTH);

        // Trái: danh sách ngày
        dayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel left = new JPanel(new BorderLayout(5, 5));
        left.setBorder(BorderFactory.createTitledBorder("Các ngày"));
        left.add(new JScrollPane(dayList), BorderLayout.CENTER);
        JPanel dayBtns = new JPanel(new GridLayout(2, 1, 5, 5));
        dayBtns.add(btnAddDay);
        dayBtns.add(btnRemoveDay);
        left.add(dayBtns, BorderLayout.SOUTH);
        left.setPreferredSize(new Dimension(220, 0));
        add(left, BorderLayout.WEST);

        // Giữa: địa điểm của ngày đang chọn
        String[] cols = {"ID", "Tên địa điểm", "Danh mục", "Chi phí (VND)", "Đánh giá (*)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        JPanel center = new JPanel(new BorderLayout(5, 5));
        center.setBorder(BorderFactory.createTitledBorder("Địa điểm trong ngày"));
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        center.add(btnRemoveDest, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        // Dưới: tổng chi phí
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTotal, BorderLayout.SOUTH);

        dayList.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) showSelectedDay(); });
    }

    /** Nạp lại giao diện từ dữ liệu lịch trình, giữ nguyên ngày đang chọn nếu có thể. */
    public void refresh(Itinerary itinerary, double budget) {
        int selected = dayList.getSelectedIndex();
        dayListModel.clear();
        for (DayPlan d : itinerary.getDays()) dayListModel.addElement(d);
        if (!itinerary.getDays().isEmpty()) {
            dayList.setSelectedIndex(Math.min(Math.max(selected, 0), itinerary.getDays().size() - 1));
        }
        showSelectedDay();

        double total = itinerary.getTotalCost();
        boolean over = budget > 0 && total > budget;
        lblTotal.setText(String.format("Tổng chi phí: %,.0f VND%s", total,
                over ? String.format("  ⚠ Vượt ngân sách %,.0f VND", total - budget) : ""));
        lblTotal.setForeground(over ? Color.RED : Color.BLACK);
    }

    private void showSelectedDay() {
        tableModel.setRowCount(0);
        DayPlan day = dayList.getSelectedValue();
        if (day == null) return;
        for (Destination d : day.getDestinations()) {
            tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getCategory(), d.getCost(), d.getRating()});
        }
    }

    public int getSelectedDayIndex() { return dayList.getSelectedIndex(); }

    public String getSelectedDestinationId() {
        int row = table.getSelectedRow();
        return row >= 0 ? table.getValueAt(row, 0).toString() : null;
    }

    public String getBudgetText() { return txtBudget.getText().trim(); }

    public void addAddDayListener(ActionListener l) { btnAddDay.addActionListener(l); }
    public void addRemoveDayListener(ActionListener l) { btnRemoveDay.addActionListener(l); }
    public void addRemoveDestListener(ActionListener l) { btnRemoveDest.addActionListener(l); }
    public void addAutoPlanListener(ActionListener l) { btnAutoPlan.addActionListener(l); }
    public void addExportListener(ActionListener l) { btnExport.addActionListener(l); }
}
