package com.example.springmongo.controller;

import com.example.springmongo.model.Cars;
import com.example.springmongo.model.User;
import com.example.springmongo.repositories.UserDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;


@Controller
public class UserController {
    UserDAO userDAO;
    CarsController carsController;
    @Autowired
    public UserController(UserDAO userDAO, CarsController carsController) {
        this.userDAO = userDAO;
        this.carsController = carsController;
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public User getUser(int id) {
        return userDAO.findById(id).get();
    }

    public void addUser(User user) {
        List<Cars> cars = user.getCars();
        carsController.addAllCars(cars);
        userDAO.save(user);
    }

    public void deleteUser(int id) {
        User user = getUser(id);
        userDAO.delete(user);
    }

    public void putUser(User user, int id) {

        User real = getUser(id);
        // real.setName(user.getName());
        real.setName(user.getName());
        real.setPassword(user.getPassword());
        //real.setEmail(user.getEmail());


        List<Cars> animals = user.getCars();
        for (Cars p: real.getCars()){
            for (Cars pp: animals){
                if (p.getId() == pp.getId()){
                    p.setName(pp.getName());
                    p.setPrecio(pp.getPrecio());
                    //p.setQuantity(pp.getQuantity());
                }
            }
        }
        carsController.actualizarTodo(user.getCars());

        userDAO.save(real);
    }

    public void patchUser(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        User user = getUser(id);
        User userPatched = applyPatch(patch, user);

        if (user.getCars().size() == userPatched.getCars().size()){
            carsController.actualizarTodo(userPatched.getCars());
        }else if (user.getCars().size() < userPatched.getCars().size()){
            carsController.addAllCars(userPatched.getCars());
        }

        userDAO.save(userPatched);

    }

    private User applyPatch(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

    public void addCars(Cars cars, int id) {
        User u = getUser(id);
        carsController.añadir(cars);
        Cars c = carsController.getCars(cars.getId());
        boolean encontrar = false;
        for (Cars cc : u.getCars()){
            if (cc.getId() == c.getId()) {
                encontrar = true;
                break;
            }
        }
        if (!encontrar){
            u.addCars(c);
        }
        userDAO.save(u);
    }

    public void deleteCarsOnUser(int id, int index) {
        User u = getUser(id);
        u.getCars().remove(index);
        userDAO.save(u);
    }

}