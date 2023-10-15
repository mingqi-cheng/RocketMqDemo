package org.example.mapper;

import org.example.pojo.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Service;


public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
}
