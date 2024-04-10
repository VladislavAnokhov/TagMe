package com.javarush.TagMe.controllers;

import com.javarush.TagMe.model.User;
import com.javarush.TagMe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping()
public class AdminController {
    private final UserService userService;
    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

   @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String userList(@RequestParam(value = "page",required = false,defaultValue = "1") int page,
                           @RequestParam(value = "limit",required = false,defaultValue = "10") int limit,
                           Model model){
       int adjustedPage = page - 1;
       List<User> users = userService.findByPage(adjustedPage,limit);
       System.out.println(users.size() + " сколько пользователей");
       Map<Integer, Integer> userAges = new HashMap<>();
       for (User user : users) {
           userAges.put(user.getId(), calculateAge(user));
       }
       model.addAttribute("users",users );
       model.addAttribute("userAges", userAges); // Передаем карту возрастов в модель
       model.addAttribute("current_page",page);

       int totalPages = (int) Math.ceil(1.0* userService.getAllCount()/ limit);
       List<Integer> pageNumbers= null;
       if(totalPages>1)
           pageNumbers= IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
       model.addAttribute("page_numbers",pageNumbers);
    return "admin/admin";
    }

    @PostMapping("/admin")
    public String deleteUser(@RequestParam(required = true,defaultValue = "") int id,
                             @RequestParam(required = true,defaultValue = "") String action,
                             Model model){
        if (action.equals("delete")){
            userService.delete(id);
        }
        return "redirect:/admin";
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

}
