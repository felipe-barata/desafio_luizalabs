# desafio_luizalabs

![CI Projeto luizalabs](https://github.com/felipe-barata/desafio_luizalabs/workflows/CI%20Projeto%20luizalabs/badge.svg)


# Instruções para testes

<p>Baixar o projeto e abrir como um projeto Maven no computador.<br/>
Para executar o projeto, é preciso de uma base de dados criada no MySQL e editar o arquivo application.properties para adicionar os parâmetros de conexão: <br/>
spring.datasource.url=jdbc:mysql://localhost:3306/{base-de-dados}?useSSL=false <br/>
spring.datasource.username={usuario} <br/>
spring.datasource.password={senha} <br/>
Depois disso ao executar o projeto o banco de dados será criado automaticamente.</p>

<p>
O projeto pode ser testado utilizando a postman collection (desafio_magalu.postman_collection.json) que se encontra no diretório do projeto. <br/>
Além disso é possível executar os testes unitários com o comando: mvn clean install no terminal ou através da IDE.
</p>
