package com.travelapp.ui;

import com.travelapp.data.UserManager;
import com.travelapp.model.Destination;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FavoritePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private UserManager userManager;
    private JButton btnRefresh;
    
    // Giao diện (interface) báo hiệu việc bấm nút xóa
    private DeleteClickListener deleteListener;

    public interface DeleteClickListener {
        void onDeleteClicked(String destinationId);
    }

    public void setDeleteClickListener(DeleteClickListener listener) {
        this.deleteListener = listener;
    }

    public FavoritePanel(UserManager userManager) {
        this.userManager = userManager;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("❤️ Các địa điểm bạn đã lưu:"));
        btnRefresh = new JButton("Làm mới danh sách");
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);

        // Thêm cột "Hành động" vào bảng
        String[] columnNames = {"ID", "Tên địa điểm", "Danh mục", "Chi phí (VND)", "Đánh giá (*)", "Hành động"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Chỉ cho phép bấm vào cột Hành động
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Nạp công cụ vẽ và bấm nút cho cột Hành động
        table.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        table.getColumn("Hành động").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });
    }

    public void refreshTable() {
        tableModel.setRowCount(0); 
        List<Destination> favorites = userManager.getCurrentUser().getFavoriteDestinations();
        for (Destination dest : favorites) {
            Object[] row = {
                dest.getId(), dest.getName(), dest.getCategory(), dest.getCost(), dest.getRating(), "❌ Xóa"
            };
            tableModel.addRow(row);
        }
    }

    // --- Các lớp hỗ trợ vẽ nút bấm ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(new Color(255, 102, 102)); // Màu đỏ nhạt
            setForeground(Color.WHITE);
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
            button.setBackground(new Color(255, 102, 102));
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getSelectedRow();
                if(row >= 0) {
                    String destId = table.getValueAt(row, 0).toString();
                    if (deleteListener != null) {
                        deleteListener.onDeleteClicked(destId);
                    }
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