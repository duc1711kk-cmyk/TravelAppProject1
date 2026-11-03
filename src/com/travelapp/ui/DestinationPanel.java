package com.travelapp.ui;

import com.travelapp.model.Destination;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DestinationPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRate;
    private JButton btnAddToItinerary;
    // Khai báo một giao diện (interface) để báo cho MainFrame biết khi nào nút được bấm
    private FavoriteClickListener favoriteListener;

    public interface FavoriteClickListener {
        void onFavoriteClicked(String destinationId);
    }

    public void setFavoriteClickListener(FavoriteClickListener listener) {
        this.favoriteListener = listener;
    }

    public DestinationPanel() {
        setLayout(new BorderLayout());

        // Thêm cột "Hành động"
        String[] columnNames = {"ID", "Tên địa điểm", "Danh mục", "Chi phí (VND)", "Đánh giá (*)", "Hành động"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép "tương tác" (nhấn) vào cột Hành động (cột cuối cùng)
                return column == 5;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Thiết lập vẽ nút và xử lý bấm nút cho cột "Hành động"
        table.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        table.getColumn("Hành động").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Thanh nút thao tác trên dòng đang chọn
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        btnRate = new JButton("⭐ Đánh giá");
        btnAddToItinerary = new JButton("📅 Thêm vào lịch trình");
        bottom.add(btnRate);
        bottom.add(btnAddToItinerary);
        add(bottom, BorderLayout.SOUTH);
    }

    public String getSelectedDestinationId() {
        int row = table.getSelectedRow();
        return row >= 0 ? table.getValueAt(row, 0).toString() : null;
    }

    public void addRateListener(ActionListener l) { btnRate.addActionListener(l); }
    public void addItineraryListener(ActionListener l) { btnAddToItinerary.addActionListener(l); }

    public void loadDataToTable(List<Destination> destinations) {
        tableModel.setRowCount(0);
        for (Destination dest : destinations) {
            Object[] row = {
                dest.getId(), dest.getName(), dest.getCategory(), dest.getCost(), dest.getRating(), "❤️ Yêu thích"
            };
            tableModel.addRow(row);
        }
    }

    // --- Các lớp hỗ trợ vẽ nút bấm trong bảng ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(Color.PINK);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            button.setBackground(Color.PINK);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                // Khi nút được bấm, lấy ID của địa điểm ở dòng đó
                int row = table.getSelectedRow();
                String destId = table.getValueAt(row, 0).toString();
                // Báo cho MainFrame biết
                if (favoriteListener != null) {
                    favoriteListener.onFavoriteClicked(destId);
                }
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}