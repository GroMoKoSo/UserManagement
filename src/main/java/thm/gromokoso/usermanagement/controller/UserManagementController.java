package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.entity.Group;
import thm.gromokoso.usermanagement.entity.User;

import java.util.List;

public class UserManagementController {

    @GetMapping("/users")
    public User getUsers() { /* TODO implement correctly */ return null; }

    @PostMapping("/users")
    public void addUser(@RequestBody User user) { /* TODO implement correctly */ }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) { /* TODO implement correctly */ return null; }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable int id, @RequestBody User user) { /* TODO implement correctly */ }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) { /* TODO implement correctly */ }

    @GetMapping("/users/{id}/apis")
    public List<Integer> getApis(@PathVariable int id) { /* TODO implement correctly */ return null; }

    @PostMapping("/users/{id}/apis")
    public void addApis(@PathVariable int id, @RequestBody Integer api_id) { /* TODO implement correctly */ }

    @DeleteMapping("/users/{id}/apis/{api_id}")
    public void addApi(@PathVariable int id, @PathVariable int api_id) { /* TODO implement correctly */ }

    @GetMapping("/users/{id}/groups")
    public List<Group> getGroups(@PathVariable int id) { /* TODO implement correctly */ return null; }
}
