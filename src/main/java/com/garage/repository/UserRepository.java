package com.garage.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.garage.entity.User;
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
 User findByEmail(String email);
 Optional<User> findByEmailAndActive(String email,byte active);
 User findByMobileNumber(String mobileNumber);
 User findByIdAndActive(int id,byte active);
}
