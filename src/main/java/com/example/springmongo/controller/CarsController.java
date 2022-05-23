package com.example.springmongo.controller;

import com.example.springmongo.model.Cars;
import com.example.springmongo.repositories.CarsDao;
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

    CarsDao carsDao;

    @Autowired
    public CarsController(CarsDao carsDao) {
        this.carsDao = carsDao;
    }

    public List<Cars> getAllCars() {
        return carsDao.findAll();
    }

    public void addAllCars(List<Cars> cars) {
        carsDao.saveAll(cars);
    }

    public void añadir(Cars cars){
        if (!carsDao.existsById(cars.getId())){
            carsDao.save(cars);
        }
    }

    public Cars getCars(int id){
        return carsDao.findById(id).get();
    }

    public void actualizarTodo(List<Cars> cars){
        for (Cars p : getAllCars()){
            for (Cars pp: cars){
                if (p.getId() == pp.getId()){
                    p.setName(pp.getName());
                    p.setPrecio(pp.getPrecio());
                    p.setQuantity(pp.getQuantity());
                    carsDao.save(p);
                }
            }
        }
    }

    public void deleteById(int id){
        carsDao.deleteById(id);
    }

    public void actualizar(int id, Cars cars) {
        Cars real = getCars(id);

        real.setName(cars.getName());
        real.setPrecio(cars.getPrecio());
        real.setQuantity(cars.getQuantity());

        carsDao.save(real);
    }

    public void patchCars(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Cars cars = getCars(id);
        Cars carsPatched = applyPatch(patch, cars);

        carsDao.save(carsPatched);

    }

    private Cars applyPatch(JsonPatch patch, Cars targetCars) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetCars, JsonNode.class));
        return objectMapper.treeToValue(patched, Cars.class);
    }

}
