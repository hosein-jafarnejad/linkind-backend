package com.hosmos.linkind.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
@EnableWebMvc
//@ComponentScan("ir.projects.learner.controllers")
public class MvcConfigurations extends WebMvcConfigurerAdapter {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.parameterName("type")
                .favorParameter(true)
                .defaultContentType(MediaType.APPLICATION_JSON_UTF8)
                .ignoreAcceptHeader(true)

                // Path extensions
                .mediaType("json", MediaType.APPLICATION_JSON_UTF8)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .favorPathExtension(true)
                .ignoreUnknownPathExtensions(true)
                .useRegisteredExtensionsOnly(true);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Json converter
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(jsonConverter);

        // String message converter
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        // XML converter
        MarshallingHttpMessageConverter marshallingHttpMessageConverter = new MarshallingHttpMessageConverter();
        XStreamMarshaller xStreamer = new XStreamMarshaller();
        marshallingHttpMessageConverter.setMarshaller(xStreamer);
        marshallingHttpMessageConverter.setUnmarshaller(xStreamer);
        converters.add(marshallingHttpMessageConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

//    @Bean
//    public InternalResourceViewResolver resourceViewResolver() {
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix("/WEB-INF/views/");
//        resolver.setSuffix(".jsp");
//        resolver.setContentType("text/html:charset=UTF-8");
//        resolver.setViewClass(JstlView.class);
//        return resolver;
//    }

}