----------------------------------------------------------------English version------------------------------------------------------------------------------

IT Inventory Project (RESTful API with Spring Boot, MySQL, and Web Frontend)
This project consists of a RESTful API for IT inventory management, developed with Spring Boot using Maven for dependency management and MySQL as the database. The application is containerized using Docker and orchestrated with Docker Compose. The web frontend, built with pure HTML, CSS, and JavaScript, is served by an Nginx reverse proxy that also routes requests to the API.

🚀 Technologies Used
Backend (RESTful API):

Java 21

Spring Boot (v3.2.7)

Spring Data JPA

Maven (v3.9.7)

Lombok

MySQL Connector/J

Spring Boot Starter Validation

Database:

MySQL 8

Frontend (Web):

HTML5

CSS3

JavaScript

Containerization and Orchestration:

Docker

Docker Compose

Reverse Proxy and Web Server:

Nginx

📦 Project Structure
pom.xml: Maven configuration for the Spring Boot project (backend).

Dockerfile: Defines build steps for the REST API Docker image.

docker-compose.yml: Defines and orchestrates three core services: db (MySQL), apirest (Spring Boot API), and frontend (Nginx).

nginx.conf: Nginx configuration to serve frontend static files and act as a reverse proxy for the API.

src/: Contains Spring Boot application source code (backend).

src/main/java/com/invent/inventario/: API packages (Controller, Service, Repository, Entity, Exception).

src/main/resources/application.properties: Spring Boot application settings.

site/: Directory containing web frontend files (HTML, CSS, JavaScript).

site/index.html: Homepage.

site/cad_inventario.html: Item registration page.

site/consulta_inventario.html: Item query page.

site/style.css: General site styles.

site/form.css: Form-specific styles.

site/table.css: Table-specific styles.

site/script.js: General site script.

site/cad_inventario.js: JavaScript logic for registration page.

site/consulta_inventario.js: JavaScript logic for query page.

⚙️ Setup and Execution
Prerequisites
Ensure you have these tools installed:

* [Docker Desktop](https://www.docker.com/products/docker-desktop/) (inclui Docker Engine e Docker Compose)

Execution Steps
Clone the repository:

bash
git clone <YOUR_REPO_URL>
cd <your_repo_name>
Build and start services with Docker Compose:
Navigate to the project root (where docker-compose.yml is located) and run:

bash
docker-compose up --build -d
--build: Ensures Docker images are built/rebuilt before starting containers.

-d: Starts containers in detached mode (background).

Check service status:

bash
docker-compose ps
You should see db, apirest, and frontend (Nginx) services with Up and healthy status (for the database).

Accessing the Application
Web Frontend: Available on host port 8030:
Access: http://localhost:8030

RESTful API: Internally accessible to Nginx on port 8080 of the apirest container, and externally via Nginx on port 8030 under /api/.
Direct access example (if port 8031 is mapped to the API in docker-compose.yml): http://localhost:8031/api/inventario/itens

🛑 Stopping Services
To stop and remove containers, networks, and volumes created by Docker Compose:

bash
docker-compose down


------------------------------------------------------------------Portuguese version----------------------------------------------------------------------- 

# Projeto Inventário de TI (API RESTful com Spring Boot e MySQL e Frontend Web)

Este projeto consiste em uma API RESTful para controle de inventário de TI, desenvolvida com Spring Boot, utilizando Maven para gerenciamento de dependências e MySQL como banco de dados. A aplicação é conteinerizada usando Docker e orquestrada com Docker Compose. O frontend web, construído com HTML, CSS e JavaScript puro, é servido por um proxy reverso Nginx que também roteia as requisições para a API.

## 🚀 Tecnologias Utilizadas

* **Backend (API RESTful):**
    * Java 21
    * Spring Boot (v3.2.7)
    * Spring Data JPA
    * Maven (v3.9.7)
    * Lombok
    * MySQL Connector/J
    * Spring Boot Starter Validation
* **Banco de Dados:**
    * MySQL 8
* **Frontend (Web):**
    * HTML5
    * CSS3
    * JavaScript
* **Conteinerização e Orquestração:**
    * Docker
    * Docker Compose
* **Proxy Reverso e Servidor Web:**
    * Nginx

## 📦 Estrutura do Projeto

* `pom.xml`: Configurações do Maven para o projeto Spring Boot (backend).
* `Dockerfile`: Define as etapas para construir a imagem Docker da API REST.
* `docker-compose.yml`: Define e orquestra os três serviços principais: `banco` (MySQL), `apirest` (API Spring Boot) e `frontend` (Nginx).
* `nginx.conf`: Configuração do Nginx para servir os arquivos estáticos do frontend e atuar como proxy reverso para a API.
* `src/`: Contém o código fonte da aplicação Spring Boot (backend).
    * `src/main/java/com/invent/inventario/`: Pacotes da API (Controller, Service, Repository, Entity, Exception).
    * `src/main/resources/application.properties`: Configurações da aplicação Spring Boot.
* `site/`: Diretório que deve conter os arquivos do frontend web (HTML, CSS, JavaScript).
    * `site/index.html`: Página inicial.
    * `site/cad_inventario.html`: Página para cadastro de itens.
    * `site/consulta_inventario.html`: Página para consulta de itens.
    * `site/style.css`: Estilos gerais do site.
    * `site/form.css`: Estilos específicos para formulários.
    * `site/table.css`: Estilos específicos para tabelas.
    * `site/script.js`: Script geral do site.
    * `site/cad_inventario.js`: Lógica JavaScript para a página de cadastro.
    * `site/consulta_inventario.js`: Lógica JavaScript para a página de consulta.

## ⚙️ Configuração e Execução

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

* [Docker Desktop](https://www.docker.com/products/docker-desktop/) (inclui Docker Engine e Docker Compose)

### Passos para Execução

1.  **Clone o repositório:**
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd <nome_do_seu_repositorio>
    ```

2.  **Construa e inicie os serviços com Docker Compose:**
    Navegue até o diretório raiz do projeto onde o `docker-compose.yml` está localizado e execute:
    ```bash
    docker-compose up --build -d
    ```
    * `--build`: Garante que as imagens Docker sejam construídas (ou reconstruídas) antes de iniciar os contêineres.
    * `-d`: Inicia os contêineres em modo "detached" (em segundo plano).

3.  **Verifique o status dos serviços:**
    ```bash
    docker-compose ps
    ```
    Você deve ver os serviços `banco`, `apirest` e `frontend` (Nginx) em estado `Up` e `healthy` (para o banco de dados).

### Acessando a Aplicação

* **Frontend Web:** O frontend estará disponível na porta `8030` do seu host.
    Acesse: `http://localhost:8030`
* **API RESTful:** A API estará disponível internamente para o Nginx na porta `8080` do contêiner `apirest`, e externamente através do Nginx na porta `8030` sob o caminho `/api/`.
    Exemplo de acesso direto (se a porta `8031` estiver mapeada no `docker-compose.yml` para a API): `http://localhost:8031/api/inventario/itens`

## 🛑 Parando os Serviços

Para parar e remover os contêineres, redes e volumes criados pelo Docker Compose:

```bash
docker-compose down
