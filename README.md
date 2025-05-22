# 🔐 Gerenciador de Senhas Seguro – Java Puro

Este projeto foi desenvolvido como parte da disciplina **Desenvolvimento Seguro** com o objetivo de criar uma aplicação segura de gerenciamento de senhas, utilizando exclusivamente **Java puro**, sem frameworks externos.

## 📌 Funcionalidades

- ✅ Cadastro de senhas com nome de serviço e credenciais
- 🔐 Armazenamento criptografado usando **AES-256**
- 🧑‍💻 Autenticação com **verificação em dois fatores (2FA)** via código
- 🎲 Geração de **senhas fortes e aleatórias**
- 🌐 Verificação de **senhas vazadas** usando a API do [Have I Been Pwned](https://haveibeenpwned.com/)

## 🛠️ Tecnologias Utilizadas

- **Java 17+**
- Criptografia **AES**
- API externa: [Have I Been Pwned - Passwords](https://haveibeenpwned.com/API/v3#SearchingPwnedPasswordsByRange)
- Leitura e escrita com `java.io` e `ObjectOutputStream`

---

## 🚀 Como Executar

1. Clone ou baixe o projeto:
   ```bash
   git clone https://github.com/seu-usuario/gerenciador-senhas-java.git
