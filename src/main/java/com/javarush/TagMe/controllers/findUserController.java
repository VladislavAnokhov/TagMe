package com.javarush.TagMe.controllers;

import com.javarush.TagMe.model.User;
import com.javarush.TagMe.services.TagService;
import com.javarush.TagMe.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/find")
public class findUserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TagService tagService;
    @Autowired
    public findUserController(UserService userService, ModelMapper modelMapper, TagService tagService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.tagService = tagService;
    }



    @GetMapping("/random")
    public String findRandom (Model model, HttpSession session,
    @RequestParam(value = "page",required = false,defaultValue = "1") int page,
    @RequestParam(value = "limit",required = false,defaultValue = "5") int limit,
    @RequestParam(value = "reset", required = false, defaultValue = "false") boolean reset
    ){

        if (reset) {
            session.removeAttribute("randomUserIds"); // Сброс списка пользователей
        }
        List<User> users = userService.initializeOrGetRandomUsersPage(page, limit);
        Map<Integer, Integer> userAges = new HashMap<>(); // Карта для хранения возраста пользователей
        for (User user : users) {
            userAges.put(user.getId(), calculateAge(user));
        }

        model.addAttribute("users",users);
        model.addAttribute("userAges", userAges); // Передаем карту возрастов в модель
        model.addAttribute("current_page",page);
        int totalPages = (int) Math.ceil(1.0* userService.getAllCount()/ limit);
        List<Integer> pageNumbers= null;
        if(totalPages>1)
            pageNumbers= IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        model.addAttribute("page_numbers",pageNumbers);


        return "users/findRandom";
    }

    @GetMapping("/byFilter")
    public String findByFilter(Model model,
                               @RequestParam(value = "page",required = false,defaultValue = "1") int page, // Spring Data JPA страницы начинаются с 0
                               @RequestParam(value = "limit",required = false,defaultValue = "5") int limit,
                               @RequestParam Optional<String> gender,
                               @RequestParam Optional<String> tag
    ){
        int adjustedPage = page - 1;
        if (gender.equals("")){
            System.out.println("замена гендера");
            gender=null;
        }
        if (tag.equals("")){
            System.out.println("замена тега");
            tag=null;
        }
        Page<User> usersPage = userService.findAllWithFilters(adjustedPage, limit, gender.orElse(null),tag.orElse(null)); // Предполагается, что метод возвращает Page<User>
        List<User> users = usersPage.getContent();
        List<String> distinctTags = userService.findDistinctTags();
        Map<Integer, Integer> userAges = new HashMap<>(); // Карта для хранения возраста пользователей
        for (User user : users) {
            // Рассчитываем возраст для каждого пользователя и сохраняем его в карту
            userAges.put(user.getId(), calculateAge(user));
        }

        model.addAttribute("userAges", userAges); // Передаем карту возрастов в модель
        model.addAttribute("gender", gender);
        model.addAttribute("tag", tag);
        model.addAttribute("users", users);
        model.addAttribute("current_page", page ); // Пользовательская нумерация страниц начинается с 1
        model.addAttribute("distinctTags", distinctTags);
        model.addAttribute("total_pages", usersPage.getTotalPages());
        model.addAttribute("total_items", usersPage.getTotalElements());

        // Создание списка номеров страниц для навигации
        int totalPages = usersPage.getTotalPages();
        if(totalPages > 1){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        return "users/findByFilter";
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
