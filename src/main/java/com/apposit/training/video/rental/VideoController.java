package com.apposit.training.video.rental;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by uduke on 2017/03/06.
 */
@Controller
public class VideoController {

    @RequestMapping("/")
    public String home(Model model){

        model.addAttribute("owner", "Umoh's");

        return "index";
    }
}
