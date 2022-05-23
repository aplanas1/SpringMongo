package com.example.springmongo.controller;

import com.example.springmongo.model.Cars;
import com.example.springmongo.repositories.CarsDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CarsController {
    CarsDAO carsDAO;


    @Autowired
    public CarsController(CarsDAO animalDAO) {
        this.carsDAO = animalDAO;
    }

    public List<Cars> getAllCars() {
        return carsDAO.findAll();
    }

    public void addAllCars(List<Cars> cars) {
        carsDAO.saveAll(cars);
    }

    public void añadir(Cars cars){
        if (!carsDAO.existsById(cars.getId())){
            carsDAO.save(cars);
        }
    }

    public Cars getCars(int id){
        return carsDAO.findById(id).get();
    }

    public void deleteById(int id){
        carsDAO.deleteById(id);
    }

    public void actualizar(int id, Cars animal) {
        Cars real = getCars(id);

        real.setName(animal.getName());
        real.setPrecio(animal.getPrecio());
        //real.setQuantity(animal.getQuantity());

        carsDAO.save(real);
    }

    public void actualizarTodo(List<Cars> cars){
        for (Cars c : getAllCars()){
            for (Cars cc: cars){
                if (c.getId() == cc.getId()){
                    c.setName(cc.getName());
                    c.setPrecio(cc.getPrecio());
                    //p.setQuantity(pp.getQuantity());
                    carsDAO.save(c);
                }
            }
        }
    }

    public void patchCars(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Cars animal = getCars(id);
        Cars animalPatched = applyPatch(patch, animal);

        carsDAO.save(animalPatched);

    }

    private Cars applyPatch(JsonPatch patch, Cars targetAnimal) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetAnimal, JsonNode.class));
        return objectMapper.treeToValue(patched, Cars.class);
    }

}
