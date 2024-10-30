Feature: Cadastro de usuário

  Scenario: Cadastrar usuário com sucesso
    Given que eu ainda não sou cadastrado
    When faço uma requisição POST para cadastrar um usuario com os seguintes dados:
      | username | email          | password |
      | John  | john.doe@email.com| 123456   |
    Then a resposta deve ter o status code 201
    And o usuário deve ser retornado com os dados corretos

  Scenario: Cadastrar usuário com email já cadastrado
    Given que eu ainda não sou cadastrado
    And já existe um usuário cadastrado com o email "john.doe@email.com"
    When faço uma requisição POST para cadastrar um usuario com os seguintes dados:
      | username | email          | password |
      | John  | john.doe@email.com| 123456   |
    Then a resposta deve ter o status code 400

  Scenario: Cadastrar usuário com email inválido
    Given que eu ainda não sou cadastrado
    When faço uma requisição POST para cadastrar um usuario com os seguintes dados:
      | username | email          | password |
      | John  | john.doeemail.com| 123456   |
    Then a resposta deve ter o status code 400