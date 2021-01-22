package com.apposit.training.video.rental.model;

public class Rental {
    private final VideoTypes type;
    private final double rate;


    public Rental(VideoTypes type, double rate) {
        this.type = type;
        this.rate = rate;
    }

    public VideoTypes getType() {
        return type;
    }

    public double getRate() {
        return rate;
    }
}
