package com.example.springmongo.repositories;

import com.example.springmongo.model.Cars;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarsDao extends MongoRepository<Cars, Integer> {
}
