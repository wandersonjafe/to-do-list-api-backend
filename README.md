# To-Do List API (back-end)

API REST de lista de tarefas com cadastro e login de usuários. Cada usuário enxerga e altera apenas as próprias tarefas.

O projeto foi construído com **Clean Architecture** e princípios **SOLID**, com Java e Spring Boot.

## Tecnologias

- Java 21
- Spring Boot 4.1
- Spring Web MVC e Bean Validation
- Spring Data JPA (Hibernate)
- Spring Security com JWT (Resource Server)
- PostgreSQL 17
- Flyway (migrations)
- Docker Compose (banco local)
- Maven

## Funcionalidades

- Cadastro e login com e-mail e senha
- Senhas armazenadas com hash BCrypt
- Autenticação stateless com JWT (expira em 60 minutos)
- CRUD de tarefas: criar, listar, editar, concluir, reabrir e excluir
- Isolamento entre usuários: uma tarefa de outro usuário responde `404`
- Validação de entrada e respostas de erro padronizadas
- CORS configurável por variável de ambiente

## Arquitetura

O código é organizado por camadas, e as dependências sempre apontam para dentro:

```
com.wanderson.todolist
├── domain            Regras de negócio em Java puro (sem Spring nem JPA)
│   ├── model         Task, TaskStatus, User
│   ├── repository    Portas (interfaces) TaskRepository e UserRepository
│   └── exception     Exceções de domínio
├── application       Casos de uso
│   ├── usecase       Um caso de uso por ação (CreateTask, Login, ...)
│   ├── dto           Commands e responses
│   └── port          Portas para serviços externos (PasswordHasher, TokenGenerator)
├── infrastructure    Detalhes técnicos
│   ├── persistence   Entidades JPA, repositórios Spring Data e adapters
│   └── security      Spring Security, BCrypt e geração de JWT
└── presentation
    └── rest          Controllers, handler de exceções e CORS
```

Pontos principais:

- O `domain` não conhece nenhum framework.
- Os casos de uso dependem de interfaces (`TaskRepository`, `PasswordHasher`, `TokenGenerator`), e a camada de infraestrutura fornece as implementações (adapters).
- A entidade de domínio (`Task`) é separada da entidade de banco (`TaskEntity`), e um mapper faz a conversão.

## Como rodar localmente

### Pré-requisitos

- JDK 21 ou superior
- Docker e Docker Compose

### Passo a passo

1. Clone o repositório:

   ```bash
   git clone https://github.com/SEU-USUARIO/to-do-list-api-backend.git
   cd to-do-list-api-backend
   ```

2. Crie o arquivo `.env` a partir do exemplo e defina o segredo do JWT (mínimo de 32 caracteres):

   ```bash
   cp .env.example .env
   ```

   ```
   JWT_SECRET=troque-isto-por-um-texto-longo-com-32-ou-mais-caracteres
   ```

   No Windows (PowerShell), use `copy .env.example .env`.

3. Suba o banco de dados:

   ```bash
   docker compose up -d
   ```

4. Rode a aplicação:

   ```bash
   ./mvnw spring-boot:run
   ```

   No Windows: `.\mvnw spring-boot:run`

A API fica disponível em `http://localhost:8080`. As migrations do Flyway criam as tabelas automaticamente na primeira execução.

## Variáveis de ambiente

| Variável | Obrigatória | Padrão | Descrição |
|---|---|---|---|
| `JWT_SECRET` | Sim | — | Segredo usado para assinar os tokens (mínimo de 32 caracteres) |
| `TODOLIST_DB_URL` | Não | `jdbc:postgresql://localhost:5432/todolist` | URL JDBC do banco |
| `TODOLIST_DB_USER` | Não | `todolist` | Usuário do banco |
| `TODOLIST_DB_PASSWORD` | Não | `todolist` | Senha do banco |
| `CORS_ALLOWED_ORIGINS` | Não | `http://localhost:5173` | Origens permitidas, separadas por vírgula |
| `PORT` | Não | `8080` | Porta do servidor |

Os padrões de banco servem para o ambiente local. Em produção, defina todas as variáveis, principalmente o `JWT_SECRET`, que nunca deve ser versionado.

## Endpoints

### Autenticação (públicos)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/register` | Cadastra um usuário e devolve o token |
| `POST` | `/auth/login` | Autentica e devolve o token |

### Tarefas (exigem `Authorization: Bearer <token>`)

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/tasks` | Lista as tarefas do usuário, da mais recente para a mais antiga |
| `POST` | `/tasks` | Cria uma tarefa |
| `PUT` | `/tasks/{id}` | Edita título e descrição |
| `PATCH` | `/tasks/{id}/complete` | Marca como concluída |
| `PATCH` | `/tasks/{id}/reopen` | Marca como pendente |
| `DELETE` | `/tasks/{id}` | Exclui a tarefa |

### Saúde da aplicação

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/actuator/health` | Status da aplicação (público) |

## Exemplos

**Cadastro**

```http
POST /auth/register
Content-Type: application/json

{
  "email": "usuario@email.com",
  "password": "senha12345"
}
```

Resposta (`201 Created`):

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "usuario@email.com"
}
```

**Criar tarefa**

```http
POST /tasks
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Estudar Spring Security",
  "description": "Ver como o JWT é validado"
}
```

Resposta (`201 Created`):

```json
{
  "id": 1,
  "title": "Estudar Spring Security",
  "description": "Ver como o JWT é validado",
  "status": "PENDING",
  "createdAt": "2026-10-12T22:30:00.000Z"
}
```

## Respostas de erro

Os erros seguem o mesmo formato:

```json
{
  "status": 400,
  "message": "Dados inválidos",
  "fieldErrors": {
    "password": "A senha deve ter entre 8 e 72 caracteres"
  }
}
```

| Status | Quando acontece |
|---|---|
| `400` | Dados inválidos ou corpo da requisição malformado |
| `401` | Credenciais inválidas no login, ou token ausente, inválido ou expirado |
| `404` | Tarefa inexistente ou pertencente a outro usuário |
| `409` | E-mail já cadastrado |

## Banco de dados

O schema é versionado com Flyway, em `src/main/resources/db/migration`:

- `V1__create_tasks_table.sql`: tabela de tarefas
- `V2__create_users_and_task_owner.sql`: tabela de usuários e vínculo das tarefas com o dono

O Hibernate roda com `ddl-auto=validate`: ele apenas confere se as entidades batem com o schema, sem alterá-lo.

## Segurança

- Senhas com hash BCrypt, nunca armazenadas em texto puro
- Tokens JWT assinados com HMAC SHA-256
- Sessão stateless e CSRF desabilitado, já que a autenticação é por token no header
- O usuário é identificado pelo token, e nunca por um dado enviado no corpo da requisição
- A mensagem de erro do login é a mesma para e-mail inexistente e senha errada

## Próximos passos

- Front-end em React + TypeScript (repositório separado)
- Deploy da API no Railway e do front na Vercel
- Testes automatizados dos casos de uso
