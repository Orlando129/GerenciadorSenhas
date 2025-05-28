# 🔐 Gerenciador de Senhas Seguro – Java Puro

Este projeto foi desenvolvido como parte da disciplina **Desenvolvimento Seguro**, com o objetivo de criar uma aplicação segura de gerenciamento de senhas, utilizando exclusivamente **Java puro**, sem frameworks externos.

---

## 📌 Funcionalidades

- ✅ Cadastro de senhas com nome do serviço e credenciais  
- 🔐 Armazenamento **criptografado com AES-256**  
- 🧑‍💻 Autenticação com **verificação em dois fatores (2FA)** via código  
- 🎲 Geração de **senhas fortes e aleatórias**  
- 🌐 Verificação de **senhas vazadas** utilizando a API do [Have I Been Pwned](https://haveibeenpwned.com)  

---

## 🛠️ Tecnologias Utilizadas

- 💻 **Java 22+**
- 🔒 Criptografia **AES (Advanced Encryption Standard)**
- 🌐 API externa: [Have I Been Pwned – Passwords](https://haveibeenpwned.com/API/v3#SearchingPwnedPasswordsByRange)
- 📁 Leitura e escrita de arquivos com `java.io` e `ObjectOutputStream`
- 📂 Criação automática da pasta `data` para armazenamento dos arquivos
- 🔎 Análise de segurança com a ferramenta **Sonar**
- 🤖 Integração contínua via **GitHub Actions** com verificação automatizada de segurança usando Sonar

---

## 📚 Conceitos Envolvidos


### 🔐 Criptografia AES-GCM
- Utilizada para proteger os dados armazenados localmente.
- O modo **AES-GCM** garante **confidencialidade** e **integridade** dos dados por meio de autenticação integrada.

### 🔑 Derivação de Chave com PBKDF2
- Deriva uma chave segura a partir de uma senha mestre, usando o algoritmo **PBKDF2WithHmacSHA256**.
- Protege contra ataques de força bruta utilizando **salt** e **iterações**.

### 🎲 Geração de Senhas Fortes
- Criação de senhas seguras com letras maiúsculas, minúsculas, números e símbolos.
- As senhas são embaralhadas para aumentar a entropia e evitar padrões previsíveis.

### 🧑‍💻 Autenticação em Duas Etapas (2FA)
- Antes de acessar as senhas armazenadas, o usuário deve digitar um código de 6 dígitos enviado via console.
- Isso adiciona uma **camada extra de segurança** contra acessos não autorizados.

### 🌐 Verificação de Senhas Vazadas
- Integração com a API do [Have I Been Pwned](https://haveibeenpwned.com/) usando a técnica **k-Anonimato**.
- Permite verificar se uma senha foi exposta em vazamentos públicos, sem comprometer a privacidade do usuário.

### 🧪 Análise de Qualidade com Sonar
- A ferramenta **SonarQube** foi utilizada para garantir que o código esteja livre de **vulnerabilidades conhecidas**, **códigos duplicados** e com alta **manutenibilidade**.
- A análise é automatizada via GitHub Actions.

---

## 🚀 Como Executar

1. Clone ou baixe o projeto:
   ```bash
   git clone https://github.com/Orlando129/GerenciadorSenhas.git
