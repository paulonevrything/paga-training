package com.apposit.training.video.rental.service;

import com.apposit.training.video.rental.model.Video;
import com.apposit.training.video.rental.model.VideoTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService{
    @Override
    public double calculatePrice(Video video, int days) {

        double rate = getRate(video.getVideoTypeId());
        double price;

        switch (video.getVideoTypeId()) {
            case REGULAR:
                price = days * rate;
                break;
            case CHILDRENS_MOVIE:
                price = (days * rate) + (video.getMaximumAge()/2);
            case NEW_RELEASE:
                price = (days * rate) - video.getYearReleased();
            default:
                price = 0;
        }

        return price;
    }

    private double getRate(VideoTypeEnum videoType) {
        double rate = 0;
        switch (videoType) {
            case REGULAR:
                rate = 10;
                break;
            case CHILDRENS_MOVIE:
                rate = 8;
                break;
            case NEW_RELEASE:
                rate = 15;
                break;
            default:
                break;
        }
        return rate;
    }
}
