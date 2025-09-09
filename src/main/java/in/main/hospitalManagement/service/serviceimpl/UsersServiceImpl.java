package in.main.hospitalManagement.service.serviceimpl;

import in.main.hospitalManagement.entity.Users;
import in.main.hospitalManagement.repository.UsersRepository;
import in.main.hospitalManagement.service.UsersService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    @Override
    public Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
    }

    public Users saveUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
        return usersRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }


}

