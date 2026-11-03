package com.travelapp;

import com.travelapp.ui.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Sử dụng invokeLater để đảm bảo giao diện Swing chạy an toàn trên luồng đồ họa
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true); // Lệnh này giúp cửa sổ hiện lên màn hình
        });
    }
}