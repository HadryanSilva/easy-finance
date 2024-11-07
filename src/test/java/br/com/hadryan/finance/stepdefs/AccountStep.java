package br.com.hadryan.finance.stepdefs;

import br.com.hadryan.finance.mapper.AccountMapperImpl;
import br.com.hadryan.finance.mapper.dto.request.account.AccountPostRequest;
import br.com.hadryan.finance.mapper.dto.response.account.AccountGetResponse;
import br.com.hadryan.finance.mapper.dto.response.account.AccountPostResponse;
import br.com.hadryan.finance.model.Account;
import br.com.hadryan.finance.model.User;
import br.com.hadryan.finance.model.enums.AccountType;
import br.com.hadryan.finance.model.enums.Currency;
import br.com.hadryan.finance.repository.AccountRepository;
import br.com.hadryan.finance.repository.UserRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Import({AccountMapperImpl.class})
public class AccountStep extends StepDefsDefault {

    private final String URL = "/api/v1/accounts";

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<AccountPostResponse> postResponse;
    private ResponseEntity<AccountGetResponse> getResponse;

    @After
    public void cleanUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Given("que eu seja um usuario cadastrado")
    public void queEuSejaUmUsuarioCadastrado() {
        userRepository.deleteAll();
        accountRepository.deleteAll();
        var user = new User();
        user.setUsername("John");
        user.setEmail("john.doe@email.com");
        user.setPassword("123456");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Assertions.assertThat(userRepository.findByUsername("John")).isNotNull();
    }

    @And("que eu ainda não tenho uma conta cadastrada")
    public void queEuAindaNaoTenhoUmaContaCadastrada() {
        var account = accountRepository.findByName("Conta Corrente");
        account.ifPresent(value -> accountRepository.delete(value));

        Assertions.assertThat(account).isEqualTo(Optional.empty());
    }

    @And("que eu já tenha uma conta cadastrada com o nome {string}")
    public void queEuJaTenhaUmaContaCadastradaComONome(String name) {
        var account = new Account();
        account.setName(name);
        account.setType(AccountType.CHECKING);
        account.setBalance(1000.0);
        account.setCurrency(Currency.BRL);
        account.setCreatedAt(LocalDateTime.now());
        account.setUser(userRepository.findByUsername("John").get());
        accountRepository.save(account);

        Assertions.assertThat(accountRepository.findByName(name)).isNotEmpty();
    }

    @When("faço uma requisição POST para cadastrar uma conta com os seguintes dados")
    public void facoUmaRequisicaoPOSTParaCadastrarUmaContaComOsSeguintesDados(DataTable dataTable) {
        List<Map<String, String>> accountList = dataTable.asMaps(String.class, String.class);
        var account = accountList.get(0);

        var request = new AccountPostRequest();
        request.setName(account.get("name"));
        request.setType(AccountType.valueOf(account.get("type")));
        request.setBalance(Double.parseDouble(account.get("balance")));
        request.setCurrency(Currency.valueOf(account.get("currency")));
        request.setUserId(Long.parseLong(account.get("userId")));

        postResponse = restTemplate.postForEntity(URL, request, AccountPostResponse.class);
    }

    @When("faço uma requisição GET para trazer os dados de uma conta {string}")
    public void facoUmaRequisicaoGETParaTrazerOsDadosDaContaExistente(String situation) {
        if (situation.equals("existente")) {
            var account = accountRepository.findByName("Conta Corrente");
            var id = account.get().getId();
            getResponse = restTemplate.getForEntity(URL + "/" + id, AccountGetResponse.class);
        } else {
            getResponse = restTemplate.getForEntity(URL + "/0", AccountGetResponse.class);
        }
    }

    @When("faço uma requisição DELETE para deletar uma conta {string}")
    public void facoUmaRequisicaoDELETEParaDeletarAContaExistente(String situation) {
        if (situation.equals("existente")) {
            var account = accountRepository.findByName("Conta Corrente");
            var id = account.get().getId();
            restTemplate.delete(URL + "/" + id);
        } else {
            restTemplate.delete(URL + "/0");
        }
    }

    @Then("a resposta da requisição {string} de cadastro de conta deve retornar o status {int}")
    public void aRespostaDaRequisicaoDeCadastroDeContaDeveRetornarOStatus(String method, int status) {
        if (method.equals("POST")) {
            Assertions.assertThat(postResponse.getStatusCode().value()).isEqualTo(status);
        } else if (method.equals("GET")) {
            Assertions.assertThat(getResponse.getStatusCode().value()).isEqualTo(status);
        }
    }

    @And("os dados da conta retornados na requisição {string} devem estar corretos")
    public void aContaDeveSerRetornadaComOsDadosCorretos(String method) {
         Optional<Account> account;
        if (method.equals("POST")) {
             account = accountRepository.findById(postResponse.getBody().getId());
        } else {
             account = accountRepository.findById(getResponse.getBody().getId());
        }
        Assertions.assertThat(account).isNotEmpty();
        Assertions.assertThat(account.get().getName()).isEqualTo("Conta Corrente");
        Assertions.assertThat(account.get().getType()).isEqualTo(AccountType.CHECKING);
        Assertions.assertThat(account.get().getBalance()).isEqualTo(1000.0);
        Assertions.assertThat(account.get().getCurrency()).isEqualTo(Currency.BRL);

        accountRepository.delete(account.get());
    }

    @And("a conta deve ser deletada do banco de dados")
    public void aContaDeveSerDeletadaDoBancoDeDados() {
        var account = accountRepository.findByName("Conta Corrente");
        Assertions.assertThat(account).isEmpty();
    }

}
