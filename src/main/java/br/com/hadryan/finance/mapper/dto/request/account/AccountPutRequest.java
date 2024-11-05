package br.com.hadryan.finance.mapper.dto.request.account;

import br.com.hadryan.finance.model.enums.AccountType;
import br.com.hadryan.finance.model.enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountPutRequest {

    private Long id;

    private String name;

    private AccountType type;

    private Double balance;

    private Currency currency;

}
