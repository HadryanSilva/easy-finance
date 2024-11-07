package br.com.hadryan.finance.controller;

import br.com.hadryan.finance.mapper.dto.request.account.AccountPostRequest;
import br.com.hadryan.finance.mapper.dto.request.account.AccountPutRequest;
import br.com.hadryan.finance.mapper.dto.response.account.AccountGetResponse;
import br.com.hadryan.finance.mapper.dto.response.account.AccountPostResponse;
import br.com.hadryan.finance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountGetResponse>> findAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.findAllByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountGetResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AccountPostResponse> save(@RequestBody AccountPostRequest request) {
        var accountSaved = accountService.save(request);
        return ResponseEntity.created(URI.create("/api/v1/accounts/" + accountSaved.getId()))
                .body(accountSaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody AccountPutRequest request) {
        accountService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
