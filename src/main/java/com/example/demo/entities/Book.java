package com.example.demo.entities;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.*;
import java.io.Serializable;


@Data
@Builder
public class Book implements Serializable {
    private static final long serialVersionUID = 1236376441137036293L;

    @Id
    private String id;
    private String isbn;
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 100, message = "Name length invalid")
    private String name;
    @NotEmpty(message = "Plot cannot be empty")
    @Size(min = 3, max = 200, message = "Plot length invalid")
    private String plot;
    @NotEmpty(message = "Author cannot be empty")
    @Size(min = 3, max = 50, message = "Author length invalid")
    private String author;
    @NotEmpty(message = "Genre cannot be empty")
    @Size(min = 3, max = 100, message = "Genre length invalid")
    private String genre;
    private boolean isAvailable;

}
