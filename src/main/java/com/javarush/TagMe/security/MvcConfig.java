package com.javarush.TagMe.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
@Override
    public void addViewControllers(ViewControllerRegistry registry){
    registry.addViewController("/login").setViewName("login/login");
}
@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/"); // Указывает на папку в файловой системе
    }

}
