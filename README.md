# IT Support Ticketing System

A comprehensive enterprise-level IT support ticketing system built with Java Spring Boot, featuring 3-tier architecture, role-based authentication, and complete ticket lifecycle management for efficient IT operations.

## 🚀 Features

### Core Functionality
- **Ticket Creation & Management** - Create tickets with priority levels and categorization
- **Assignment Workflows** - Automated and manual ticket assignment to technicians
- **Collaborative Commenting System** - Real-time communication between users and technicians
- **User Management** - Comprehensive user profile and access management
- **Status Tracking** - Complete ticket lifecycle monitoring from creation to resolution
- **Separate Dashboards** - Distinct interfaces for administrators and technicians

### Advanced Features
- **3-Tier Architecture** - Controller-Service-Repository pattern for maintainable code
- **Role-Based Authentication** - Spring Security integration with user roles and permissions
- **Complex Entity Relationships** - JPA/Hibernate entities with proper associations
- **Priority Management** - Configurable priority levels (Low, Medium, High, Critical)
- **Ticket Categories** - Hardware, Software, Network, Security issue classification
- **Search & Filtering** - Advanced search capabilities with multiple filters
- **Audit Trail** - Complete history tracking of ticket changes and updates

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.x
- **Security**: Spring Security 6.x
- **Data Access**: Spring Data JPA
- **ORM**: JPA/Hibernate
- **Database**: MySQL
- **Template Engine**: Thymeleaf
- **Frontend**: HTML5, CSS3, JavaScript
- **Build Tool**: Maven
- **Language**: Java 17+

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## ⚡ Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/it-ticket-system.git
   cd it-ticket-system
   ```

2. **Configure database**
   
   Create a MySQL database and update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/it_ticket_system
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run database migrations**
   ```bash
   mvn spring-boot:run
   ```
   *Note: Hibernate will automatically create tables on first run*

5. **Access the application**
   
   Visit `http://localhost:8080` to access the system.

## 🏗️ Architecture

### 3-Tier Architecture Implementation

```
┌─────────────────┐
│   Controller    │  ← Presentation Layer (REST/MVC Controllers)
├─────────────────┤
│    Service      │  ← Business Logic Layer
├─────────────────┤
│   Repository    │  ← Data Access Layer (JPA Repositories)
└─────────────────┘
```

### Core Components

#### Controllers
- **TicketController** - Handles ticket CRUD operations
- **UserController** - Manages user authentication and profiles
- **AdminController** - Administrative functions and dashboard
- **TechnicianController** - Technician-specific operations

#### Services
- **TicketService** - Business logic for ticket management
- **UserService** - User management and authentication logic
- **NotificationService** - Email and system notifications
- **ReportService** - Analytics and reporting functionality

#### Repositories
- **TicketRepository** - Custom JPA queries for ticket operations
- **UserRepository** - User data access with security integration
- **CommentRepository** - Comment and communication data access

## 🗄️ Database Schema

### Core Entities

#### User Entity
```java
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, TECHNICIAN, USER
    
    @OneToMany(mappedBy = "creator")
    private List<Ticket> createdTickets;
}
```

#### Ticket Entity
```java
@Entity
public class Ticket {
    @Id @GeneratedValue
    private Long id;
    private String title;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private Priority priority; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Enumerated(EnumType.STRING)
    private Status status; // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    
    @ManyToOne
    private User creator;
    
    @ManyToOne
    private User assignedTechnician;
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
```

#### Comment Entity
```java
@Entity
public class Comment {
    @Id @GeneratedValue
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    
    @ManyToOne
    private Ticket ticket;
    
    @ManyToOne
    private User author;
}
```

## 🔐 Security Implementation

### Spring Security Configuration
- **Authentication**: Form-based login with session management
- **Authorization**: Role-based access control (RBAC)
- **Password Encoding**: BCrypt encryption for secure password storage
- **CSRF Protection**: Enabled for all state-changing operations

### User Roles
- **ADMIN**: Full system access, user management, system configuration
- **TECHNICIAN**: Ticket assignment, resolution, commenting
- **USER**: Ticket creation, viewing own tickets, commenting

## 📱 Usage

### For End Users
1. **Login** to the system with your credentials
2. **Create New Ticket** by providing title, description, and priority
3. **Track Progress** through the ticket status updates
4. **Communicate** with technicians via the commenting system
5. **View History** of all your submitted tickets

### For Technicians
1. **Dashboard Access** to view assigned tickets
2. **Ticket Assignment** - receive automatic or manual assignments
3. **Status Updates** - update ticket progress (Open → In Progress → Resolved)
4. **Communication** - respond to user queries and provide updates
5. **Resolution Documentation** - document solutions for knowledge base

### For Administrators
1. **User Management** - create, modify, and deactivate user accounts
2. **System Configuration** - manage ticket categories and priorities
3. **Assignment Rules** - configure automatic ticket assignment logic
4. **Reporting** - generate system usage and performance reports
5. **System Monitoring** - oversee overall ticket system health

## 🔧 Configuration

### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/it_ticket_system
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:password}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123

# Email Configuration (for notifications)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}
```

### Custom Repository Methods
```java
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(Status status);
    List<Ticket> findByCreatorAndStatus(User creator, Status status);
    List<Ticket> findByAssignedTechnicianAndStatus(User technician, Status status);
    
    @Query("SELECT t FROM Ticket t WHERE t.priority = :priority AND t.status != 'CLOSED'")
    List<Ticket> findOpenTicketsByPriority(@Param("priority") Priority priority);
}
```

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn integration-test
```

### Test Coverage
```bash
mvn jacoco:report
```

## 📊 API Endpoints

### Public Endpoints
- `GET /` - Home page
- `GET /login` - Login page
- `POST /login` - Authenticate user

### User Endpoints
- `GET /tickets` - View user's tickets
- `POST /tickets` - Create new ticket
- `GET /tickets/{id}` - View ticket details
- `POST /tickets/{id}/comments` - Add comment

### Technician Endpoints
- `GET /technician/dashboard` - Technician dashboard
- `PUT /tickets/{id}/assign` - Accept ticket assignment
- `PUT /tickets/{id}/status` - Update ticket status

### Admin Endpoints
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/users` - Manage users
- `POST /admin/users` - Create new user
- `GET /admin/reports` - System reports

## 🚀 Deployment

### Development
```bash
mvn spring-boot:run
```

### Production
```bash
mvn clean package
java -jar target/it-ticket-system-1.0.0.jar
```

### Docker Deployment
```dockerfile
FROM openjdk:17-jre-slim
COPY target/it-ticket-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Muhammad Syazwan Akmal bin Sahimi**
- GitHub: [@flerraa](https://github.com/flerraa)
- Email: syazwanakmal80@gmail.com

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Spring Security for robust authentication and authorization
- Thymeleaf team for the template engine
- MySQL for reliable database management

---

*Built with ☕ for efficient IT operations management*
