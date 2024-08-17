package com.prth.irest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prth.irest.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
