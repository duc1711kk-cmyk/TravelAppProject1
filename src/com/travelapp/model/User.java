package com.travelapp.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    // Sử dụng List để lưu danh sách các địa điểm yêu thích
    private List<Destination> favoriteDestinations;

    public User(String username) {
        this.username = username;
        // Khởi tạo danh sách rỗng khi tạo user mới
        this.favoriteDestinations = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public List<Destination> getFavoriteDestinations() { return favoriteDestinations; }

    // Hàm thêm địa điểm vào danh sách yêu thích
    public void addFavorite(Destination dest) {
        // TODO: Nhóm tự cài đặt thêm địa điểm yêu thích, tránh trùng lặp.
    }

    // Hàm xóa địa điểm khỏi danh sách yêu thích
    public void removeFavorite(Destination dest) {
        // TODO: Nhóm tự cài đặt xóa địa điểm khỏi danh sách yêu thích.
    }
}