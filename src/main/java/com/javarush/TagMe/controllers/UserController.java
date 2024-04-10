package com.javarush.TagMe.controllers;

import com.javarush.TagMe.dto.UserDTO;
import com.javarush.TagMe.model.Photo;
import com.javarush.TagMe.model.Tag;
import com.javarush.TagMe.model.User;
import com.javarush.TagMe.services.PhotoService;
import com.javarush.TagMe.services.TagService;
import com.javarush.TagMe.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final TagService tagService;
    private final ModelMapper modelMapper;
    private final PhotoService photoService;

    @Autowired
    public UserController(UserService userService, TagService tagService, ModelMapper modelMapper, PhotoService photoService) {
        this.userService = userService;
        this.tagService = tagService;
        this.modelMapper = modelMapper;
        this.photoService = photoService;
    }

    @GetMapping("/{id}")
    public String getUserPage(@PathVariable("id") int id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user );
        model.addAttribute("age",calculateAge(user));
        return "users/user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.id")
    @GetMapping("/{id}/edit")
    public String edit(Model model,@PathVariable("id") int id){
        model.addAttribute("user",userService.findById(id));
        return "users/edit";
    }

    @PostMapping("/{id}")
    public String update (@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") int id){
        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
            });
            return  "users/edit";}
        userService.upDate(id,user);
        return "redirect:/user/{id}";
    }

    @PostMapping("/{id}/deleteUser")
    public String deleteUser(@PathVariable("id") int id,RedirectAttributes redirectAttributes){
        userService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно удален");
        return "redirect:/menu";
    }

    @PostMapping("/{id}/upload/photo")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Выберите файл для загрузки");
                return "redirect:/edit";
            }
            photoService.save(file, userService.findById(id));
            redirectAttributes.addFlashAttribute("message", "Фотография '" + fileName + "' успешно загружена");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Ошибка при загрузке файла '" + fileName + "'");
        }
        return "redirect:/user/{id}";
    }

    @PostMapping("{id}/deletePhoto/{photoId}")
    public String deletePhoto(@PathVariable("id")int id, @PathVariable("photoId") int photoId) {
        photoService.delete(userService.findById(id),photoId);
        return "redirect:/user/{id}";
    }

    @PostMapping("{id}/deleteTag/{tagId}")
    public String deleteTag(@PathVariable("id")int id, @PathVariable("tagId")int taqId){
        tagService.delete(taqId);
        return  "redirect:/user/{id}";
    }
    @PostMapping("{id}/saveTag")
    public String saveTag(@PathVariable("id")int id, @RequestParam("tag")String tag){
        tagService.save(tag, userService.findById(id));
        return "redirect:/user/{id}";
    }

    private int calculateAge(User user){
        Calendar birthDateCal = Calendar.getInstance();
        birthDateCal.setTime(user.getDate());
        Calendar currentDateCal = Calendar.getInstance();
        int age = currentDateCal.get(Calendar.YEAR) - birthDateCal.get(Calendar.YEAR);
        if (currentDateCal.get(Calendar.DAY_OF_YEAR) < birthDateCal.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}



