package com.javarush.TagMe.controllers;

import com.javarush.TagMe.dto.UserDTO;
import com.javarush.TagMe.model.User;
import com.javarush.TagMe.services.UserService;
import com.javarush.TagMe.util.UserNotCreatedException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public MenuController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @GetMapping
    public String mainMethod(Model model){
        if (model.containsAttribute("successMessage")) {
            model.addAttribute("showSuccessMessage", true);
        } else {
            model.addAttribute("showSuccessMessage", false);
        }
        return "users/menu";
    }


    @GetMapping("/registration")
    public String newUser(@ModelAttribute("user") UserDTO userDTO ){
        return "users/registration";
    }


    @PostMapping()
    public String createUser(@ModelAttribute ("userDTO")@Valid UserDTO userDTO, BindingResult bindingResult,
                             Model model, RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }

        if (!userService.save(convertToUser(userDTO))){
            model.addAttribute("emailError","Пользователь с таким email уже существует");
        }

        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно зарегистрирован");

        return "redirect:/menu";
    }

    private User convertToUser(UserDTO userDTO ) {
        return modelMapper.map(userDTO, User.class);}

    private UserDTO convertToUserDTO(User user){
        return modelMapper.map(user,UserDTO.class);
    }
}
