package com.hanabank.repository;

import com.hanabank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query(value="SELECT b FROM Account b WHERE b.email= :email" )
    Account findByEmail(@Param("email")String email);

    @Query(value="SELECT b from Account b")
    List<Account> findAll();

    @Query(value="SELECT b FROM Account b WHERE b.id= :id" )
    Account findById(@Param ("id")int id);

//    @Query(value="SELECT b CASE WHEN  WHERE b.email= :email THEN TRUE ELSE FALSE FROM Account b" )
//    boolean existByEmail(String email);
}
