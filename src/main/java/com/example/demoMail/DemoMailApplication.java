package com.example.demoMail;

import com.example.demoMail.filestorage.FileStorage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class DemoMailApplication implements CommandLineRunner {

	@Resource
	FileStorage fileStorage;

	public static void main(String[] args) throws Exception{
		SpringApplication.run(DemoMailApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		fileStorage.deleteAll();
		fileStorage.init();
	}
}
