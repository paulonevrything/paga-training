package com.apposit.training.video.rental;

import com.apposit.training.video.rental.model.Credentials;
import com.apposit.training.video.rental.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
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
    public String home(Model model){

        model.addAttribute("owner", "Umoh's");

        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Credentials credentials) {

        String loggedInUser = loginService.login(credentials.getUsername());

        Model model = new ExtendedModelMap();
        model.addAttribute("user", loggedInUser);

        return "index";
    }
}
