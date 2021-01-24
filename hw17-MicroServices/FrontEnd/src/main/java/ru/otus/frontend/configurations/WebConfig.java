package ru.otus.frontend.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Это победа!!! Так работает!
        // https://stackoverflow.com/questions/58346138/spring-boot-spring-mvc-webjars-dependencies-return-404
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/")
                .resourceChain(false);
        registry.setOrder(1);
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/*");
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    // TODO: Не работает
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
}
