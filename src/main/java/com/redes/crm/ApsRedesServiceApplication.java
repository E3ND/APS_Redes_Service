package com.redes.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class ApsRedesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApsRedesServiceApplication.class, args);
	}

}
