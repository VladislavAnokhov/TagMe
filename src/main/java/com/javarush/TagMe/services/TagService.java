package com.javarush.TagMe.services;

import com.javarush.TagMe.model.Tag;
import com.javarush.TagMe.model.User;
import com.javarush.TagMe.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;
    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    public List<Tag> findDistinctTags(){
        return tagRepository.findDistinctTags();
    }
    @Transactional
    public void delete(int tagId){
        tagRepository.deleteById(tagId);
    }
    @Transactional
    public void save(String string, User user){
        Tag tag = new Tag();
        tag.setTag(string);
        tag.setUser(user);
        tagRepository.save(tag);
    }

}
