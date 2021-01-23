package com.apposit.training.video.rental.service;

import com.apposit.training.video.rental.data.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apposit.training.video.rental.model.Video;

import java.util.*;

@Service
public class VideoService {

    @Autowired
    private DAO dao;

    public List<Video> getVideos() {

        Video video = new Video();

        List<Video> videos = (List<Video>)(List<?>) dao.find(video);

        return videos;
    }

    public double getPrice() {

        return 0;
    }
}
