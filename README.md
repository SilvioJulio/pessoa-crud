## 🚀 API de Gerenciamento de Pessoas - DB Academia

Esta é uma API REST desenvolvida para o gerenciamento de pessoas e seus respectivos endereços. O projeto segue as melhores práticas de arquitetura e segurança exigidas em **2026**, com foco em testes automatizados e integridade de dados.

## 🛠️ Tecnologias e Versões

*   **Java 21** (LTS)
*   **Spring Boot 3.4.13** (Patch de segurança contra CVE-2025-22235)
*   **Spring Data JPA** com Hibernate
*   **Banco de Dados H2** (Em memória para desenvolvimento)
*   **Datafaker 2.4.0** (Geração de massa de dados aleatória)
*   **SpringDoc OpenAPI** (Swagger UI)
*   **JUnit 5 & Mockito** (Testes Unitários e Integração)

## 📋 Diferenciais do Projeto

### 1. Segurança e Estabilidade
O projeto foi atualizado para a versão mais recente do Spring Boot em 2026, corrigindo vulnerabilidades de exposição de endpoints e garantindo conformidade com a **CVE-2025-22235**.

### 2. Geração Automática de Dados (Datafaker)
Implementamos um `DatabaseSeeder` que, ao iniciar a aplicação, utiliza o **Datafaker** para popular o banco de dados H2 com 10 registros fictícios (nomes brasileiros, CPFs e endereços reais). Isso garante que a API sempre tenha dados para demonstração e testes imediatos.

### 3. Arquitetura Baseada em DTOs
Utilizamos o padrão **DTO (Data Transfer Object)** para isolar as entidades do banco de dados da camada de exibição. Resolvemos problemas de recursão infinita no JSON (`nesting depth 1001`) através das anotações `@JsonManagedReference` e `@JsonBackReference`.

### 4. Paginação Profissional
A API utiliza o modo de serialização `VIA_DTO`, garantindo que o retorno das listas paginadas no Swagger e Postman seja limpo, estável e contenha informações de navegação (`totalPages`, `totalElements`, etc).

## 📖 Documentação da API (Swagger)

A documentação interativa pode ser acessada através da URL:
🔗 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Principais Endpoints:
*   `GET /pessoas`: Retorna lista paginada (Ex: `?page=0&size=10`).
*   `POST /pessoas`: Cria uma pessoa e seus endereços.
    *   **Formato de Data:** `dd/MM/yyyy` (Ex: `12/01/2000`).
*   `GET /pessoas/{id}`: Busca detalhada de um registro.
*   `PUT /pessoas/{id}`: Atualização completa dos dados.
*   `DELETE /pessoas/{id}`: Remove a pessoa e seus endereços (Cascade).

## 🧪 Suíte de Testes

O projeto possui dois níveis de testes automatizados:

1.  **Testes Unitários:** Validação isolada de Services e Mappers.
2.  **Testes de Integração (`PessoaIT`):** Testes ponta a ponta que utilizam `TestRestTemplate` para validar o fluxo real desde o Controller até a persistência no banco H2.

Para executar todos os testes:
```powershell
.\mvnw.cmd test