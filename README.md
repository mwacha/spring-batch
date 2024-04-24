# spring-batch
Projeto utilizando Spring Batch para processamento de arquivos CSV.

### Como rodar o projeto
- Gerar Arquivo CSV: Utilize o utilitário CSVGenerator para criar um arquivo CSV com o número desejado de registros.
- Configuração de Armazenamento: Na classe ProductImportController, altere o valor da constante TEMP_STORAGE para o diretório de sua preferência. Essa configuração simula um armazenamento externo (storage).
- Inicialização do Banco de Dados: Execute o comando docker-compose up para iniciar o banco de dados PostgreSQL.
- Executar o Projeto Spring-Batch: Inicie o projeto Spring-Batch.
- Importar Arquivo CSV: Utilize o Postman para fazer uma requisição POST para a URL http://localhost:8080/api/v1/employees/import, enviando o arquivo CSV gerado.
- Exemplo de Requisição:<br><br>
  `curl --location 'http://localhost:8080/import-products' \
  --header 'Content-Type: multipart/form-data' \
  --form 'file=@"/Users/Marcelo/developer/springboot-batch/src/main/resources/products.csv"'`
<br><br>
Ao seguir esses passos, você poderá utilizar o Spring Batch para processar eficientemente seus arquivos CSV.
