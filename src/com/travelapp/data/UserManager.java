package com.travelapp.data;

import com.travelapp.model.DayPlan;
import com.travelapp.model.Destination;
import com.travelapp.model.Itinerary;
import com.travelapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý người dùng hiện tại, danh sách yêu thích và lịch trình.
 * Dữ liệu được lưu vào user_data.txt với định dạng:
 *   FAV,D01
 *   ITIN,Tên lịch trình
 *   DAY,1,D01;D03
 */
public class UserManager {
    private User currentUser;
    private Itinerary itinerary;
    private final DestinationManager destManager;
    private final String filePath;

    public UserManager(DestinationManager destManager, String filePath) {
        this.destManager = destManager;
        this.filePath = filePath;
        this.currentUser = new User("Sinh Viên");
        this.itinerary = new Itinerary("Chuyến đi của tôi");
        load();
    }

    public User getCurrentUser() { return currentUser; }
    public Itinerary getItinerary() { return itinerary; }

    /** Thêm vào yêu thích. Trả về false nếu đã có từ trước. */
    public boolean toggleFavorite(Destination dest) {
        // TODO: Nhóm tự cài đặt thêm địa điểm vào yêu thích và lưu dữ liệu.
        return false;
    }

    public void removeFavoriteById(String destId) {
        // TODO: Nhóm tự cài đặt xóa địa điểm yêu thích theo ID và lưu dữ liệu.
    }

    public void save() {
        // TODO: Nhóm tự cài đặt lưu danh sách yêu thích và lịch trình.
    }

    private void load() {
        // TODO: Nhóm tự cài đặt đọc dữ liệu người dùng và khôi phục lịch trình.
    }
}
