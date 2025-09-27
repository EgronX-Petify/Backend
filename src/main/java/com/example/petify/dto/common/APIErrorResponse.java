package com.example.petify.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIErrorResponse {
    private int status;
    private String message;

    private List<FieldError> errors;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }

    public void addError(String field, String message) {
        if (this.errors == null) {
            errors = new ArrayList<>();
        }
        this.errors.add(new FieldError(field, message));
    }

}
