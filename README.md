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

A lógica não é uma simples substituição de texto (find/replace). Em vez disso, o sistema trata os
