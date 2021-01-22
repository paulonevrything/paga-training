package com.apposit.training.video.rental;

import com.apposit.training.video.rental.model.Credentials;
import com.apposit.training.video.rental.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by uduke on 2017/03/06.
 */
@Controller
public class VideoController {

    private LoginService loginService;

    @Autowired
    public VideoController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping("/")
    public String home(){
        return "index";
    }

    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public String login(Credentials credentials, Model model) {

        String loggedInUser = loginService.login(credentials.getUsername());
        System.out.println("loggedInUser = " + loggedInUser);

        model.addAttribute("user", loggedInUser);

        return "videos";
    }


    @RequestMapping(value = "/video/{user}/{id}", method = RequestMethod.GET)
    public String video(@PathVariable String user, @PathVariable int id, Model model) {
        if(user == null) {
            return "index";
        }

        System.out.println("Video id = " + id);
        model.addAttribute("user", user);
        return "video";
    }
}
