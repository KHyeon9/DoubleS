package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.Todo;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByUserAccount(UserAccount userAccount);
}
