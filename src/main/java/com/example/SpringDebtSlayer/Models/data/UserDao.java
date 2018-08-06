package com.example.SpringDebtSlayer.Models.data;

import com.example.SpringDebtSlayer.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}
