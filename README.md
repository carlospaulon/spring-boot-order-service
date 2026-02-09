## Executando o projeto com Docker - Container

O projeto é executado exclusivamente via containers Docker.

Para executar sem alterações nos arquivos docker, basta realizar:
```
mvn clean package -DskipTests
docker compose up -d
```

Verificar logs com:
```
docker logs rabbitmq
docker logs order-service
docker logs kafka

docker logs -f order-service # In real time
```
---
### Se realizou alguma mudança no Dockerfile, pom ou garantir rebuild
```
docker compose up -d --build
```
--- 
### Resetar bancos e filas
```
docker compose down -v

docker volume ls # Vê os volumes
```
---
> Este README reflete o estado atual do branch `feature/create-order`.

> A execução local sem Docker ainda não está disponível.
---
## Interfaces de visualização do projeto

### RabbitMQ
Interface de gerenciamento disponível em:
- http://localhost:15672/#/

Credenciais:
- Definidas via arquivo `.env`
- Valores padrão do container: guest / guest

### Sonar
Interface de gerenciamento disponível em:
- http://localhost:9000/

Credenciais (default):
- Usuário: admin
- Senha: admin (primeiro login exige troca)

### Apache Kafka
Interface de gerenciamento disponível em:
- http://localhost:8085/

### Swagger
Interface de gerenciamento disponível em:
- http://localhost:8080/swagger-ui/index.html