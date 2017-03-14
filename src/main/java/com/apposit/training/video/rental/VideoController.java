package com.apposit.training.video.rental;

import com.apposit.training.video.rental.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by uduke on 2017/03/06.
 */
@Controller
public class VideoController {

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String home(Model model){

        model.addAttribute("owner", "Umoh's");

        return "index";
    }
}
