# Emissor de Documentos - Prova de Conceito (POC)

![Status do Projeto](https://img.shields.io/badge/status-Finalizado-green)

## 📑 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [O Conceito de "Campos Inteligentes"](#-o-conceito-de-campos-inteligentes-powered-by-spel)
- [Agradecimentos](#-agradecimentos)

## 📖 Sobre o Projeto

Este projeto é uma Prova de Conceito (POC) para um sistema de emissão de documentos. A aplicação permite a criação de documentos para pacientes, utilizando templates pré-definidos ou começando de um documento em branco.

A arquitetura é baseada em um back-end **Java com Spring Boot** que gerencia os metadados dos documentos, um front-end em **React** que utiliza o editor de texto **TinyMCE** para a criação dos documentos, e o **MinIO** como Object Storage para armazenar os arquivos gerados.

Este projeto foi inspirado no desafio "Clinica Salutar" da plataforma **IsiFlix**, do Professor Isidro.

## ✨ Funcionalidades Principais

-   **API RESTful** para gerenciamento de metadados de documentos.
-   **Criação de Documentos:** Emissão a partir de templates ou de uma página em branco.
-   **Editor de Texto Rico:** Interface de edição com TinyMCE.
-   **Campos Inteligentes:** Preenchimento automático de dados do paciente no documento através de uma sintaxe especial.
-   **Armazenamento de Arquivos:** Integração com MinIO para persistência dos documentos.

## 🚀 Tecnologias Utilizadas

O projeto é dividido em duas partes principais:

| Componente     | Tecnologias                                                               |
| :------------- | :------------------------------------------------------------------------ |
| **Back-end** | <ul><li>Java 21</li><li>Spring Boot 3</li><li>Spring Data JPA</li><li>Maven</li></ul> |
| **Front-end** | <ul><li>React</li><li>TinyMCE</li></ul>                                  |
| **Storage** | <ul><li>MinIO (Object Storage)</li></ul>                                 |
| **Banco de Dados** | <ul><li>MySQL</li></ul>                                                   |

## 🧠 O Conceito de "Campos Inteligentes" (Powered by SpEL)

Uma das funcionalidades centrais desta POC é a capacidade de popular dinamicamente as informações do paciente no corpo de um documento. Este recurso é implementado utilizando o poderoso **Spring Expression Language (SpEL)**.

### Como Funciona?

A lógica não é uma simples substituição de texto (find/replace). Em vez disso, o sistema trata os placeholders no template como expressões a serem avaliadas em tempo de execução.

1.  **Definição no Template:** Ao criar um template, o usuário insere expressões SpEL que acessam atributos de um objeto `Paciente`. A sintaxe utilizada para delimitar essas expressões é `[...]`.

    *Exemplo de texto em um template:*
    ```text
    Atesto, para os devidos fins, que o(a) paciente [nome], portador(a) do CPF nº [cpf], necessita de repouso por 3 (três) dias.
    ```

2.  **Contexto de Avaliação:** Quando um novo documento é gerado a partir deste template, o back-end cria uma instância do objeto `Paciente` com os dados relevantes e a define como o objeto raiz no contexto de avaliação do SpEL.

3.  **Processamento com SpEL:** A API então lê o conteúdo do template, identifica todas as expressões delimitadas por `[...]` e utiliza o parser do SpEL para avaliá-las com base no objeto `Paciente` fornecido.

4.  **Resultado Final:** Cada expressão é substituída pelo resultado de sua avaliação, gerando o documento final com os dados preenchidos de forma segura e precisa.

    *Exemplo do documento gerado:*
    ```text
    Atesto, para os devidos fins, que o(a) paciente João da Silva, portador(a) do CPF nº 123.456.789-00, necessita de repouso por 3 (três) dias.
    ```

### Vantagens desta Abordagem

-   **Flexibilidade:** Permite o acesso não apenas a atributos diretos (`[nome]`), mas também a propriedades de objetos aninhados (`[endereco.cidade]`) ou até mesmo a execução de métodos (`[getNome().toUpperCase()]`), se permitido na configuração.
-   **Segurança:** Utiliza um parser robusto e bem testado, evitando os problemas de uma implementação de substituição manual.
-   **Manutenibilidade:** Desacopla a lógica de geração de documentos do código-fonte. Novos templates com novos campos podem ser criados sem a necessidade de alterar a API.

## 🌟 Agradecimentos

-   Agradecimento especial ao [**Professor Isidro**](https://www.linkedin.com/in/professor-isidro-phd/) e ao projeto **"Clinica Salutar"** da plataforma **IsiFlix**, que serviram de inspiração e base conceitual para esta Prova de Conceito.
