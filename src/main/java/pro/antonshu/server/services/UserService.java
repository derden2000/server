package pro.antonshu.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pro.antonshu.server.entities.User;
import pro.antonshu.server.repositories.UserRepository;

//@Service
public interface UserService extends UserDetailsService {

    User findOneByLogin(String login);

    User save(User user);

    User getUser(User user);
}
