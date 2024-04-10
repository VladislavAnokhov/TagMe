package com.javarush.TagMe.dto;

import com.javarush.TagMe.model.Contact;
import com.javarush.TagMe.model.Photo;
import com.javarush.TagMe.model.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String name;

    private String password;

    @Email
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String gender;
    private String confirmPassword;
    private String description;
    private List<Tag> tagsList;
    private List<Photo> photoList;
    private List<Contact> contactList;

}
