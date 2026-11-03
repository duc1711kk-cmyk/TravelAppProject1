package com.travelapp.model;

import java.util.ArrayList;
import java.util.List;

/** Lịch trình du lịch gồm nhiều ngày (DayPlan). */
public class Itinerary {
    private String name;
    private List<DayPlan> days;

    public Itinerary(String name) {
        this.name = name;
        this.days = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<DayPlan> getDays() { return days; }

    public DayPlan addDay() {
        DayPlan day = new DayPlan(days.size() + 1);
        days.add(day);
        return day;
    }

    public void removeDay(int index) {
        // TODO: Nhóm tự cài đặt xóa ngày và đánh số lại các ngày còn lại.
    }

    public double getTotalCost() {
        // TODO: Nhóm tự cài đặt tính tổng chi phí toàn bộ lịch trình.
        return 0;
    }

    /** Tìm xem địa điểm đã nằm trong ngày nào chưa (trả về số ngày, hoặc -1). */
    public int findDayContaining(Destination dest) {
        // TODO: Nhóm tự cài đặt tìm ngày đang chứa địa điểm.
        return -1;
    }
}
