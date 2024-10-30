Feature: Cadastro de usuário

  Scenario: Cadastrar usuário com sucesso
    Given que eu ainda não sou cadastrado
    When faço uma requisição POST para cadastrar um usuario com os seguintes dados:
      | username | email          | password |
      | John  | john.doe@email.com| 123456   |
    Then a resposta da requisição POST deve ter o status code 201
    And o usuário deve ser retornado com os dados corretos

  Scenario: Cadastrar usuário com email já cadastrado
    Given que eu ainda não sou cadastrado
    And já existe um usuário cadastrado com o email "john.doe@email.com"
    When faço uma requisição POST para cadastrar um usuario com os seguintes dados:
      | username | email          | password |
      | John  | john.doe@email.com| 123456   |
    Then a resposta da requisição POST deve ter o status code 400

  Scenario: Cadastrar usuário com email inválido
    Given que eu ainda não sou cadastrado
    When faço uma requisição POST para cadastrar um usuario com os seguintes dados:
      | username | email          | password |
      | John  | john.doeemail.com| 123456   |
    Then a resposta da requisição POST deve ter o status code 400

  Scenario: Trazer os dados de um usuário
    Given que eu sou um usuário cadastrado
    When faço uma requisição GET para trazer os dados do usuário existente
    Then a resposta da requisição GET deve ter o status code 200
    And os dados retornados devem estar corretos

  Scenario: Buscar usuário inexistente
    Given que eu ainda não sou cadastrado
    When faço uma requisição GET para trazer os dados do usuário inexistente
    Then a resposta da requisição GET deve ter o status code 404

  Scenario: Deletar usuário
    Given que eu sou um usuário cadastrado
    When faço uma requisição DELETE para deletar o usuário existente
    Then a resposta da requisição DELETE deve ter o status code 204
    And o usuário deve ser deletado do banco de dados

  Scenario: Deletar usuário inexistente
    Given que eu ainda não sou cadastrado
    When faço uma requisição DELETE para deletar o usuário inexistente
    Then a resposta da requisição DELETE deve ter o status code 404