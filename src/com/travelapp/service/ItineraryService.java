package com.travelapp.service;

import com.travelapp.model.DayPlan;
import com.travelapp.model.Destination;
import com.travelapp.model.Itinerary;

import java.util.ArrayList;
import java.util.List;

public class ItineraryService {

    /** Thêm địa điểm vào một ngày. Trả về thông báo lỗi, hoặc null nếu thành công. */
    public String addToDay(Itinerary itinerary, int dayIndex, Destination dest) {
        // TODO: Nhóm tự cài đặt kiểm tra và thêm địa điểm vào ngày được chọn.
        return "Chức năng thêm địa điểm chưa được hoàn thiện.";
    }

    /** Kiểm tra tổng chi phí có vượt ngân sách không (budget <= 0 nghĩa là không giới hạn). */
    public boolean isOverBudget(Itinerary itinerary, double budget) {
        // TODO: Nhóm tự cài đặt kiểm tra tổng chi phí so với ngân sách.
        return false;
    }

    /**
     * Tự động chia danh sách địa điểm vào các ngày (mỗi ngày tối đa perDay địa điểm).
     * Dùng khi muốn đưa kết quả gợi ý Knapsack thành lịch trình.
     */
    public void autoDistribute(Itinerary itinerary, List<Destination> destinations, int perDay) {
        // TODO: Nhóm tự cài đặt thuật toán phân chia địa điểm vào các ngày.
        // Giữ nguyên lịch trình hiện tại cho đến khi chức năng được hoàn thiện.
    }

    /** Xuất lịch trình ra văn bản. */
    public String exportText(Itinerary itinerary) {
        // TODO: Nhóm tự cài đặt xuất lịch trình thành văn bản.
        return "Chức năng xuất lịch trình chưa được hoàn thiện.";
    }
}
