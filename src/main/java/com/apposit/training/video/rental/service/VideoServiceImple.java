package com.apposit.training.video.rental.service;

import com.apposit.training.video.rental.data.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apposit.training.video.rental.model.Video;

import java.util.*;

 class VideoServices {

    @Autowired
    private DAO dao;

    public List<Video> getVideos() {

        Video video = new Video();

        List<Video> videos = (List<Video>)(List<?>) dao.find(video);

        return videos;
    }

    public Video getVideo(int id) {
        Video vid = (Video) dao.get(Video.class, id, Locale.ENGLISH);

        return vid;
    }

    public double getPrice() {

        return 0;
    }
}
