package in.main.hospitalManagement.service;

import in.main.hospitalManagement.entity.Users;

import java.util.List;
import java.util.Optional;


public interface UsersService {
    Optional<Users> findByUsername(String username);

    Users saveUser(Users user);

    List<Users> getAllUsers();
}
