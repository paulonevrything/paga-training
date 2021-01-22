package com.apposit.training.video.rental.service;

import com.apposit.training.video.rental.data.DAO;
import com.apposit.training.video.rental.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService{

    @Autowired
    private DAO dao;

    @Override
    public List<Video> getVideos() {

        return null;
    }

    @Override
    public double getPrice() {
        return 0;
    }

}
