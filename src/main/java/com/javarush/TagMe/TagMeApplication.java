package com.javarush.TagMe;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TagMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TagMeApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
 		return new ModelMapper();
	}
}
