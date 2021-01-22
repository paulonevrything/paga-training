package com.apposit.training.video.rental.service;

import com.apposit.training.video.rental.model.Video;

import java.util.List;

public interface VideoService {
    public List<Video> getVideos();
    public double getPrice();
}
