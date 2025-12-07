package com.sigepu.sigepu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeamos la URL "/imagenes/**" a la carpeta física "uploads" en la raíz del proyecto
        // "file:uploads/" indica que es una ruta en el sistema de archivos, no dentro del JAR.
        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations("file:uploads/");
    }
}