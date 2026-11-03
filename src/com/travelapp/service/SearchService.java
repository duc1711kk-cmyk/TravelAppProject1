package com.travelapp.service;

import com.travelapp.model.Destination;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    // 1. Hàm tìm kiếm theo tên (Có sử dụng chứa từ khóa, không phân biệt hoa thường)
    public List<Destination> searchByName(List<Destination> allDestinations, String keyword) {
        // TODO: Nhóm tự cài đặt tìm kiếm địa điểm theo tên.
        return new ArrayList<>();
    }

    // 2. Hàm lọc theo danh mục (Ví dụ: "Thiên nhiên", "Nghỉ dưỡng")
    public List<Destination> filterByCategory(List<Destination> allDestinations, String category) {
        // TODO: Nhóm tự cài đặt lọc địa điểm theo danh mục.
        return new ArrayList<>();
    }
}