package br.com.newtonpaiva.pi5receiveCloud.controller

import br.com.newtonpaiva.pi5receiveCloud.dto.ObjectDTO
import br.com.newtonpaiva.pi5receiveCloud.dto.ObjectDownloadDTO
import br.com.newtonpaiva.pi5receiveCloud.dto.ResponseFinalDTO
import br.com.newtonpaiva.pi5receiveCloud.dto.UsernameDTO
import br.com.newtonpaiva.pi5receiveCloud.service.ReceiveCloudService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/receive")
class ReceiveCloudController(
        private val receiveCloudService: ReceiveCloudService
) {

    @PostMapping
    @RequestMapping("/list")
    fun listFilesInBucket(@RequestBody usernameDTO: UsernameDTO): ResponseEntity<Any>{

        val responseConnection: ObjectDTO? = receiveCloudService.createConnection()
        val accountId = responseConnection?.accountId
        val apiUrl = responseConnection?.apiUrl
        val authorizationToken = responseConnection?.authorizationToken

        if(accountId != null && apiUrl != null && authorizationToken != null){
            val responseBucket = receiveCloudService.listBuckets(apiUrl, authorizationToken, accountId)
            val responseFindBucket = receiveCloudService.findBucketName(responseBucket, usernameDTO.username!!)
            return if(responseFindBucket != ""){
                println(("BUCKET ID: $responseFindBucket"))
                val responseFilesTheBucket = receiveCloudService.listFiles(apiUrl, authorizationToken, responseFindBucket!!)
                if(responseFilesTheBucket!!.files.isNotEmpty()){
                    val filesName = receiveCloudService.returnList(responseFilesTheBucket)
                    return ResponseEntity.ok(filesName)
                } else {
                    ResponseEntity.badRequest().body("Nenhum arquivo encontrado")
                }
            } else {
                ResponseEntity.badRequest().body("Nenhum arquivo encontrado")
            }
        }
        else {
            return ResponseEntity.badRequest().body("Serviço da CLOUD indisponível")
        }
    }

    @PostMapping
    @RequestMapping("/download")
    fun downloadFile(@RequestBody objectDownloadDTO: ObjectDownloadDTO): ResponseEntity<Any>{

        val responseConnection: ObjectDTO? = receiveCloudService.createConnection()
        val accountId = responseConnection?.accountId
        val apiUrl = responseConnection?.apiUrl
        val authorizationToken = responseConnection?.authorizationToken

        try {
            return if(accountId != null && apiUrl != null && authorizationToken != null){
                val responseFileDownload = receiveCloudService.downloadFile(objectDownloadDTO.nameFile!!, objectDownloadDTO.username!!)
                if(responseFileDownload.exists()){
                    val responseReadFile = receiveCloudService.readFile(responseFileDownload)
                    val responseFile = ResponseFinalDTO(responseReadFile, responseFileDownload.absolutePath)
                    return ResponseEntity.ok(responseFile)
                } else {
                    ResponseEntity.badRequest().body("Serviço da CLOUD indisponível")
                }
                ResponseEntity.ok(responseFileDownload)
            }
            else {
                ResponseEntity.badRequest().body("Serviço da CLOUD indisponível")
            }
        } catch (e: Exception){
            return ResponseEntity.badRequest().body("Não foi possível baixar o arquivo da CLOUD!")
        }



    }

}