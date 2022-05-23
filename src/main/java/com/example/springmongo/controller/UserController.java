package com.example.springmongo.controller;

import com.example.springmongo.model.Cars;
import com.example.springmongo.model.User;
import com.example.springmongo.repositories.UserDao;
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
    UserDao userDao;
    CarsController carsController;

    @Autowired
    public UserController(UserDao userDao, CarsController carsController) {
        this.userDao = userDao;
        this.carsController = carsController;
    }

    public List<User> getAllUsers() {

        for (User u: userDao.findAll()){
            actualizarProducto(u);
        }

        return userDao.findAll();
    }

    public User getUser(int id) {
        User user = userDao.findById(id).get();

        actualizarProducto(user);

        return userDao.findById(id).get();
    }

    private void actualizarProducto(User user) {
        for (Cars p : user.getCars()){
            for (Cars pp: carsController.getAllCars()){
                if (carsController.carsDao.existsById(p.getId())){
                    if (p.getId() == pp.getId()) {
                        p.setName(pp.getName());
                        p.setPrecio(pp.getPrecio());
                        p.setQuantity(pp.getQuantity());
                        userDao.save(user);
                    }
                }else{
                    int i = user.getCars().indexOf(p);
                    user.getCars().remove(i);
                    userDao.save(user);
                }
            }
        }


    }

    public void addUser(User user) {
        List<Cars> cars = user.getCars();
        carsController.addAllCars(cars);

        userDao.save(user);
    }

    public void deleteUser(int id) {
        User user = getUser(id);
        userDao.delete(user);
    }

    public void putUser(User user, int id) {

        User real = getUser(id);
        real.setPassword(user.getPassword());
        real.setName(user.getName());

        List<Cars> cars = user.getCars();
        for (Cars p: real.getCars()){
            for (Cars pp: cars){
                if (p.getId() == pp.getId()){
                    p.setName(pp.getName());
                    p.setPrecio(pp.getPrecio());
                    p.setQuantity(pp.getQuantity());
                }
            }
        }
        carsController.actualizarTodo(user.getCars());

        userDao.save(real);
    }

    public void patchUser(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        User user = getUser(id);
        User userPatched = applyPatch(patch, user);

        if (user.getCars().size() == userPatched.getCars().size()){
            carsController.actualizarTodo(userPatched.getCars());
        }else if (user.getCars().size() < userPatched.getCars().size()){
            carsController.addAllCars(userPatched.getCars());
        }
        userDao.save(userPatched);

    }

    private User applyPatch(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

    public void addProduct(Cars cars, int id) {
        User u = getUser(id);
        carsController.añadir(cars);
        Cars p = carsController.getCars(cars.getId());

        boolean encontrar = false;
        for (Cars pp : u.getCars()){
            if (pp.getId() == p.getId()) {
                encontrar = true;
                break;
            }
        }
        if (!encontrar){
            u.addProduct(p);
        }
        userDao.save(u);
    }

    public void deleteProductOnUser(int id, int index) {
        User u = getUser(id);
        u.getCars().remove(index);
        userDao.save(u);
    }

}