package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.repositary.UserRepositary;


@Service
public class UserService {

    @Autowired
    UserRepositary userRepository;

    public String addUser(User user) {
        userRepository.save(user);
        return "User added";
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean validateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && password != null && password.equals(user.getPassword());
    }

    public String getRole(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? user.getRole() : null;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

	public List<User> getAllUsers() {
		
		return userRepository.findAll();
	}

	public User getUserByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}
}
