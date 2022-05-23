package com.example.springmongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "cars")
public class Cars {

    @Id
    private int id;
    private String name;
    private int quantity;
    private int precio;
}
