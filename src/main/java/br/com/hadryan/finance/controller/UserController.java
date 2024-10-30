package br.com.hadryan.finance.controller;

import br.com.hadryan.finance.mapper.dto.request.UserPostRequest;
import br.com.hadryan.finance.mapper.dto.request.UserPutRequest;
import br.com.hadryan.finance.mapper.dto.response.UserGetResponse;
import br.com.hadryan.finance.mapper.dto.response.UserPostResponse;
import br.com.hadryan.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody UserPostRequest request) {
        var user = userService.save(request);
        return ResponseEntity.created(URI.create("/api/v1/users/" + user.getId()))
                .body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody UserPutRequest request, @PathVariable Long id) {
        userService.update(request, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
