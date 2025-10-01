# Car Fleet Manager API

Sistema de gerenciamento de frotas de veículos com cadastro completo, busca avançada, estatísticas detalhadas e exercícios de lógica de programação.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=alert_status&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=coverage&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=code_smells&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=ncloc&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=sqale_index&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=reliability_rating&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=duplicated_lines_density&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=vulnerabilities&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=bugs&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=security_rating&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_car-fleet-manager&metric=sqale_rating&token=b6ce94a17d4984b5ba72c334c6e61732ebff4d15)](https://sonarcloud.io/summary/new_code?id=souluanf_car-fleet-manager)

## Sumário

- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Frontend](#frontend)
- [Swagger](#swagger)
- [Requisitos](#requisitos)
- [Configuração](#configuração)
- [Execução](#execução)
- [Contato](#contato)

## Arquitetura

![Arquitetura da Aplicação](car-fleet-manager.png)

A aplicação segue uma arquitetura de microserviços com três componentes principais:
- **Frontend (React):** Interface de usuário desenvolvida em React
- **Backend (Spring Boot):** API REST
- **Database (MySQL):** Banco de dados relacional para persistência

### Princípios de Desenvolvimento
- **DDD (Domain-Driven Design):** Organização em camadas (Controller → Service → Repository) com separação clara de responsabilidades
- **Clean Code:** Código limpo, legível e bem documentado com nomenclatura expressiva
- **SOLID:** Aplicação dos princípios de design orientado a objetos

### Otimizações Futuras

Para cenários com grande volume de dados, o sistema está preparado para evoluir com:
- **Specification Pattern:** Para consultas complexas e dinâmicas usando JPA Specifications
- **Custom Queries:** Queries JPQL/SQL otimizadas para desempenho em operações de leitura
- **Pagination:** Suporte nativo para paginação em endpoints de listagem
- **Indexação:** Índices estratégicos no banco de dados para otimizar buscas

> **Nota:** Atualmente, devido ao volume reduzido de dados, optou-se por uma implementação mais simples e direta, mantendo o código limpo e de fácil manutenção.

## Funcionalidades

### Gerenciamento de Veículos
- **CRUD Completo:** Criar, listar, atualizar (completo e parcial) e excluir veículos
- **Busca Avançada:** Filtrar veículos por nome, marca, ano e cor
- **Campos:** Veículo, marca, ano, descrição, cor e status de venda
- **Validações:** Validação de dados com Bean Validation

### Estatísticas e Relatórios
- **Dashboard de Estatísticas:** Total de veículos, vendidos e disponíveis
- **Veículos Cadastrados na Semana:** Lista de veículos recém-cadastrados
- **Distribuição por Década:** Quantidade de veículos agrupados por década de fabricação
- **Distribuição por Fabricante:** Quantidade de veículos por marca

### Exercícios de Lógica
- **Cálculo de Votos:** Calcula percentuais de votos válidos, brancos e nulos
- **Bubble Sort:** Ordenação de vetores utilizando algoritmo Bubble Sort
- **Fatorial:** Cálculo recursivo de fatorial de números naturais
- **Múltiplos:** Soma de múltiplos de 3 ou 5 abaixo de um número X

## Tecnologias

### Core
- Java 21
- Spring Boot 3.5.5
- Spring Data JPA
- Maven 3.6+

### Banco de Dados
- MySQL 8.0
- Flyway (migrations)

### Documentação
- OpenAPI 3.0 (Swagger)
- Swagger UI

### Testes
- JUnit 5
- Spring Boot Test

### DevOps
- Docker
- Docker Compose

## Frontend

### Acesso
- **URL:** [http://localhost:3000](http://localhost:3000)

### Telas Disponíveis

#### 🚗 Gerenciamento de Veículos
- **Listagem de Veículos:** Tabela completa com todos os veículos cadastrados
- **Filtros Avançados:** Busca por nome, marca, ano e cor
- **Formulário de Cadastro:** Criação de novos veículos com validação
- **Edição de Veículos:** Atualização de dados de veículos existentes
- **Exclusão:** Remoção de veículos com confirmação
- **Indicador de Status:** Badge visual para veículos vendidos/disponíveis

#### 📊 Estatísticas
- **Cards de Resumo:** Total de veículos, vendidos, disponíveis e cadastrados na semana
- **Gráficos de Distribuição:**
  - Por década de fabricação (gráfico de barras horizontal)
  - Por fabricante (gráfico de barras horizontal)
- **Lista de Veículos Recentes:** Grid com veículos cadastrados nos últimos 7 dias

#### 🧮 Exercícios de Lógica
Interface interativa com 4 exercícios programáticos:
- **Exercício 1 - Votos:** Formulário para cálculo de percentuais eleitorais
- **Exercício 2 - Bubble Sort:** Input de vetor e visualização da ordenação
- **Exercício 3 - Fatorial:** Cálculo recursivo com exibição do resultado
- **Exercício 4 - Múltiplos:** Cálculo de soma de múltiplos de 3 ou 5

### Tecnologias do Frontend
- React 18
- JavaScript (ES6+)
- React Router DOM v6
- Axios para requisições HTTP
- CSS3 com design responsivo
- Navegação SPA (Single Page Application)

## Swagger

- **OpenAPI UI:** [http://localhost:8080/car-fleet-manager/swagger-ui/index.html](http://localhost:8080/car-fleet-manager/swagger-ui/index.html)
- **API Docs:** [http://localhost:8080/car-fleet-manager/v3/api-docs](http://localhost:8080/car-fleet-manager/v3/api-docs)

## Requisitos

- JDK 21
- Maven 3.6+
- Docker

## Configuração

**Instalação do JDK, Maven e Docker:**

- [Instruções para instalação do JDK](https://docs.oracle.com/en/java/javase/21/install/overview-jdk-installation.html)
- [Instruções para instalação do Maven](https://maven.apache.org/install.html)
- [Instruções para instalação do Docker](https://docs.docker.com/get-docker/)

## Execução

Copie as variáveis utilizadas

```bash
cp example.env .env
```

Execute o comando abaixo:

```bash
docker-compose up -d
```

## Contato

Para suporte ou feedback:

- **Nome:** Luan Fernandes
- **Email:**  [contact@luanfernandes.dev](mailto:contact@luanfernandes.dev)
- **LinkedIn:** [https://linkedin.com/in/souluanf](https://linkedin.com/in/souluanf)