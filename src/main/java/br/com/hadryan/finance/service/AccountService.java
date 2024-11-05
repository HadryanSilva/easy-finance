package br.com.hadryan.finance.service;

import br.com.hadryan.finance.exception.NotFoundException;
import br.com.hadryan.finance.mapper.AccountMapper;
import br.com.hadryan.finance.mapper.dto.request.account.AccountPostRequest;
import br.com.hadryan.finance.mapper.dto.request.account.AccountPutRequest;
import br.com.hadryan.finance.mapper.dto.response.account.AccountGetResponse;
import br.com.hadryan.finance.mapper.dto.response.account.AccountPostResponse;
import br.com.hadryan.finance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public List<AccountGetResponse> findAllByUser(Long userId) {
        log.info("Finding all accounts by user id: {}", userId);
        return accountRepository.findAllByUserId(userId)
                .stream()
                .map(accountMapper::accountToGetResponse)
                .toList();
    }

    public AccountGetResponse findById(Long id) {
        log.info("Finding account by id: {}", id);
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return accountMapper.accountToGetResponse(account);

    }

    public AccountPostResponse save(AccountPostRequest request) {
        log.info("Saving account: {}", request.getName());
        var accountToSave = accountMapper.postToAccount(request);
        accountToSave.setCreatedAt(LocalDateTime.now());
        var savedAccount = accountRepository.save(accountToSave);
        log.info("Account saved successfully");
        return accountMapper.accountToPostResponse(savedAccount);
    }

    public void update(Long id, AccountPutRequest request) {
        log.info("Updating account with id: {}", id);
        var accountFound = accountRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Account not found"));
        if (request.getName() != null) {
            accountFound.setName(request.getName());
        }
        if (request.getType() != null) {
            accountFound.setType(request.getType());
        }
        if (request.getBalance() != null) {
            accountFound.setBalance(accountFound.getBalance() + request.getBalance());
        }
        if (request.getCurrency() != null) {
            accountFound.setCurrency(request.getCurrency());
        }

        accountRepository.save(accountFound);
        log.info("Account updated successfully");
    }

    public void delete(Long id) {
        log.info("Deleting account by id: {}", id);
        var accountToDelete = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        accountRepository.delete(accountToDelete);
    }

}
