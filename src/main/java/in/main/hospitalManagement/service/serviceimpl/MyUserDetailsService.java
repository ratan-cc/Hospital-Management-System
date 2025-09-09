package in.main.hospitalManagement.service.serviceimpl;

import in.main.hospitalManagement.entity.UserPrincipal;
import in.main.hospitalManagement.entity.Users;
import in.main.hospitalManagement.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public MyUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> users = usersRepository.findByUsername(username);
        if (users.isEmpty()) {
            System.out.println("User 404");
            throw new UsernameNotFoundException("User 404");
        }
        return new UserPrincipal(users.orElse(null));
    }

}

