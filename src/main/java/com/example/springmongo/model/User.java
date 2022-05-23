package com.example.springmongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document(collection = "users")
public class User {

    @Id
    private int id;

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";
    private String name;
    private String password;

    private List<Cars> cars;

    public void addProduct(Cars cars){
        this.cars.add(cars);
    }

}
