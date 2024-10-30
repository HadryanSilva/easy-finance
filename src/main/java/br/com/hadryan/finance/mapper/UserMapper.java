package br.com.hadryan.finance.mapper;

import br.com.hadryan.finance.mapper.dto.request.UserPostRequest;
import br.com.hadryan.finance.mapper.dto.request.UserPutRequest;
import br.com.hadryan.finance.mapper.dto.response.UserGetResponse;
import br.com.hadryan.finance.mapper.dto.response.UserPostResponse;
import br.com.hadryan.finance.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User postToUser(UserPostRequest userPostRequest);

    User putToUser(UserPutRequest userPutRequest);

    UserPostResponse userToPostResponse(User user);

    UserGetResponse userToGetResponse(User user);

}
