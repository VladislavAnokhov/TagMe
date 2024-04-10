package com.javarush.TagMe.services;

import com.javarush.TagMe.model.Photo;
import com.javarush.TagMe.model.User;
import com.javarush.TagMe.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class PhotoService {
    private PhotoRepository photoRepository;
    private final Path rootLocation = Paths.get("uploads");

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать каталог для загрузки фотографий", e);
        }
        this.photoRepository = photoRepository;
    }
    @Transactional
    public void save(MultipartFile file, User user) throws IOException {
        String fileName = user.getName() + "_" + user.getEmail() + "_" + (user.getPhotoList().size() + 1) + ".jpg";
        Path destinationFile = rootLocation.resolve(fileName)
                .normalize().toAbsolutePath();
        file.transferTo(destinationFile);

        Photo photo = new Photo();
        photo.setCreatedTime(LocalDateTime.now());
        photo.setUser(user);
        photo.setFileLink(fileName);
        photoRepository.save(photo);
    }
    @Transactional
    public void delete(User user,int photoId){
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Фотография не найдена"));
        Path path = rootLocation.resolve(photo.getFileLink()).normalize();
        System.out.println(path);
        try {
            if (Files.exists(path)) {
                Files.delete(path);
                photoRepository.deleteById(photoId);
            } else {
                throw new RuntimeException("Файл не найден: " + path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при удалении файла: " + path, e);
        }
    }
}
