package thm.gromokoso.usermanagement.controller;

import org.springframework.web.bind.annotation.*;
import thm.gromokoso.usermanagement.entity.Group;

import java.util.List;

public class GroupManagementController {

    @GetMapping("/groups")
    public Group getGroups() { /* TODO implement correctly */ return null; }

    @PostMapping("/groups")
    public void addGroup(@RequestBody Group group) { /* TODO implement correctly */ }

    @GetMapping("/groups/{id}")
    public Group getGroup(@PathVariable int id) { /* TODO implement correctly */ return null; }

    @PutMapping("/groups/{id}")
    public void updateGroup(@PathVariable int id, @RequestBody Group group) { /* TODO implement correctly */ }

    @DeleteMapping("/groups/{id}")
    public void deleteGroup(@PathVariable int id) { /* TODO implement correctly */ }

    @GetMapping("/groups/{id}/apis")
    public List<Integer> getApis(@PathVariable int id) { /* TODO implement correctly */ return null; }

    @PostMapping("/groups/{id}/apis")
    public void addApis(@PathVariable int id, @RequestBody Integer api_id) { /* TODO implement correctly */ }

    @DeleteMapping("/groups/{id}/apis/{api_id}")
    public void deleteApi(@PathVariable int id, @PathVariable int api_id) { /* TODO implement correctly */ }

    @GetMapping("/groups/{id}/users")
    public List<Integer> getUsers(@PathVariable int id) { /* TODO implement correctly */ return null; }

    @PostMapping("/groups/{id}/users")
    public void addUser(@PathVariable int id, @RequestBody Integer user_id) { /* TODO implement correctly */ }

    @DeleteMapping("/groups/{id}/users/{user_id}")
    public void deleteUser(@PathVariable int id, @PathVariable int user_id) { /* TODO implement correctly */ }
}
