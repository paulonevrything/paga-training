package com.apposit.training.video.rental.service;

import com.apposit.training.video.rental.data.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apposit.training.video.rental.model.Video;

import java.util.*;

public interface VideoService {

    public List<Video> getVideos();

    public Video getVideo(int id);

}
