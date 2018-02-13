package ru.captive.config;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebApplicationConfig extends WebMvcConfigurerAdapter {

    private static final String NOT_FOUND = "/notFound";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/notFound").setViewName("redirect:/");
    }


    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container ->
                container.addErrorPages(
                        new ErrorPage(HttpStatus.NOT_FOUND, NOT_FOUND),
                        new ErrorPage(HttpStatus.BAD_REQUEST, NOT_FOUND)
                );
    }

}