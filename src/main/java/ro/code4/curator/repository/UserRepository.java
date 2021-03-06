package ro.code4.curator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.code4.curator.entity.User;

import java.io.Serializable;

/**
 * Created on 4/20/17.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Serializable> {

    User findByUsername(String username);
}
