

# la obra magna
# asur - sistema de gestion

## asociacion de sordos del uruguay

esto es un sistema hecho en **java** para manejar la interna de asur.
usuarios, permisos, actividades, pagos y todo eso.
corre por consola, con base de datos en **postgresql** levantada con **docker**.

si seguis los pasos, anda. si no anda, es porque te salteaste algo.
 lo speedrunee en 3 dias como jesus asi q puede estar bastante fulero en alguna q otra cosa no soy programadora tengo problemas psiquiatricos nomas y lo hice x amor al arte

---

## que trae esta poronga

* manejo de usuarios
* perfiles y permisos (no cualquiera hace cualquier cosa)
* control de funcionalidades
* auditoria 
* actividades
* espacios y recursos
* tipos de actividades
* pagos

---

## con que esta hecho

* java 17+
* maven (para no compilar a mano como un cavernicola)
* postgresql
* docker + docker compose (la bd se levanta sola)
* intellij (si usas eclipse es bajo tu responsabilidad)

---

## seguridad 

* contraseñas encriptadas con bcrypt
* no se guardan passwords en texto plano
* validacion de cedula uruguaya
* contraseña fuerte o no entras
* permisos por perfil
* prepared statements (no hay sql injection aca capo)
* auditoria de todo lo que hace el usuario

---

## requisitos 

* java 17 o mas
* maven
* docker andando
* ganas de leer

---

## como levantar esto sin llorar

### 1. bajar el proyecto

```bash
unzip asur-proyecto.zip
cd asur-proyecto
```

---

### 2. levantar la base de datos

```bash
docker-compose up -d
```

si no sabes si anda:

```bash
docker ps
```

tenes que ver `asur_postgres`, si no esta, algo hiciste mal.

---

### 3. crear usuario y schema (leer bien)

```bash
docker exec -it asur_postgres psql -u postgres
```

adentro de postgres:

```sql
create user proyecto with password 'proyecto123';
create database db_asur owner proyecto;
grant all privileges on database db_asur to proyecto;
\c db_asur
create schema proyecto authorization proyecto;
grant all on schema proyecto to proyecto;
alter user proyecto set search_path to proyecto;
\q
```

**proyecto va en minusculas, no seas animal**

---

### 4. cargar las tablas y datos

desde la carpeta del proyecto:

```bash
docker exec -i asur_postgres psql -u proyecto -d db_asur < src/main/sql/02_ddl.sql
docker exec -i asur_postgres psql -u proyecto -d db_asur < src/main/sql/03_datos_iniciales.sql
```

---

### 5. crear el config.properties

ruta:

```
src/main/resources/config.properties
```

contenido:

```properties
db.url=jdbc:postgresql://localhost:5432/db_asur
db.user=proyecto
db.password=proyecto123
```

si no pones esto, no conecta 

---

### 6. correr el sistema

abrilo en intellij, busca:

```
src/main/java/com/asur/main.java
```

click derecho → run.

---

## usuarios para probar

| mail                                  | pass      | que es   |
| ------------------------------------- | --------- | -------- |
| [admin@asur.uy](mailto:admin@asur.uy) | asd123!@# | el jefe  |
| [maria@asur.uy](mailto:maria@asur.uy) | asd123!@# | socia    |
| [juan@asur.uy](mailto:juan@asur.uy)   | asd123!@# | auxiliar |
| [laura@asur.uy](mailto:laura@asur.uy) | asd123!@# | invitada |

---

## errores tipicos (retraso del usuario)
### sea lo q sea primero renombra config.propierties.example y sacale el .example si no no te va a ir ni pa atra

### no conecta / connection refused

* docker apagado
* contenedor caido

```bash
docker-compose up -d
docker ps
docker logs asur_postgres
```

---

### password authentication failed

usaste mayusculas en el usuario.

```properties
db.user=proyecto
```

asi, cortita.

---

### relation already exists / permission denied

rompiste el schema. resetear:

```bash
docker exec -i asur_postgres psql -u postgres -d db_asur -c "drop schema proyecto cascade;"
docker exec -i asur_postgres psql -u postgres -d db_asur -c "create schema proyecto authorization proyecto;"
docker exec -i asur_postgres psql -u postgres -d db_asur -c "grant all on schema proyecto to proyecto;"
docker exec -i asur_postgres psql -u proyecto -d db_asur < src/main/sql/02_ddl.sql
docker exec -i asur_postgres psql -u proyecto -d db_asur < src/main/sql/03_datos_iniciales.sql
```

---

## reset total 

```bash
docker-compose down -v
docker-compose up -d
```

y volves a arrancar desde el paso 3.

---

## comandos utiles

```bash
mvn clean compile
mvn exec:java -dexec.mainclass="com.asur.main"
docker-compose up -d
docker-compose down
docker logs asur_postgres
```

---

