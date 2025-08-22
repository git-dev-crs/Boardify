package com.boardify.board_service.feign;


import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignClientConfig {
    @Bean
    public Decoder feignDecoder() {
        return new CustomDecoder(() -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter()));
    }
}
