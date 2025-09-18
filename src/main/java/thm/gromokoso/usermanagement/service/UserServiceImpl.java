package thm.gromokoso.usermanagement.service;

import thm.gromokoso.usermanagement.entity.User;
import thm.gromokoso.usermanagement.model.GroupWithGroupRole;
import thm.gromokoso.usermanagement.repository.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) { this.userRepository = userRepository; }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> fetchUserList() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findById(userName).orElse(null);
    }

    @Override
    public void deleteUserByUserName(String userName) {
        userRepository.deleteById(userName);
    }

    @Override
    public void addApiToUser(String userName, int api_id) {
        /* TODO implement when repo is function is available */
    }

    @Override
    public List<Integer> fetchApiListFromUser(String userName) {
        /* TODO implement when repo is function is available */
        return List.of();
    }

    @Override
    public void deleteApiIdFromUser(String userName, int api_id) {
        /* TODO implement when repo is function is available */
    }

    @Override
    public List<GroupWithGroupRole> fetchGroupListFromUser(String userName) {
        /* TODO implement when repo is function is available */
        return List.of();
    }
}
