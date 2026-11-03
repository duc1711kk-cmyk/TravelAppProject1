package com.travelapp.data;

import com.travelapp.model.Destination;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DestinationManager {
    private List<Destination> allDestinations;
    // Số lượt đánh giá của từng địa điểm (cột thứ 6 trong file, mặc định 1 nếu file cũ không có)
    private Map<String, Integer> ratingCounts;
    private String filePath;

    public DestinationManager() {
        this.allDestinations = new ArrayList<>();
        this.ratingCounts = new HashMap<>();
    }

    public void loadDestinationsFromFile(String filePath) {
        // TODO: Nhóm tự cài đặt đọc, phân tích dữ liệu và tạo danh sách Destination.
        this.filePath = filePath;
        allDestinations.clear();
        ratingCounts.clear();
    }

    /** Ghi lại danh sách địa điểm (kèm điểm đánh giá mới) ra file. */
    public void saveDestinationsToFile() {
        // TODO: Nhóm tự cài đặt chuyển danh sách địa điểm thành các dòng và lưu xuống file.
    }

    public List<Destination> getAllDestinations() { return allDestinations; }

    public Destination findById(String id) {
        for (Destination d : allDestinations) {
            if (d.getId().equals(id)) return d;
        }
        return null;
    }

    public int getRatingCount(String id) { return ratingCounts.getOrDefault(id, 1); }
    public void setRatingCount(String id, int count) { ratingCounts.put(id, count); }
}
