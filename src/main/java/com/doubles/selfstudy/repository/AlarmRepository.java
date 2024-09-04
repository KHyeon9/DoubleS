package com.doubles.selfstudy.repository;

import com.doubles.selfstudy.entity.Alarm;
import com.doubles.selfstudy.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Page<Alarm> findAllByUserAccount(UserAccount userAccount, Pageable pageable);
}
