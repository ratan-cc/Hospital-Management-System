package in.main.hospitalmanagement.service;

import in.main.hospitalmanagement.entity.Users;

import java.util.List;
import java.util.Optional;


public interface UsersService {
    Optional<Users> findByUsername(String username);

    Users saveUser(Users user);

    List<Users> getAllUsers();
}
