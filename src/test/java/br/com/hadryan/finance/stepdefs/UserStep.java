package br.com.hadryan.finance.stepdefs;

import br.com.hadryan.finance.exception.EmailAlreadyRegisteredException;
import br.com.hadryan.finance.mapper.UserMapperImpl;
import br.com.hadryan.finance.mapper.dto.request.UserPostRequest;
import br.com.hadryan.finance.mapper.dto.response.UserGetResponse;
import br.com.hadryan.finance.mapper.dto.response.UserPostResponse;
import br.com.hadryan.finance.model.User;
import br.com.hadryan.finance.repository.UserRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@Import({UserMapperImpl.class})
public class UserStep extends StepDefsDefault {

    private final String URL = "/api/v1/users";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    private ResponseEntity<UserPostResponse> postResponse;
    private ResponseEntity<UserGetResponse> getResponse;
    private String errorMessage;

    @Given("que eu ainda não sou cadastrado")
    public void queEuAindaNaoSouCadastrado() {
        var user = userRepository.findByUsername("John");
        user.ifPresent(value -> userRepository.delete(value));
        user = userRepository.findByUsername("John");

        Assertions.assertThat(user).isEqualTo(Optional.empty());
    }

    @And("já existe um usuário cadastrado com o email {string}")
    public void jaExisteUmUsuarioCadastradoComOEmail(String email) {
        var user = new User();
        user.setUsername("John");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @When("faço uma requisição POST para cadastrar um usuario com os seguintes dados:")
    public void facoUmaRequisicaoPOSTParaUsersComOsSeguintesDados(DataTable data) {

        var postRequest = new UserPostRequest();
        postRequest.setUsername(data.cell(1, 0));
        postRequest.setEmail(data.cell(1, 1));
        postRequest.setPassword(passwordEncoder.encode(data.cell(1, 2)));

        var request = new HttpEntity<>(postRequest);

        try {
            postResponse = restTemplate.postForEntity(URL, request, UserPostResponse.class);
        } catch (EmailAlreadyRegisteredException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("a resposta deve ter o status code {int}")
    public void aRespostaDeveTerOStatusCode(int statusCode) {
        Assertions.assertThat(postResponse.getStatusCode().value()).isEqualTo(statusCode);
    }

    @And("o usuário deve ser retornado com os dados corretos")
    public void oUsuarioDeveSerRetornadoComOsDadosCorretos() {
        var user = userRepository.findByUsername("John").get();
        Assertions.assertThat(postResponse.getBody().getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(postResponse.getBody().getEmail()).isEqualTo(user.getEmail());
    }
}
