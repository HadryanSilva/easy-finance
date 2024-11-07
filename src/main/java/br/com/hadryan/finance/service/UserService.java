package br.com.hadryan.finance.service;

import br.com.hadryan.finance.exception.EmailAlreadyRegisteredException;
import br.com.hadryan.finance.exception.NotFoundException;
import br.com.hadryan.finance.mapper.UserMapper;
import br.com.hadryan.finance.mapper.dto.request.user.UserPostRequest;
import br.com.hadryan.finance.mapper.dto.request.user.UserPutRequest;
import br.com.hadryan.finance.mapper.dto.response.user.UserGetResponse;
import br.com.hadryan.finance.mapper.dto.response.user.UserPostResponse;
import br.com.hadryan.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.userToGetResponse(user);
    }

    public UserPostResponse save(UserPostRequest request) {
        log.info("Saving user: {}", request.getUsername());
        var user = userMapper.postToUser(request);
        user.setCreatedAt(LocalDateTime.now());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("Email already registered");
        }
        var savedUser = userRepository.save(user);
        return userMapper.userToPostResponse(savedUser);
    }

    public void update(UserPutRequest request, Long id) {
        log.info("Updating user: {}", request.getUsername());
        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userFound.setUpdatedAt(LocalDateTime.now());

        if (request.getPassword() != null) {
            userFound.setPassword(request.getPassword());
        }

        if (request.getEmail() != null) {
            userFound.setEmail(request.getEmail());
        }

        if (request.getUsername() != null) {
            userFound.setUsername(request.getUsername());
        }

        userRepository.save(userFound);
    }

    public void delete(Long id) {
        log.info("Deleting user with id: {}", id);
        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(userFound);
    }

}
