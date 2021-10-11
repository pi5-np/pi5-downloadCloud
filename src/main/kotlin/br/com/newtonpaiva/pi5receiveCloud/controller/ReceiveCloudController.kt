package br.com.newtonpaiva.pi5receiveCloud.controller

import br.com.newtonpaiva.pi5receiveCloud.dto.ObjectDTO
import br.com.newtonpaiva.pi5receiveCloud.dto.ObjectDownloadDTO
import br.com.newtonpaiva.pi5receiveCloud.dto.ResponseFinalDTO
import br.com.newtonpaiva.pi5receiveCloud.dto.UsernameDTO
import br.com.newtonpaiva.pi5receiveCloud.service.ReceiveCloudService
import br.com.newtonpaiva.pi5receiveCloud.service.ValidateJwt
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/receive")
class ReceiveCloudController(
        private val receiveCloudService: ReceiveCloudService,
        private val validateJwt: ValidateJwt
) {

    @PostMapping
    @RequestMapping("/list")
    fun listFilesInBucket(@RequestBody usernameDTO: UsernameDTO, request: HttpServletRequest): ResponseEntity<Any> {

        try {
            val tokenResponse = validateJwt.getJwtFromRequest(request)
            if (tokenResponse != "") {
                validateJwt.validateToken(tokenResponse)
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is not found!")
            }
            try {
                val responseConnection: ObjectDTO? = receiveCloudService.createConnection()
                val accountId = responseConnection?.accountId
                val apiUrl = responseConnection?.apiUrl
                val authorizationToken = responseConnection?.authorizationToken

                if (accountId != null && apiUrl != null && authorizationToken != null) {
                    val responseBucket = receiveCloudService.listBuckets(apiUrl, authorizationToken, accountId)
                    val responseFindBucket = receiveCloudService.findBucketName(responseBucket, usernameDTO.username!!)
                    return if (responseFindBucket != "") {
                        println(("BUCKET ID: $responseFindBucket"))
                        val responseFilesTheBucket = receiveCloudService.listFiles(apiUrl, authorizationToken, responseFindBucket!!)
                        if (responseFilesTheBucket!!.files.isNotEmpty()) {
                            val filesName = receiveCloudService.returnList(responseFilesTheBucket)
                            return ResponseEntity.ok(filesName)
                        } else {
                            ResponseEntity.badRequest().body("Nenhum arquivo encontrado")
                        }
                    } else {
                        ResponseEntity.badRequest().body("Nenhum arquivo encontrado")
                    }
                } else {
                    return ResponseEntity.badRequest().body("Serviço da CLOUD indisponível")
                }
            } catch (e: Exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
            }
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        }

    }

    @PostMapping
    @RequestMapping("/download")
    fun downloadFile(@RequestBody objectDownloadDTO: ObjectDownloadDTO, request: HttpServletRequest): ResponseEntity<Any>{

        try {
            val tokenResponse = validateJwt.getJwtFromRequest(request)
            if (tokenResponse != "") {
                validateJwt.validateToken(tokenResponse)
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is not found!")
            }
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
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        }
    }

}