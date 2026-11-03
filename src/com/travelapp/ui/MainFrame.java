package com.travelapp.ui;

import com.travelapp.data.DestinationManager;
import com.travelapp.data.UserManager;
import com.travelapp.model.Destination;
import com.travelapp.model.DayPlan;
import com.travelapp.service.BudgetService;
import com.travelapp.service.ItineraryService;
import com.travelapp.service.RatingService;
import com.travelapp.service.SearchService;
import com.travelapp.data.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class MainFrame extends JFrame {
    
    private DestinationPanel destinationPanel;
    private SearchPanel searchPanel;
    private BudgetPanel budgetPanel; 
    private FavoritePanel favoritePanel; 
    private ItineraryPanel itineraryPanel;
    private RatingService ratingService;
    private ItineraryService itineraryService;

    private DestinationManager destManager;
    private SearchService searchService;
    private BudgetService budgetService; 
    private UserManager userManager;

    public MainFrame() {
        setTitle("TravelApp - Ứng Dụng Quản Lý Du Lịch");
        setSize(1000, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout()); 

        // 1. Khởi tạo Data và Service
        destManager = new DestinationManager();
        destManager.loadDestinationsFromFile("resources/data/destinations.txt");
        searchService = new SearchService();
        budgetService = new BudgetService(); 
        userManager = new UserManager(destManager, "resources/data/user_data.txt");
        ratingService = new RatingService(destManager);
        itineraryService = new ItineraryService();

        // 2. Thiết lập hệ thống Tab (JTabbedPane)
        JTabbedPane tabbedPane = new JTabbedPane();

        // --- TAB 1: Khám Phá ---
        JPanel exploreTab = new JPanel(new BorderLayout());
        searchPanel = new SearchPanel();
        destinationPanel = new DestinationPanel();
        exploreTab.add(searchPanel, BorderLayout.NORTH);
        exploreTab.add(destinationPanel, BorderLayout.CENTER);
        
        // --- TAB 2: Ngân Sách ---
        budgetPanel = new BudgetPanel();

        // --- TAB 3: Yêu Thích ---
        favoritePanel = new FavoritePanel(userManager);

        // Thêm các Tab vào màn hình chính
        tabbedPane.addTab("🔍 Khám phá & Tìm kiếm", exploreTab);
        tabbedPane.addTab("💰 Gợi ý Ngân sách (Knapsack)", budgetPanel);
        tabbedPane.addTab("❤️ Mục Yêu Thích", favoritePanel);

        // --- TAB 4: Lịch trình ---
        itineraryPanel = new ItineraryPanel();
        itineraryPanel.refresh(userManager.getItinerary(), 0);
        tabbedPane.addTab("📅 Lịch trình", itineraryPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Nạp dữ liệu ban đầu cho bảng
        List<Destination> allDest = destManager.getAllDestinations();
        destinationPanel.loadDataToTable(allDest);

        // 3. Kết nối sự kiện
        setupActions();
    }

    private void setupActions() {
        // --- 1. Chức năng Tìm kiếm ---
        searchPanel.addSearchListener(e -> applyFilters());

        // --- 2. Chức năng Gợi ý Ngân sách ---
        budgetPanel.addSuggestListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String budgetStr = budgetPanel.getBudgetText();
                    if(budgetStr.isEmpty()) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Vui lòng nhập số tiền!");
                        return;
                    }
                    double budget = Double.parseDouble(budgetStr);
                    List<Destination> allDest = destManager.getAllDestinations();
                    List<Destination> suggested = budgetService.suggestKnapsack(allDest, budget);
                    
                    StringBuilder sb = new StringBuilder();
                    sb.append("Với ngân sách ").append(String.format("%,.0f", budget)).append(" VND, đây là lựa chọn tối ưu nhất:\n");
                    sb.append("--------------------------------------------------\n");
                    
                    double totalCost = 0;
                    double totalRating = 0;
                    for(Destination d : suggested) {
                        sb.append(String.format("- %-20s | %-12s | Giá: %,10.0f VND | Sao: %.1f\n", 
                                  d.getName(), d.getCategory(), d.getCost(), d.getRating()));
                        totalCost += d.getCost();
                        totalRating += d.getRating();
                    }
                    
                    sb.append("--------------------------------------------------\n");
                    sb.append(String.format("Tổng chi phí: %,.0f VND\n", totalCost));
                    sb.append(String.format("Tổng điểm đánh giá (Max Value): %.1f\n", totalRating));
                    
                    budgetPanel.setResultText(sb.toString());

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Vui lòng chỉ nhập số (ví dụ: 3500000)!");
                }
            }
        });

        // --- 3. Chức năng Thêm Yêu Thích ---
        destinationPanel.setFavoriteClickListener(new DestinationPanel.FavoriteClickListener() {
            @Override
            public void onFavoriteClicked(String destinationId) {
                Destination selectedDest = null;
                for (Destination d : destManager.getAllDestinations()) {
                    if (d.getId().equals(destinationId)) {
                        selectedDest = d;
                        break;
                    }
                }
                if (selectedDest != null) {
                    boolean success = userManager.toggleFavorite(selectedDest);
                    if (success) {
                        JOptionPane.showMessageDialog(MainFrame.this, 
                            "Đã thêm " + selectedDest.getName() + " vào danh sách yêu thích!");
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this, 
                            selectedDest.getName() + " đã có trong danh sách yêu thích từ trước!", 
                            "Thông báo", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        // --- 4. SỰ KIỆN XÓA YÊU THÍCH (Đã sửa lỗi treo máy) ---
        favoritePanel.setDeleteClickListener(new FavoritePanel.DeleteClickListener() {
            @Override
            public void onDeleteClicked(String destinationId) {
                // Dùng SwingUtilities.invokeLater để tách luồng, giúp JTable thoát khỏi trạng thái editing trước khi mở bảng thông báo
                SwingUtilities.invokeLater(() -> {
                    // Hiển thị hộp thoại xác nhận
                    int confirm = JOptionPane.showConfirmDialog(MainFrame.this, 
                        "Bạn có chắc muốn xóa địa điểm này khỏi mục yêu thích?", 
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        userManager.removeFavoriteById(destinationId); // Xóa trong dữ liệu ngầm
                        favoritePanel.refreshTable(); // Cập nhật lại giao diện bảng ngay lập tức
                        JOptionPane.showMessageDialog(MainFrame.this, "Đã xóa thành công!");
                    }
                });
            }
        });
        
        // --- 6. Đánh giá địa điểm ---
        destinationPanel.addRateListener(e -> {
            Destination d = destManager.findById(String.valueOf(destinationPanel.getSelectedDestinationId()));
            if (d == null) {
                JOptionPane.showMessageDialog(this, "Hãy chọn một địa điểm trong bảng trước!");
                return;
            }
            Integer[] stars = {5, 4, 3, 2, 1};
            Integer choice = (Integer) JOptionPane.showInputDialog(this,
                    "Chấm điểm cho " + d.getName() + " (1-5 sao):", "Đánh giá",
                    JOptionPane.QUESTION_MESSAGE, null, stars, stars[0]);
            if (choice != null) {
                double newRating = ratingService.rate(d, choice);
                applyFilters();
                JOptionPane.showMessageDialog(this, String.format(
                        "Cảm ơn bạn! Điểm trung bình mới của %s: %.1f", d.getName(), newRating));
            }
        });

        // --- 7. Thêm địa điểm vào lịch trình ---
        destinationPanel.addItineraryListener(e -> {
            Destination d = destManager.findById(String.valueOf(destinationPanel.getSelectedDestinationId()));
            if (d == null) {
                JOptionPane.showMessageDialog(this, "Hãy chọn một địa điểm trong bảng trước!");
                return;
            }
            List<DayPlan> days = userManager.getItinerary().getDays();
            if (days.isEmpty()) {
                days.add(new DayPlan(1));
            }
            String[] options = new String[days.size()];
            for (int i = 0; i < options.length; i++) options[i] = "Ngày " + days.get(i).getDayNumber();
            String pick = (String) JOptionPane.showInputDialog(this, "Thêm \"" + d.getName() + "\" vào ngày nào?",
                    "Thêm vào lịch trình", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (pick == null) return;
            int dayIndex = java.util.Arrays.asList(options).indexOf(pick);
            String error = itineraryService.addToDay(userManager.getItinerary(), dayIndex, d);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Thông báo", JOptionPane.WARNING_MESSAGE);
            } else {
                userManager.save();
                JOptionPane.showMessageDialog(this, "Đã thêm " + d.getName() + " vào " + pick + "!");
            }
        });

        // --- 8. Tab Lịch trình ---
        itineraryPanel.addAddDayListener(e -> {
            userManager.getItinerary().addDay();
            userManager.save();
            refreshItinerary();
        });
        itineraryPanel.addRemoveDayListener(e -> {
            int idx = itineraryPanel.getSelectedDayIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Hãy chọn một ngày để xóa!");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Xóa ngày này khỏi lịch trình?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                userManager.getItinerary().removeDay(idx);
                userManager.save();
                refreshItinerary();
            }
        });
        itineraryPanel.addRemoveDestListener(e -> {
            int idx = itineraryPanel.getSelectedDayIndex();
            Destination d = destManager.findById(String.valueOf(itineraryPanel.getSelectedDestinationId()));
            if (idx < 0 || d == null) {
                JOptionPane.showMessageDialog(this, "Hãy chọn một địa điểm trong ngày để bỏ!");
                return;
            }
            userManager.getItinerary().getDays().get(idx).removeDestination(d);
            userManager.save();
            refreshItinerary();
        });
        itineraryPanel.addAutoPlanListener(e -> {
            double budget = getItineraryBudget();
            if (budget <= 0) {
                JOptionPane.showMessageDialog(this, "Nhập ngân sách (số dương) để tự lập lịch trình!");
                return;
            }
            List<Destination> suggested = budgetService.suggestKnapsack(destManager.getAllDestinations(), budget);
            if (suggested.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ngân sách quá thấp, không có địa điểm nào phù hợp.");
                return;
            }
            // Xếp theo đánh giá giảm dần, 2 địa điểm mỗi ngày
            suggested.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
            if (JOptionPane.showConfirmDialog(this, "Lịch trình hiện tại sẽ bị thay thế. Tiếp tục?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            itineraryService.autoDistribute(userManager.getItinerary(), suggested, 2);
            userManager.save();
            refreshItinerary();
        });
        itineraryPanel.addExportListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("lich_trinh.txt"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String text = itineraryService.exportText(userManager.getItinerary());
                boolean ok = FileHandler.writeFile(chooser.getSelectedFile().getPath(),
                        java.util.Arrays.asList(text.split("\n")));
                JOptionPane.showMessageDialog(this, ok ? "Đã xuất lịch trình!" : "Xuất file thất bại!");
            }
        });

        // --- 5. Tự động tải lại danh sách khi chuyển Tab ---
        JTabbedPane tabbedPane = (JTabbedPane) ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) { 
                favoritePanel.refreshTable();
            } else if (tabbedPane.getSelectedIndex() == 3) {
                refreshItinerary();
            }
        });
    }

    /** Áp dụng bộ lọc danh mục + từ khóa hiện tại lên bảng địa điểm. */
    private void applyFilters() {
        String keyword = searchPanel.getSearchKeyword();
        String category = searchPanel.getSelectedCategory();
        List<Destination> currentList = destManager.getAllDestinations();
        currentList = searchService.filterByCategory(currentList, category);
        if (!keyword.isEmpty()) {
            currentList = searchService.searchByName(currentList, keyword);
        }
        destinationPanel.loadDataToTable(currentList);
    }

    private double getItineraryBudget() {
        try {
            String t = itineraryPanel.getBudgetText();
            return t.isEmpty() ? 0 : Double.parseDouble(t);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void refreshItinerary() {
        itineraryPanel.refresh(userManager.getItinerary(), getItineraryBudget());
    }
}
