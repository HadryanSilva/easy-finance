Feature: Cadastro de contas

  Scenario: Cadastrar usuário com sucesso
    Given que eu seja um usuario cadastrado
    And que eu ainda não tenho uma conta cadastrada
    When faço uma requisição POST para cadastrar uma conta com os seguintes dados
      | name           | type    |balance   | currency | userId |
      | Conta Corrente | CHECKING | 1000     | BRL      | 1     |
    Then a resposta da requisição "POST" de cadastro de conta deve retornar o status 201
    And os dados da conta retornados na requisição "POST" devem estar corretos

  Scenario: Cadastrar conta com nome já cadastrado
    Given que eu seja um usuario cadastrado
    And que eu já tenha uma conta cadastrada com o nome "Conta Corrente"
    When faço uma requisição POST para cadastrar uma conta com os seguintes dados
      | name           | type    |balance   | currency | userId |
      | Conta Corrente | CHECKING | 1000     | BRL      | 1     |
    Then a resposta da requisição "POST" de cadastro de conta deve retornar o status 400

  Scenario: Buscar dados de uma conta existente
    Given que eu seja um usuario cadastrado
    And que eu já tenha uma conta cadastrada com o nome "Conta Corrente"
    When faço uma requisição GET para trazer os dados de uma conta "existente"
    Then a resposta da requisição "GET" de cadastro de conta deve retornar o status 200
    And os dados da conta retornados na requisição "GET" devem estar corretos

  Scenario: Buscar dados de uma conta inexistente
    Given que eu seja um usuario cadastrado
    And que eu ainda não tenho uma conta cadastrada
    When faço uma requisição GET para trazer os dados de uma conta "inexistente"
    Then a resposta da requisição "GET" de cadastro de conta deve retornar o status 404

  Scenario: Deletar conta com sucesso
    Given que eu seja um usuario cadastrado
    And que eu já tenha uma conta cadastrada com o nome "Conta Corrente"
    When faço uma requisição DELETE para deletar uma conta "existente"
    Then a resposta da requisição "DELETE" de cadastro de conta deve retornar o status 204
    And a conta deve ser deletada do banco de dados

  Scenario: Deletar conta inexistente
    Given que eu seja um usuario cadastrado
    And que eu ainda não tenho uma conta cadastrada
    When faço uma requisição DELETE para deletar uma conta "inexistente"
    Then a resposta da requisição "DELETE" de cadastro de conta deve retornar o status 404