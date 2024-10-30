package br.com.hadryan.finance.mapper;

import br.com.hadryan.finance.mapper.dto.request.UserPostRequest;
import br.com.hadryan.finance.mapper.dto.response.UserGetResponse;
import br.com.hadryan.finance.mapper.dto.response.UserPostResponse;
import br.com.hadryan.finance.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User postToUser(UserPostRequest userPostRequest);

    UserPostResponse userToPostResponse(User user);

    UserGetResponse userToGetResponse(User user);

}
