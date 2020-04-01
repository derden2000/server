package pro.antonshu.server.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import pro.antonshu.server.entities.User;


public interface UserService extends UserDetailsService {

    User findOneByLogin(String login);

    User save(User user);

    User getUser(User user);
}
