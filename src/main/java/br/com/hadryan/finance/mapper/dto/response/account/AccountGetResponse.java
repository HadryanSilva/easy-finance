package br.com.hadryan.finance.mapper.dto.response.account;

import br.com.hadryan.finance.model.enums.AccountType;
import br.com.hadryan.finance.model.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AccountGetResponse {

    private Long id;

    private String name;

    private AccountType type;

    private Double balance;

    private Currency currency;

    private LocalDateTime createdAt;

}
