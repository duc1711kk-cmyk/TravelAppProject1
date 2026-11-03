package com.travelapp.model; // Dòng này khai báo "hộ khẩu" của class

public class Destination {
    // 1. Thuộc tính (Attributes) - Sử dụng tính Đóng gói (Encapsulation)
    private String id;
    private String name;
    private String category;
    private double cost;
    private double rating;

    // 2. Hàm khởi tạo (Constructor) - Khuôn đúc đối tượng
    public Destination(String id, String name, String category, double cost, double rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.rating = rating;
    }

    // 3. Các hàm lấy dữ liệu (Getters)
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getCost() { return cost; }
    public double getRating() { return rating; }

    // 4. Các hàm sửa dữ liệu (Setters)
    public void setRating(double rating) {
        // Lợi ích của việc dùng private: Ta có thể kiểm soát dữ liệu đầu vào
        if (rating >= 0 && rating <= 5) {
            this.rating = rating;
        } else {
            System.out.println("Lỗi: Điểm đánh giá chỉ từ 0 đến 5!");
        }
    }

    // 5. Hàm hiển thị thông tin nhanh
    @Override
    public String toString() {
        return name + " (" + category + ") - Giá: " + cost;
    }
}