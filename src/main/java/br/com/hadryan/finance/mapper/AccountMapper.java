package br.com.hadryan.finance.mapper;

import br.com.hadryan.finance.mapper.dto.request.account.AccountPostRequest;
import br.com.hadryan.finance.mapper.dto.request.account.AccountPutRequest;
import br.com.hadryan.finance.mapper.dto.response.account.AccountGetResponse;
import br.com.hadryan.finance.mapper.dto.response.account.AccountPostResponse;
import br.com.hadryan.finance.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Account postToAccount(AccountPostRequest accountPostRequest);

    @Mapping(target = "createdAt", ignore = true)
    Account putToAccount(AccountPutRequest accountPutRequest);

    AccountPostResponse accountToPostResponse(Account account);

    AccountGetResponse accountToGetResponse(Account account);

}
