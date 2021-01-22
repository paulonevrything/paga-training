package com.apposit.training.video.rental.service;

import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService{
    @Override
    public String login(String username) {
        return username;
    }
}
