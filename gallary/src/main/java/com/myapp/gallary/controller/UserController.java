package com.myapp.gallary.controller;

import com.myapp.gallary.Entity.Folder;
import com.myapp.gallary.Entity.User;
import com.myapp.gallary.dto.RegistrationDTO;
import com.myapp.gallary.repository.FolderRepository;
import com.myapp.gallary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @GetMapping("/register")
    public String register(Model model, RegistrationDTO registrationDTO) {
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, User user, @ModelAttribute(name = "registrationDTO") RegistrationDTO registrationDTO, BindingResult result) {

        if (registrationDTO.getName().matches("^[a-zA-Z0-9]{3,}$")){
            user = new User();
            user.setName(registrationDTO.getName().trim());
            user.setPassword(new BCryptPasswordEncoder().encode(registrationDTO.getPassword()));
            user.setEnabled(true);

            Folder rootFolder = new Folder();
            rootFolder.setName("root");
            rootFolder.setParentFolder(null);

            user.setRootFolder(rootFolder);
            folderRepository.save(rootFolder);
            userRepository.save(user);


        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


}
