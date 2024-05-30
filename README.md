# spring-batch
Projeto utilizando Spring Batch 

## Para processamento de arquivos CSV.
### Como rodar o projeto
- Gerar Arquivo CSV: Utilize o utilitário CSVGenerator para criar um arquivo CSV com o número desejado de registros.
- Configuração de Armazenamento: Na classe `ProductImportController`, altere o valor da constante TEMP_STORAGE para o diretório de sua preferência. Essa configuração simula um armazenamento externo (storage).
- Inicialização do Banco de Dados: Execute o comando docker-compose up para iniciar o banco de dados PostgreSQL.
- Executar o Projeto Spring-Batch: Inicie o projeto Spring-Batch.
- Importar Arquivo CSV: Utilize o Postman para fazer uma requisição POST para a URL `http://localhost:8080/import-products`, enviando o arquivo CSV gerado.
- Exemplo de Requisição:<br><br>
  `curl --location 'http://localhost:8080/import-products' \
  --header 'Content-Type: multipart/form-data' \
  --form 'file=@"/Users/Marcelo/developer/springboot-batch/src/main/resources/products.csv"'`
<br><br>
Ao seguir esses passos, você poderá utilizar o Spring Batch para processar eficientemente seus arquivos CSV.


## Para processamento em lote.
### Execute os passos:
- pip3 install psycopg2-binary 
- python3 developer-utils/insert_statements.py
  Este comando irá criar 20.000 registros na tabela charge.
- Chame a URL http://localhost:8080/api/startJob

#### Para limpar a base de dados execute os comandos abaixo:
```sql 
delete from analysis_result;
delete from batch_step_execution_context;
delete from batch_step_execution;
delete from batch_job_execution_context;
delete from batch_job_execution_params;
delete from batch_job_execution;
delete from batch_job_instance;
```
### Dependência
As seguintes dependências são necessárias:
- subir o microsserviço client para a disponibilizar a rest api que simula um resultado de análise pelo id do cliente. (EM BREVE)
  



