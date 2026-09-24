# E-commerce Back-End API

API REST para um backend de e-commerce, desenvolvida em Java 17 com Spring Boot 3, persistência em MongoDB e autenticação stateless via JWT com controle de acesso por papéis (ADMIN e USER).

O projeto cobre o fluxo completo de um e-commerce simplificado: catálogo de produtos e categorias, carrinho de compras por usuário, fechamento de pedidos com baixa de estoque e um painel administrativo para gestão de pedidos.

## Funcionalidades

- Autenticação e autorização com JWT (registro, login e roles ADMIN/USER)
- Catálogo de produtos: CRUD completo, paginação e filtros por categoria, nome e faixa de preço
- Categorias: CRUD completo
- Carrinho de compras por usuário: adição, atualização de quantidade, remoção de item e limpeza do carrinho
- Checkout: geração de pedido a partir do carrinho, validação e baixa de estoque, histórico de pedidos por usuário
- Painel administrativo: listagem de todos os pedidos e atualização de status (PENDING, PAID, SHIPPED, DELIVERED, CANCELED)
- Tratamento de erros centralizado, com respostas de erro padronizadas
- Documentação interativa via Swagger/OpenAPI
- Seed automático de um usuário administrador e categorias de exemplo na inicialização
- Testes unitários (JUnit 5 e Mockito) para as regras de negócio principais
- Dockerfile e docker-compose para execução da API junto com o MongoDB

## Stack técnica

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.3 (Web, Security, Validation, Data MongoDB) |
| Banco de dados | MongoDB |
| Autenticação | JWT (biblioteca jjwt) e BCrypt |
| Documentação | springdoc-openapi (Swagger UI) |
| Testes | JUnit 5, Mockito, AssertJ |
| Build | Maven |
| Containers | Docker e Docker Compose |

## Arquitetura

O projeto segue uma arquitetura em camadas:

```
controller  -> recebe a requisicao HTTP, valida entrada, delega ao service
service     -> regras de negocio
repository  -> acesso a dados (Spring Data MongoDB)
model       -> entidades persistidas no MongoDB
dto         -> objetos de entrada (request) e saida (response) da API
security    -> autenticacao JWT, filtro de requisicao, UserDetails customizado
exception   -> excecoes de negocio e handler global
config      -> configuracao de seguranca, OpenAPI e seed de dados
```

## Como executar

### Opção 1: Docker Compose

Sobe a API e o MongoDB em containers, sem necessidade de instalação local além do Docker:

```bash
docker compose up --build
```

A API fica disponível em `http://localhost:8080`.

### Opção 2: Execução local com Maven

Pré-requisitos: Java 17, Maven e uma instância de MongoDB acessível (local ou remota).

```bash
# subir um MongoDB local, caso necessário
docker run -d -p 27017:27017 --name mongo mongo:7

# executar a aplicacao
mvn spring-boot:run
```

Por padrão, a aplicação se conecta em `mongodb://localhost:27017/ecommerce_db`. Para utilizar outra instância, defina a variável de ambiente `MONGODB_URI` antes da execução.

## Usuário de teste (seed automático)

Na primeira execução, a aplicação cria automaticamente:

- Administrador: `admin@ecommerce.com` / `admin123`
- Categorias de exemplo: Eletrônicos, Livros, Roupas

Esse comportamento pode ser desativado com a variável de ambiente `SEED_DATA=false`.

## Documentação da API

Com a aplicação em execução, a documentação interativa fica disponível em:

```
http://localhost:8080/swagger-ui.html
```

A interface lista todos os endpoints e seus modelos de request/response, e permite executar chamadas diretamente pelo navegador (utilizar o botão "Authorize" e informar o token JWT no formato `Bearer <token>`).

Uma coleção do Postman também está disponível em [`postman/ecommerce-backend.postman_collection.json`](postman/ecommerce-backend.postman_collection.json).

## Principais endpoints

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/api/auth/register` | Público | Cria uma conta (role USER) |
| POST | `/api/auth/login` | Público | Autentica e retorna o token JWT |
| GET | `/api/products` | Público | Lista produtos (paginado, com filtros) |
| GET | `/api/products/{id}` | Público | Detalhe de um produto |
| POST | `/api/products` | ADMIN | Cria produto |
| PUT | `/api/products/{id}` | ADMIN | Atualiza produto |
| DELETE | `/api/products/{id}` | ADMIN | Remove (desativa) produto |
| GET / POST / PUT / DELETE | `/api/categories` | Público (GET) / ADMIN (demais) | CRUD de categorias |
| GET | `/api/cart` | Autenticado | Consulta o carrinho atual |
| POST | `/api/cart/items` | Autenticado | Adiciona item ao carrinho |
| PUT | `/api/cart/items/{productId}` | Autenticado | Atualiza quantidade de um item |
| DELETE | `/api/cart/items/{productId}` | Autenticado | Remove um item do carrinho |
| POST | `/api/orders/checkout` | Autenticado | Gera um pedido a partir do carrinho |
| GET | `/api/orders` | Autenticado | Lista os pedidos do usuário autenticado |
| GET | `/api/orders/{id}` | Autenticado | Detalhe de um pedido próprio |
| GET | `/api/admin/orders` | ADMIN | Lista todos os pedidos da loja |
| PATCH | `/api/admin/orders/{id}/status` | ADMIN | Atualiza o status de um pedido |

## Testes

```bash
mvn test
```

## Licença

Projeto desenvolvido para fins de portfólio.
