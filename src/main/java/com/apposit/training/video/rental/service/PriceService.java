package com.apposit.training.video.rental.service;

import com.apposit.training.video.rental.model.Video;

public interface PriceService {
    public double calculatePrice(Video video, int days);
}
