# desafio_luizalabs

![CI Projeto luizalabs](https://github.com/felipe-barata/desafio_luizalabs/workflows/CI%20Projeto%20luizalabs/badge.svg)


#Instruções para testes

Baixar o projeto e abrir como um projeto Maven no computador.
Para executar o projeto, é preciso de uma base de dados criada no MySQL e editar o arquivo application.properties para adicionar os parâmetros de conexão:
spring.datasource.url=jdbc:mysql://localhost:3306/{base-de-dados}?useSSL=false
spring.datasource.username={usuario}
spring.datasource.password={senha}
Depois disso ao executar o projeto o banco de dados será criado automaticamente.

O projeto pode ser testado utilizando a postman collection (desafio_magalu.postman_collection.json) que se encontra no diretório do projeto.
Além disso é possível executar os testes unitários com o comando: mvn clean install no terminal ou através da IDE.