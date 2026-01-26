# ASUR - Sistema de Gestión
## Asociación de Sordos del Uruguay

Sistema de gestión institucional para ASUR desarrollado en Java.

---

## Requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL 16+
- Docker y Docker Compose
- IntelliJ IDEA (recomendado)

---

## Instalación Paso a Paso

### 1. Clonar o descomprimir el proyecto
```bash
unzip asur-proyecto.zip
cd asur-proyecto
```

### 2. Levantar la base de datos con Docker
```bash
docker-compose up -d
```

Verificar que esté corriendo:
```bash
docker ps
```
Deberías ver `asur_postgres` en la lista.

### 3. Crear usuario y schema en PostgreSQL

Conectar a PostgreSQL:
```bash
docker exec -it asur_postgres psql -U postgres
```

Ejecutar estos comandos uno por uno:
```sql
CREATE USER proyecto WITH PASSWORD 'proyecto123';
CREATE DATABASE db_asur OWNER proyecto;
GRANT ALL PRIVILEGES ON DATABASE db_asur TO proyecto;
\c db_asur
CREATE SCHEMA IF NOT EXISTS proyecto AUTHORIZATION proyecto;
GRANT ALL ON SCHEMA proyecto TO proyecto;
ALTER USER proyecto SET search_path TO proyecto;
\q
```

### 4. Ejecutar scripts SQL
Desde la carpeta del proyecto:
```bash
docker exec -i asur_postgres psql -U proyecto -d db_asur < src/main/sql/02_ddl.sql
docker exec -i asur_postgres psql -U proyecto -d db_asur < src/main/sql/03_datos_iniciales.sql
```

### 5. Crear archivo de configuración
Crear `src/main/resources/config.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/db_asur
db.user=proyecto
db.password=proyecto123

mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.user=tu_correo@gmail.com
mail.smtp.password=tu_app_password
mail.from=noreply@asur.uy
mail.smtp.starttls=true
mail.smtp.ssl=false
```

**IMPORTANTE:** El usuario debe ser `proyecto` en minúsculas.

### 6. Abrir en IntelliJ
1. File → Open → seleccionar carpeta `asur-proyecto`
2. Esperar que cargue Maven (popup "Load Maven Project")
3. Abrir `src/main/java/com/asur/Main.java`
4. Click derecho → Run 'Main.main()'

### 7. Login
```
correo: admin@asur.uy
contraseña: asd123!@#
```

---

## Solución de Errores Comunes

### Error: "password authentication failed for user PROYECTO"
**Causa:** PostgreSQL es case-sensitive. El usuario se creó en minúsculas.

**Solución:** En `config.properties` cambiar:
```properties
db.user=proyecto    # minúsculas, NO "PROYECTO"
```

### Error: "relation already exists"
**Causa:** Las tablas ya fueron creadas anteriormente.

**Solución:** Reiniciar el schema:
```bash
docker exec -i asur_postgres psql -U postgres -d db_asur -c "DROP SCHEMA proyecto CASCADE;"
docker exec -i asur_postgres psql -U postgres -d db_asur -c "CREATE SCHEMA proyecto AUTHORIZATION proyecto;"
docker exec -i asur_postgres psql -U postgres -d db_asur -c "GRANT ALL ON SCHEMA proyecto TO proyecto;"
docker exec -i asur_postgres psql -U proyecto -d db_asur < src/main/sql/02_ddl.sql
docker exec -i asur_postgres psql -U proyecto -d db_asur < src/main/sql/03_datos_iniciales.sql
```

### Error: "permission denied for table"
**Causa:** Las tablas fueron creadas por otro usuario (postgres).

**Solución:** Misma que arriba - eliminar schema y recrear con usuario proyecto.

### Error: "database db_asur already exists"
**Causa:** La base de datos ya existe.

**Solución:** Ignorar el error o eliminar y recrear:
```bash
docker exec -i asur_postgres psql -U postgres -c "DROP DATABASE db_asur;"
docker exec -i asur_postgres psql -U postgres -c "CREATE DATABASE db_asur OWNER proyecto;"
```

### Error: "Connection refused" o "error conectando a bd"
**Causa:** Docker no está corriendo o el contenedor está apagado.

**Solución:**
```bash
docker ps                      # verificar si está corriendo
docker-compose up -d           # levantar si está apagado
docker logs asur_postgres      # ver logs si hay error
```

### Error: "role proyecto already exists"
**Causa:** El usuario ya fue creado.

**Solución:** Ignorar el error, continuar con los siguientes pasos.

---

## Resetear Todo (Nuclear Option)
Si nada funciona, empezar de cero:
```bash
docker-compose down -v                    # elimina contenedor y volumen
docker-compose up -d                      # crea todo de nuevo
docker exec -it asur_postgres psql -U postgres
```

Dentro de psql:
```sql
CREATE USER proyecto WITH PASSWORD 'proyecto123';
CREATE DATABASE db_asur OWNER proyecto;
\c db_asur
CREATE SCHEMA proyecto AUTHORIZATION proyecto;
GRANT ALL ON SCHEMA proyecto TO proyecto;
\q
```

Ejecutar scripts:
```bash
docker exec -i asur_postgres psql -U proyecto -d db_asur < src/main/sql/02_ddl.sql
docker exec -i asur_postgres psql -U proyecto -d db_asur < src/main/sql/03_datos_iniciales.sql
```

---

## Usuarios de Prueba

| Correo | Contraseña | Perfil |
|--------|------------|--------|
| admin@asur.uy | asd123!@# | Administrador |
| maria@asur.uy | asd123!@# | Socio |
| juan@asur.uy | asd123!@# | Auxiliar |
| laura@asur.uy | asd123!@# | Invitado |

---

## Módulos del Sistema

| Código | Módulo |
|--------|--------|
| RF001 | Gestión de Usuarios |
| RF002 | Gestión de Perfiles |
| RF003 | Gestión de Funcionalidades |
| RF004 | Auditoría |
| RF005 | Gestión de Actividades |
| RF006 | Gestión de Espacios/Recursos |
| RF007 | Gestión de Tipos de Actividades |
| RF008 | Gestión de Pagos |

---

## Estructura del Proyecto
```
asur-proyecto/
├── pom.xml                     # Configuración Maven
├── docker-compose.yml          # Configuración Docker
├── README.md                   # Este archivo
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/asur/
    │   │   ├── Main.java           # Punto de entrada
    │   │   ├── controlador/        # Controlador principal
    │   │   ├── modelos/            # Entidades y validaciones
    │   │   ├── daos/               # Acceso a datos
    │   │   ├── servicios/          # Lógica de negocio
    │   │   ├── consola/            # Interfaz de usuario
    │   │   └── utils/              # Utilidades
    │   ├── resources/
    │   │   └── config.properties   # Configuración (crear manualmente)
    │   └── sql/
    │       ├── 01_dcl.sql          # Usuarios y permisos
    │       ├── 02_ddl.sql          # Tablas y estructuras
    │       └── 03_datos_iniciales.sql  # Datos de prueba
    └── test/                       # Tests unitarios
```

---

## Comandos Útiles

```bash
# Maven
mvn clean compile              # Compilar
mvn exec:java -Dexec.mainClass="com.asur.Main"   # Ejecutar

# Docker
docker-compose up -d           # Levantar BD
docker-compose down            # Apagar BD
docker-compose down -v         # Apagar y eliminar datos
docker logs asur_postgres      # Ver logs

# PostgreSQL
docker exec -it asur_postgres psql -U proyecto -d db_asur   # Conectar
\dt                            # Listar tablas
\q                             # Salir
```

---

## Branches para GitHub
```bash
git init
git checkout -b main
git add .
git commit -m "Initial commit - ASUR Sistema de Gestión"

# Para desarrollo
git checkout -b develop

# Para nuevas funcionalidades
git checkout -b feature/nombre-funcionalidad

# Para corrección de bugs
git checkout -b bugfix/descripcion-bug
```

---

## Seguridad Implementada
- Contraseñas cifradas con BCrypt (factor 12)
- Validación de cédula uruguaya con algoritmo de dígito verificador
- Validación de contraseña fuerte (8+ chars, letra, número, especial)
- Control de acceso por perfiles y funcionalidades
- Auditoría de acciones de usuario
- Prepared Statements para prevenir SQL Injection
