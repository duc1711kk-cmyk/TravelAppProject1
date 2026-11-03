package com.travelapp.model;

import java.util.ArrayList;
import java.util.List;

/** Kế hoạch của một ngày trong lịch trình: gồm danh sách các địa điểm sẽ đi. */
public class DayPlan {
    private int dayNumber;
    private List<Destination> destinations;

    public DayPlan(int dayNumber) {
        this.dayNumber = dayNumber;
        this.destinations = new ArrayList<>();
    }

    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }
    public List<Destination> getDestinations() { return destinations; }

    public boolean addDestination(Destination dest) {
        // TODO: Nhóm tự cài đặt thêm địa điểm, không cho phép trùng lặp.
        return false;
    }

    public void removeDestination(Destination dest) {
        // TODO: Nhóm tự cài đặt xóa địa điểm khỏi ngày.
    }

    public double getTotalCost() {
        // TODO: Nhóm tự cài đặt tính tổng chi phí của ngày.
        return 0;
    }

    @Override
    public String toString() {
        return "Ngày " + dayNumber + " (" + destinations.size() + " địa điểm)";
    }
}
