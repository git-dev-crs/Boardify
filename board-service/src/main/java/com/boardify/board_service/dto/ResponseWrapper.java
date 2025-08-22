package com.boardify.board_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseWrapper<T> {
    private boolean success;
    private T data;
    private String error;

    public ResponseWrapper(){
    }

    public ResponseWrapper(T data){
        this.success = true;
        this.data = data;
        this.error = null;
    }
    public ResponseWrapper(String error){
        this.success = false;
        this.data = null;
        this.error = error;
    }

    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper<>(data);
    }

    public static <T> ResponseWrapper<T> failure(String error) {
        return new ResponseWrapper<>(error);
    }
}
