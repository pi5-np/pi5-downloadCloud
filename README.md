# pi5-downloadCloud
Microsserviço de Donwload de arquivo do Backblaze - Newton Paiva

### Microsserviço de Listagem/Download de Arquivo da CLOUD utilizando Kotlin + Spring Boot + Backblaze (CLOUD)

Method: POST
-- URL local para listagem de arquivos da CLOUD : localhost:8080/api/receive/list
Obs.: Caso o Spring Boot esteja rodando em outra porta, substitua o 8080 pela porta escolhida

##### Exemplo de input para listagem de arquivo para a CLOUD
{
    "username":"usuario"
}
-- username é o nome do usuário logado
##### Ele irá devolver uma lista de nomes do arquivos que estão na nuvem no BUCKET daquele usuário, que está vinculado ao seu username.

##### Exemplo de output para listagem de arquivo para a CLOUD
[
    "05cc0dfe-4632-4861-a373-287344d0fabe.txt",
    "Arquivo de TEST-597f46ba-d0a5-4007-b783-1ecfd0b322e5.txt",
    "Arquivo de TEST-775a6f3e-6855-4d45-8cb5-8854eca04f38.txt",
    "d0c9d3dc-7dbe-4d8a-a537-0cedf73190c2.txt",
    "f3244e4c-d401-40ef-89d9-9f220afbc234.txt"
]

##### Caso não haja nenhum arquivo ele irá devolver a mensagem: "Nenhum arquivo encontrado".

Method: POST
-- URL local para listagem de arquivos da CLOUD : localhost:8080/api/receive/download
Obs.: Caso o Spring Boot esteja rodando em outra porta, substitua o 8080 pela porta escolhida

##### Exemplo de input para download do arquivo na CLOUD
{
    "nameFile":"f3244e4c-d401-40ef-89d9-9f220afbc234.txt",
    "username":"caio97"
}
-- nameFile é o nome do arquivo retornado na lista
-- username é o nome do usuário logado

##### O microsserviço irá acessar o BUCKET, e com o nome do arquivo irá fazer o donwload e salvar na pasta tmp, irá ler esse .txt e devolver o conteúdo dele no Body juntamente com o PATH do arquivo que será usado no envio do EMAIL.

##### Exemplo de output para download do arquivo na CLOUD
{
    "text": "Message TEST",
    "pathFile": "C:\\Users\\public\\AppData\\Local\\Temp\\tmp4401963142923781433.txt"
}
 
### O microsseviço está usando a CLOUD Backblaze (https://www.backblaze.com/) e está usando o username para pesquisar e fazer o download dos arquivos dentro do BUCKET

