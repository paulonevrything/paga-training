package com.apposit.training.video.rental.services;

import com.apposit.training.video.rental.data.DAO;
import com.apposit.training.video.rental.model.User;

/**
 * Created by rketema on 3/14/17.
 */
public class UserServiceImpl implements UserService {

    DAO dao;

    @Override
    public void saveUser(String firstName, String lastName, String Username) {

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(Username);

        dao.insert(user);
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }
}
