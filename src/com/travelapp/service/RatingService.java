package com.travelapp.service;

import com.travelapp.data.DestinationManager;
import com.travelapp.model.Destination;

/**
 * Cho phép người dùng chấm điểm địa điểm (1-5 sao).
 * Điểm mới được gộp vào điểm trung bình hiện tại theo số lượt đánh giá.
 */
public class RatingService {
    private final DestinationManager destManager;

    public RatingService(DestinationManager destManager) {
        this.destManager = destManager;
    }

    /** @return điểm trung bình mới, hoặc -1 nếu điểm đầu vào không hợp lệ */
    public double rate(Destination dest, double stars) {
        // TODO: Nhóm tự cài đặt kiểm tra và cập nhật điểm đánh giá.
        return -1;
    }
}
