package com.apposit.training.video.rental;

import com.apposit.training.video.rental.model.Credentials;
import com.apposit.training.video.rental.service.LoginService;
import com.apposit.training.video.rental.service.PriceService;
import com.apposit.training.video.rental.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.apposit.training.video.rental.model.Video;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by uduke on 2017/03/06.
 */
@Controller
public class VideoController {


    private LoginService loginService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private PriceService priceService;

    @Autowired
    public VideoController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping("/")
    public String home(){
        return "index";
    }

    @RequestMapping(value = "/videos", method = RequestMethod.GET)
    public String login(Credentials credentials, Model model) {

        String loggedInUser = loginService.login(credentials.getUsername());
        System.out.println("loggedInUser = " + loggedInUser);

        List<Video> allVideos = videoService.getVideos();

        model.addAttribute("user", loggedInUser);
        model.addAttribute("allVideos", allVideos);

        return "videos";
    }

    @RequestMapping(value = "/video/{user}/{id}/", method = RequestMethod.GET)
    public String video(@PathVariable String user, @PathVariable int id, @RequestParam int days, Model model) {
        if(user == null || id == 0) {
            return "index";
        }
        if(days < 1) {
            return "videos";
        }

        Video video = videoService.getVideo(id);
        model.addAttribute("user", user);
        model.addAttribute("video", video);
        model.addAttribute("rentalDays", days);

        double price = priceService.calculatePrice(video, days);
        model.addAttribute("price", price);

        return "video";
    }
}
