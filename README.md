# 🏆 LAVAVA 2025 - Valorant Tournament Management System

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-6DB33F?style=flat&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Apache_Maven-3.9+-C71A36?style=flat&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![API Documentation](https://img.shields.io/badge/API_Docs-Swagger-85EA2D?style=flat&logo=swagger&logoColor=white)](http://localhost:8080/swagger-ui.html)

> **Professional tournament management system for Valorant esports with comprehensive REST API, interactive documentation, and Discord integration**

LAVAVA 2025 is a sophisticated tournament management platform built with **Java 21** and **Spring Boot 3.4.5**. It provides a complete ecosystem for Valorant tournament organization, featuring advanced player statistics, dynamic ranking systems, team management, and real-time match tracking with interactive API documentation.

---

## 🚀 Key Features

### 🎯 **Player Management System**
- **Comprehensive Registration**: Secure user authentication with role-based access control
- **Advanced Profiles**: Detailed player statistics with performance tracking
- **Dynamic Rankings**: Real-time ranking calculations based on match performance
- **Player Performance Analytics**: Individual statistics and historical data

### 👥 **Team Operations**
- **Team Formation**: Create and manage competitive teams
- **Member Management**: Invite system with approval workflows
- **Team Statistics**: Collective performance metrics and history
- **Administrative Controls**: Full CRUD operations for team management

### 🏟️ **Match Management**
- **Comprehensive Match System**: Full match lifecycle management
- **Official Map Pool**: All current Valorant competitive maps
- **Real-time Results**: Live score tracking and result recording
- **Match History**: Complete historical data with detailed statistics

### 📊 **Advanced Analytics & Rankings**
- **Dynamic Leaderboards**: Real-time ranking updates with pagination
- **Performance Metrics**: Advanced statistical analysis
- **Seasonal Rankings**: Support for multiple tournament seasons
- **Custom Queries**: Flexible ranking filters and search capabilities

### 🔧 **Technical Excellence**
- **Interactive API Documentation**: Complete Swagger UI integration
- **Professional Documentation**: Comprehensive Javadoc throughout codebase
- **Multi-Environment Support**: Development, testing, and production configurations
- **Security Integration**: Spring Security with authentication and authorization
- **Database Flexibility**: H2 for development/testing, PostgreSQL for production

---

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.9+** 
- **PostgreSQL 15+** (for production)
- **Git** for version control

---

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/codeNilson/lavava2025.git
cd lavava2025
```

### 2. Configure Database (Production)
```bash
# Create PostgreSQL database
createdb lavava2025

# Update application-prod.yaml with your database credentials
```

### 3. Build and Run

#### Development Mode (H2 Database)
```bash
# Build the project
mvn clean compile

# Run with development profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Production Mode (PostgreSQL)
```bash
# Build the project
mvn clean package -DskipTests

# Run with production profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

#### Testing
```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report
```

---

## 🌐 API Documentation

The application provides comprehensive interactive API documentation through Swagger UI:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **Base API Path**: `/api/v1`

### 📚 Available Endpoints

#### 🏆 Player Rankings (`/api/v1/rankings`)
- `GET /leaderboard` - Global player leaderboard with pagination
- `GET /player/{playerId}` - Individual player ranking details
- `GET /season/{seasonId}` - Seasonal rankings
- `POST /update` - Update player rankings (Admin only)
- `DELETE /{rankingId}` - Remove ranking entry (Admin only)

#### 👤 Player Management (`/api/v1/players`)  
- `GET /` - List all players with pagination and search
- `GET /{id}` - Get player details
- `POST /` - Create new player
- `PUT /{id}` - Update player information
- `DELETE /{id}` - Remove player (Admin only)

#### 👥 Team Management (`/api/v1/teams`)
- `GET /` - List all teams with pagination
- `GET /{id}` - Get team details
- `POST /` - Create new team
- `PUT /{id}` - Update team information
- `DELETE /{id}` - Remove team (Admin only)

#### 🎮 Match Management (`/api/v1/matches`)
- `GET /` - List matches with filters
- `GET /{id}` - Get match details
- `POST /` - Create new match
- `PUT /{id}` - Update match information
- `DELETE /{id}` - Remove match (Admin only)

---

## 🏗️ Architecture & Technology Stack

### **Backend Framework**
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat&logo=spring&logoColor=white)

### **Database & Persistence**
![H2](https://img.shields.io/badge/H2_Database-0078D4?style=flat&logo=h2&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=hibernate&logoColor=white)

### **Documentation & API**
![Swagger](https://img.shields.io/badge/Swagger_UI-85EA2D?style=flat&logo=swagger&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-6BA539?style=flat&logo=openapiinitiative&logoColor=white)
![SpringDoc](https://img.shields.io/badge/SpringDoc-6DB33F?style=flat&logo=spring&logoColor=white)

### **Testing & Quality**
![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-78C679?style=flat&logo=mockito&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white)

---

## 🗂️ Project Structure

```
src/
├── main/
│   ├── java/io/github/codenilson/lavava2025/
│   │   ├── controllers/          # REST API endpoints
│   │   │   ├── MatchController.java
│   │   │   ├── PlayerController.java
│   │   │   ├── PlayerRankingController.java
│   │   │   └── TeamController.java
│   │   ├── services/             # Business logic layer
│   │   │   ├── MatchService.java
│   │   │   ├── PlayerService.java
│   │   │   ├── PlayerRankingService.java
│   │   │   └── TeamService.java
│   │   ├── repositories/         # Data access layer
│   │   ├── entities/             # JPA entities and DTOs
│   │   ├── config/               # Configuration classes
│   │   ├── authentication/       # Security implementation
│   │   └── errors/               # Exception handling
│   └── resources/
│       ├── application.yaml      # Main configuration
│       ├── application-dev.yaml  # Development settings
│       ├── application-prod.yaml # Production settings
│       └── application-test.yaml # Testing configuration
└── test/                         # Comprehensive test suite
    ├── controllers/              # Controller integration tests
    ├── services/                 # Service unit tests
    └── repositories/             # Repository tests
```

---

## ⚙️ Configuration Profiles

### Development Profile (`dev`)
- **Database**: H2 in-memory database
- **Port**: 8080
- **Base Path**: `/api/v1`
- **Auto-seeding**: Enabled for sample data

### Production Profile (`prod`)
- **Database**: PostgreSQL
- **Security**: Enhanced configuration
- **Logging**: Structured logging
- **Performance**: Optimized settings

### Test Profile (`test`)
- **Database**: H2 in-memory (isolated)
- **Security**: Disabled for testing
- **Data**: Clean state for each test

---

## 🧪 Testing

The project includes comprehensive testing coverage:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PlayerRankingControllerTest

# Run tests with specific profile
mvn test -Dspring.profiles.active=test
```

### Test Coverage

- **Unit Tests**: Service layer business logic
- **Integration Tests**: Controller endpoints with real database
- **Repository Tests**: Data layer validation
- **Security Tests**: Authentication and authorization

---

## 🤖 Discord Integration *(Planned)*

Future integration will include:

- **Tournament Notifications**: Automated match reminders
- **Ranking Updates**: Real-time leaderboard notifications  
- **Interactive Commands**: Player statistics and team management
- **Match Results**: Automated result broadcasting

---

## ⚙️ Environment Variables

For production deployment, configure the following environment variables:

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/lavava2025
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

# Application Configuration
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080

# Security Configuration
JWT_SECRET=your_jwt_secret_key_here
```

---

## 📊 Performance & Monitoring

- **Health Checks**: Spring Boot Actuator endpoints
- **Metrics**: Application performance monitoring
- **Logging**: Structured logging with configurable levels
- **Database Connection Pooling**: HikariCP for optimal performance

---

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
     mvn spring-boot:run
     ```
   - Ou, se preferir, gere o JAR e execute:
     ```bash
     mvn package
     java -jar target/lavava2025-0.0.1-SNAPSHOT.jar
     ```

---

## 🛠️ Tecnologias

- ![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)
- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white)
- ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)

---

## 📝 License

![MIT License](https://img.shields.io/badge/License-MIT-green.svg)

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Development Team

**Denilson Silva** | [![Github](https://img.shields.io/badge/GitHub-100000?style=flat&logo=github&logoColor=white)](https://github.com/codeNilson) | 📧 fcodenilson@gmail.com

**Eric Nascimento** | [![Github](https://img.shields.io/badge/GitHub-100000?style=flat&logo=github&logoColor=white)](https://github.com/EricNasciment) | 📧 Ericsilva075@gmail.com

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 🐛 Issues & Support

Feel free to submit issues and enhancement requests via [GitHub Issues](https://github.com/codeNilson/lavava2025/issues).

For support and questions, contact the development team through the provided email addresses.

---

## 📈 Roadmap

- [ ] **Discord Bot Integration**: Complete bot development with interactive commands
- [ ] **Real-time Notifications**: WebSocket integration for live updates
- [ ] **Advanced Analytics**: Enhanced statistical analysis and reporting
- [ ] **Mobile API**: Dedicated endpoints for mobile applications
- [ ] **Tournament Brackets**: Automated bracket generation and management
- [ ] **Streaming Integration**: OBS and streaming platform connectivity

---

<div align="center">

**⭐ If you find this project useful, please consider giving it a star! ⭐**

[🏆 LAVAVA 2025](https://github.com/codeNilson/lavava2025) - Professional Valorant Tournament Management System

*Documentation created with AI assistance*

</div>
