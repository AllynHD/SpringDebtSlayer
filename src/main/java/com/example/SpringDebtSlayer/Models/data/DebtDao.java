package com.example.SpringDebtSlayer.Models.data;

import com.example.SpringDebtSlayer.Models.Debt;
import com.example.SpringDebtSlayer.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository
@Transactional
public interface DebtDao extends CrudRepository<Debt, Integer> {

    ArrayList<Debt> findByUser(User user);
}
