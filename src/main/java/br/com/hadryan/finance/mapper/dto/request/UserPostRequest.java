package br.com.hadryan.finance.mapper.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostRequest {

    private String username;

    private String password;

    private String email;

}
