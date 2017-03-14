package com.apposit.training.video.rental.services;

/**
 * Created by rketema on 3/14/17.
 */
public interface UserService {

    /**
     * Save User
     * @param firstName
     * @param lastName
     * @param Username
     */
     public void saveUser(String firstName, String lastName, String Username);
}
