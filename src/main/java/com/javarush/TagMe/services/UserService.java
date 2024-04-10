package com.javarush.TagMe.services;

import com.javarush.TagMe.dto.UserDTO;
import com.javarush.TagMe.model.Role;
import com.javarush.TagMe.model.User;
import com.javarush.TagMe.repositories.RoleRepository;
import com.javarush.TagMe.repositories.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    final UsersRepository usersRepository;
     final HttpSession httpSession;
     final BCryptPasswordEncoder bCryptPasswordEncoder;
     final RoleRepository roleRepository;

    public UserService(UsersRepository usersRepository, HttpSession httpSession, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.httpSession = httpSession;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }
    @Transactional
    public void delete(int id){
        usersRepository.deleteById(id);
    }
    public User findById(int id){
        return usersRepository.findById(id).orElse(null);
    }
    public int getAllCount(){
        return (int) usersRepository.count();
    }
    public List<User> findAll(){
        return usersRepository.findAll();
    }


    public List<User> findByPage(int page,int itemsPerPage){
        List<User> userList = usersRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
        return userList;
    }



    public Page<User> findAllWithFilters(int page, int itemsPerPage, String gender, String tag) {

        Pageable pageable = PageRequest.of(page, itemsPerPage);
        if (gender != null && tag != null) {
            return usersRepository.findByGenderAndTag(gender, tag, pageable);
        } else if (gender != null) {
            return usersRepository.findByGender(gender, pageable);
        } else if (tag != null) {
            return usersRepository.findByTag(tag, pageable);
        } else {
            return usersRepository.findAll(pageable);
        }
    }

    public List<String> findDistinctTags() {
        return usersRepository.findDistinctTags();
    }

    public List<User> initializeOrGetRandomUsersPage(int page, int itemsPerPage) {
        List<Integer> userIds = (List<Integer>) httpSession.getAttribute("randomUserIds");
        if (userIds == null) {
            userIds = usersRepository.findAll().stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
            Collections.shuffle(userIds); //рандом по айди пользователей с помощью коллекции
            httpSession.setAttribute("randomUserIds", userIds);
        }

        int start = (page - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, userIds.size());
        List<Integer> pageUserIds = userIds.subList(start, end);

        return pageUserIds.stream()
                .map(id -> usersRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    public void upDate(int id,User user){
        user.setId(id);
        user.setUpdatedAt(LocalDateTime.now());
        usersRepository.save(user);
    }

    @Transactional
    public boolean save(User user){
        User userFromBD = usersRepository.findByEmail(user.getEmail());

        if (userFromBD != null){
            return false;
        }
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRoles(Collections.singleton(userRole));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        enrichUser(user);
        usersRepository.save(user);
        return true;
    }
    private void enrichUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedWho("visitor");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usersRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }
}
