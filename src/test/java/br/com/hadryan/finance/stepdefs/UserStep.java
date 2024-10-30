package br.com.hadryan.finance.stepdefs;

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
import org.springframework.http.HttpMethod;
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

    @Given("que eu ainda não sou cadastrado")
    public void queEuAindaNaoSouCadastrado() {
        var user = userRepository.findByUsername("John");
        user.ifPresent(value -> userRepository.delete(value));
        user = userRepository.findByUsername("John");

        Assertions.assertThat(user).isEqualTo(Optional.empty());
    }

    @Given("que eu sou um usuário cadastrado")
    public void queEuJaSouCadastrado() {
        var user = userRepository.findByUsername("John");
        if (user.isEmpty()) {
            var newUser = new User();
            newUser.setUsername("John");
            newUser.setEmail("john.doe@email.com");
            newUser.setPassword(passwordEncoder.encode("123456"));
            newUser.setCreatedAt(LocalDateTime.now());
            userRepository.save(newUser);
        }

        Assertions.assertThat(userRepository.findByEmail("john.doe@email.com")).isNotEmpty();
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

        postResponse = restTemplate.postForEntity(URL, request, UserPostResponse.class);

    }

    @When("faço uma requisição GET para trazer os dados do usuário existente")
    public void facoUmaRequisicaoGETParaTrazerOsDadosDoUsuarioExistente() {
        var id = userRepository.findByUsername("John").get().getId();
        getResponse = restTemplate.getForEntity(URL + "/" + id, UserGetResponse.class);
    }

    @When("faço uma requisição GET para trazer os dados do usuário inexistente")
    public void facoUmaRequisicaoGETParaTrazerOsDadosDoUsuarioInexistente() {
        getResponse = restTemplate.getForEntity(URL + "/0", UserGetResponse.class);
    }

    @When("faço uma requisição DELETE para deletar o usuário existente")
    public void facoUmaRequisicaoDELETEParaTrazerOsDadosDoUsuarioExistente() {
        var id = userRepository.findByUsername("John").get().getId();
        getResponse = restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, null, UserGetResponse.class);
    }

    @When("faço uma requisição DELETE para deletar o usuário inexistente")
    public void facoUmaRequisicaoDELETEParaTrazerOsDadosDoUsuarioInexistente() {
        getResponse = restTemplate.exchange(URL + "/0", HttpMethod.DELETE, null, UserGetResponse.class);
    }

    @Then("a resposta da requisição POST deve ter o status code {int}")
    public void aRespostaDaRequisicaoPostDeveTerOStatusCode(int statusCode) {
        Assertions.assertThat(postResponse.getStatusCode().value()).isEqualTo(statusCode);
    }

    @Then("a resposta da requisição GET deve ter o status code {int}")
    public void aRespostaDaRequisicaoGetDeveTerOStatusCode(int statusCode) {
        Assertions.assertThat(getResponse.getStatusCode().value()).isEqualTo(statusCode);
    }

    @Then("a resposta da requisição DELETE deve ter o status code {int}")
    public void aRespostaDaRequisicaoDELETEDeveTerOStatusCode(int statusCode) {
        Assertions.assertThat(getResponse.getStatusCode().value()).isEqualTo(statusCode);
    }

    @And("o usuário deve ser retornado com os dados corretos")
    public void oUsuarioDeveSerRetornadoComOsDadosCorretos() {
        var user = userRepository.findByUsername("John").get();
        Assertions.assertThat(postResponse.getBody().getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(postResponse.getBody().getEmail()).isEqualTo(user.getEmail());

    }

    @Then("os dados retornados devem estar corretos")
    public void osDadosRetornadosDevemEstarCorretos() {
        var user = userRepository.findByUsername("John").get();
        Assertions.assertThat(getResponse.getBody().getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(getResponse.getBody().getEmail()).isEqualTo(user.getEmail());
    }

    @And("o usuário deve ser deletado do banco de dados")
    public void oUsuarioDeveSerDeletadoDoBancoDeDados() {
        var user = userRepository.findByUsername("John");
        Assertions.assertThat(user).isEqualTo(Optional.empty());
    }
}
