package com.prth.irest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IrestWorkbenchApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(IrestWorkbenchApplication.class, args);
	}
	
/*	@Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.registerModule(new JavaTimeModule()); // Register the module
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
    */

}
