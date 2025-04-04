[![codecov](https://codecov.io/gh/igorAlves02/sistema_de_gerenciamento_de_impostos/branch/main/graph/badge.svg)](https://codecov.io/gh/igorAlves02/sistema_de_gerenciamento_de_impostos)

# API de Cálculo de Impostos com Spring Boot, Spring Security e JWT 🚀

## Descrição do Projeto
Sistema de gerenciamento e cálculo de impostos no Brasil.

## Funcionalidades
- **Gerenciamento de Tipos de Impostos**:
  - Listagem de todos os tipos de impostos
  - Cadastro de novos tipos de impostos
  - Consulta de detalhes de um imposto específico
  - Exclusão de tipos de impostos
  
- **Cálculo de Impostos**:
  - Cálculo baseado no tipo de imposto e valor base
  
- **Segurança**:
  - Autenticação via JWT
  - Restrição de acesso por perfil de usuário
  - Apenas administradores podem criar/excluir impostos e realizar cálculos

## Tecnologias Utilizadas 🛠️
- Java 21
- Spring Boot 3.4
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- Swagger/OpenAPI
- JUnit para testes

## Endpoints Disponíveis

### Autenticação
- **POST /user/register**: Registra um novo usuário
- **POST /user/login**: Autentica o usuário e retorna um token JWT

### Tipos de Impostos
- **GET /tipos**: Lista todos os tipos de impostos
- **GET /tipos/{id}**: Retorna detalhes de um imposto específico
- **POST /tipos**: Cadastra um novo imposto (ADMIN)
- **DELETE /tipos/{id}**: Exclui um imposto (ADMIN)

### Cálculo
- **POST /calculo**: Calcula o valor do imposto (ADMIN)

## Estratégia de Escalabilidade 📈

O sistema utiliza o **Padrão Strategy** para implementar diferentes lógicas de cálculo de impostos, permitindo:

- **Escalabilidade**: Adicionar novos impostos sem modificar código existente
- **Manutenção simplificada**: Lógica de cada imposto isolada
- **Testabilidade**: Estratégias testadas independentemente

## Segurança 🔒
- Autenticação baseada em token JWT
- Autorização com perfis USER e ADMIN
- Endpoints sensíveis protegidos
- Senhas criptografadas com BCrypt

## Como Executar o Projeto

### Pré-requisitos
- JDK 21
- Maven instalado
- PostgreSQL instalado e configurado

### Configuração do Banco de Dados
1. Crie um banco de dados PostgreSQL chamado `impostos`
2. Configure as credenciais no arquivo `application.properties`

### Executando a Aplicação
1. Clone o repositório
2. Compile o projeto com `mvn clean install`
3. Execute com `mvn spring-boot:run`
4. Acesse a documentação Swagger em `http://localhost:8080/swagger-ui.html`

## Processo de Autenticação para Testar a API

1. Registre um usuário usando o endpoint `/user/register`
2. Faça login com as credenciais usando o endpoint `/user/login`
3. Use o token JWT retornado nas requisições subsequentes no cabeçalho `Authorization: Bearer <token>`

---

## Contribuição
Contribuições são bem-vindas! 🎉 Sinta-se à vontade para abrir pull requests.

## Previsão para o Futuro 🚀
- **Tratamento de Exceção Personalizado**: Implementação de um sistema de tratamento de exceções mais robusto e personalizado para melhorar a experiência do usuário 