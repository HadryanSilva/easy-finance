package br.com.hadryan.finance.service;

import br.com.hadryan.finance.exception.EmailAlreadyRegisteredException;
import br.com.hadryan.finance.mapper.UserMapper;
import br.com.hadryan.finance.mapper.dto.request.UserPostRequest;
import br.com.hadryan.finance.mapper.dto.request.UserPutRequest;
import br.com.hadryan.finance.mapper.dto.response.UserGetResponse;
import br.com.hadryan.finance.mapper.dto.response.UserPostResponse;
import br.com.hadryan.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserGetResponse findById(Long id) {
        log.info("Finding user by id: {}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.userToGetResponse(user);
    }

    public UserPostResponse save(UserPostRequest request) {
        log.info("Saving user: {}", request);
        var user = userMapper.postToUser(request);
        user.setCreatedAt(LocalDateTime.now());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("Email already registered");
        }
        var savedUser = userRepository.save(user);
        return userMapper.userToPostResponse(savedUser);
    }

    public void update(UserPutRequest request, Long id) {
        log.info("Updating user: {}", request);
        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userFound.setUpdatedAt(LocalDateTime.now());
        userFound.setUsername(request.getUsername());
        userFound.setPassword(request.getPassword());
        userRepository.save(userFound);
    }

    public void delete(Long id) {
        log.info("Deleting user with id: {}", id);
        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(userFound);
    }

}
