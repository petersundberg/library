package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "userCache")
    public List<User> findAll(String name, boolean sortOnLastname) {
        log.info("Request to find all users.");
        log.warn("Fresh data ...");
        //return userRepository.findAll();
        var users = userRepository.findAll();
        if(name != null) {
            users = users.stream()
                    .filter(user -> user.getFirstName().startsWith(name) ||
                            user.getLastName().startsWith(name))
                    .collect(Collectors.toList());
        }
        if(sortOnLastname){
            //users.sort((user1, user2) -> user1.getLastname().compareTo(user2.getLastname()));
            users.sort(Comparator.comparing(User::getLastName));
        }
        return users;
    }

    @Cacheable(value = "userCache", key = "#id")
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, //404 -> Not found
                        String.format("Could not find the user by id %s", id)));

    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Could not find user by username %s", username))));
    }

    @CachePut(value = "userCache", key = "#result.id")
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @CachePut(value = "userCache", key = "#id")
    public void update(String id, User user) {
        if(!userRepository.existsById(id)) {
            log.error(String.format("Could not find the user by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 -> Not found
                    String.format("Could not find the user by id %s", id));
        }
        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

                /*user.setId(id);
        user.setPhone("tel." + user.setPhone();         */

/*        user = User.builder()
                .id(id)
                .phone("tel:" + user.getPhone())
                .build();*/
        userRepository.save(user);
    }

    @CacheEvict(value = "userCache", key = "#id")
    public void delete(String id) {
        if(!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 -> Not found
                    String.format("Could not find the user by id %s", id));
        }
        userRepository.deleteById(id);
    }



}
