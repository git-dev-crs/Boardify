package com.boardify.board_service.feign;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Response;
import feign.Util;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class CustomDecoder extends SpringDecoder {

    private final ObjectMapper objectMapper;

    public CustomDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (response.body() == null) return null;

        String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));

        JavaType javaType = objectMapper.getTypeFactory().constructType(type);
        return objectMapper.readValue(body, javaType);
    }
}
