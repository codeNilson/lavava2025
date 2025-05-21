# LAVAVA 2025 JAVA EDITION ![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white) ![Discord](https://img.shields.io/badge/Discord-5865F2?style=flat&logo=discord&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white) ![MIT License](https://img.shields.io/badge/License-MIT-green.svg)

API para gerenciamento do torneio da **Liga Amadora de Valorant (LAVAVA)**, desenvolvida em Java com Spring, destinada a ser integrada com um bot do Discord.  
A API centraliza e fornece informações estruturadas sobre usuários, partidas, times, jogadores e rankings, armazenando tudo em seu próprio banco de dados.

---

## ✨ Funcionalidades

- 👤 Cadastro de usuários
- 🏆 Criação e gerenciamento de times
- 🎮 Cadastro e controle de partidas
- 🔎 Consultas e buscas de jogadores, times e partidas
- 📊 Ranking dos jogadores

---

## ⚙️ Instalação e Execução

Este projeto utiliza **Spring Boot**.  
Para rodar localmente:

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/codeNilson/lavava2025.git
   cd lavava2025
   ```

2. **Configure as variáveis de ambiente:**
   - Utilize o arquivo de exemplo de variáveis de ambiente (`.env.example`) para criar seu arquivo `.env` ou configure diretamente no ambiente.
   - Defina as variáveis necessárias para conexão com o banco de dados e outros serviços.

3. **Execute o projeto:**
   - Com Java e Maven instalados, rode:
     ```bash
     ./mvnw spring-boot:run
     ```
   - Ou, se preferir, gere o JAR e execute:
     ```bash
     ./mvnw package
     java -jar target/lavava2025-0.0.1-SNAPSHOT.jar
     ```

---

## 🛠️ Tecnologias

- ![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)
- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white)
- ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)

---

## 📝 Licença

![MIT License](https://img.shields.io/badge/License-MIT-green.svg)  
Este projeto está licenciado sob a licença MIT.

---

## 👨‍💻 Desenvolvedores

- **Nilson** | [![Github](https://img.shields.io/badge/GitHub-100000?style=flat&logo=github&logoColor=white)](https://github.com/codeNilson) | 📧 fcodenilson@gmail.com

- **EricNasciment** | [![Github](https://img.shields.io/badge/GitHub-100000?style=flat&logo=github&logoColor=white)](https://github.com/codeNilson) | 📧 Ericsilva075@gmail.com

---

Sinta-se à vontade para sugerir melhorias ou reportar problemas via [issues](https://github.com/codeNilson/lavava2025/issues)!
