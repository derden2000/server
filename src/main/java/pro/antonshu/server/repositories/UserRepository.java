package pro.antonshu.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.antonshu.server.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByLogin(String login);
}
