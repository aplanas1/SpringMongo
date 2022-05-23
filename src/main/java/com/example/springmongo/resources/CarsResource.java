package com.example.springmongo.resources;

import com.example.springmongo.controller.CarsController;
import com.example.springmongo.model.Cars;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CarsResource.Product_RESOURCE)
public class CarsResource {
    public final static String Product_RESOURCE = "/cars";

    CarsController carsController;

    @Autowired
    public CarsResource(CarsController carsController) {
        this.carsController = carsController;
    }

    @GetMapping
    public List<Cars> cars(){
        return carsController.getAllCars();
    }

    @GetMapping("{id}")
    public Cars cars(@PathVariable("id") int id){
        return carsController.getCars(id);
    }

    @PostMapping
    public void addCars(@RequestBody Cars cars){
        carsController.añadir(cars);
    }

    @DeleteMapping("{id}")
    public void deleteCars(@PathVariable("id") int id){
        carsController.deleteById(id);
    }

    @PutMapping("{id}")
    public void putCars(@PathVariable("id") int id, @RequestBody Cars cars){
        carsController.actualizar(id, cars);
    }

    @PatchMapping("{id}")
    public void patchCars(@PathVariable("id") int id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        carsController.patchCars(id,patch);
    }
}
