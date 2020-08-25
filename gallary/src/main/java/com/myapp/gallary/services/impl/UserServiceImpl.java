package com.myapp.gallary.services.impl;

import com.myapp.gallary.Entity.User;
import com.myapp.gallary.repository.UserRepository;
import com.myapp.gallary.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository siteUserRepository;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = siteUserRepository.findByName(authentication.getName());
        return user.get();
    }
}
