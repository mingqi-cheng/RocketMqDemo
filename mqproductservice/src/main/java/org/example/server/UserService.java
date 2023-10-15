package org.example.server;

import org.example.mapper.UserRepository;
import org.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public Flux<User> findAll() {
        return userRepository.findAll();
    }
    public Mono<User> addUser(User user) {
        return userRepository.save(user);
    }

    public Mono<ResponseEntity<Void>> delUser(int id) {
        return userRepository.findById(id)
                .flatMap(user -> userRepository.delete(user).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<User>> updateUser(User user) {
        return userRepository.findById(user.getId())
                .flatMap(user0 -> userRepository.save(user))
                .map(user0 -> new ResponseEntity<User>(user0, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<User>(HttpStatus.NOT_FOUND));
    }
    public Mono<User> getById(int id) {
        return userRepository.findById(id);
    }

    public Flux<User> getAllUser() {
        return userRepository.findAll();
    }

}
