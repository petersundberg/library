package com.example.demo.entities.validation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityError {
    private String field;
    private String message;
    private String rejectedValue;


}
