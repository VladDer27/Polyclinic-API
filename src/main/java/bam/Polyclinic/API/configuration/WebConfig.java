package bam.Polyclinic.API.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS to all endpoints
                .allowedOrigins("http://31.128.39.88:3000") // Allow this origin to access the endpoints
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowable methods
                .allowedHeaders("*")// Allow all headers
                .allowCredentials(true); // Allow credentials
    }
}
