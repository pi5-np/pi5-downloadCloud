package br.com.newtonpaiva.pi5receiveCloud.service

import br.com.newtonpaiva.pi5receiveCloud.dto.*
import com.backblaze.b2.client.B2StorageClientFactory
import com.backblaze.b2.client.contentHandlers.B2ContentFileWriter
import com.backblaze.b2.client.contentSources.B2Headers
import com.backblaze.b2.client.structures.B2DownloadByNameRequest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.File
import java.util.*

@Service
class ReceiveCloudService {

    fun readFile(file: File): String{

        return org.apache.commons.io.FileUtils.readFileToString(file)

    }

    fun downloadFile(nameFile: String, bucketName: String): File {

        val b2Client = B2StorageClientFactory
                .createDefaultFactory()
                .create("003cd5502dfb9f90000000001", "K003k8wiQxhAh5o6q6fVXIUTEsde/nU", B2Headers.USER_AGENT)


        val file: File = File.createTempFile("tmp", ".txt")
        org.apache.tomcat.util.http.fileupload.FileUtils.forceMkdirParent(file)
        val downloadRequest: B2DownloadByNameRequest = B2DownloadByNameRequest
                .builder(bucketName, nameFile)
                .build()
        val downloadHandler = B2ContentFileWriter.builder(file).build()
        b2Client.downloadByName(downloadRequest, downloadHandler)

        b2Client.close()

        return file
    }

    fun returnList(filesDTO: FilesDTO): MutableList<String>{
        val returnList = mutableListOf<String>()
        filesDTO!!.files.forEach { item ->
            println("FILE NAME: ${item.fileName}")
            returnList.add(item.fileName!!)
        }
        return returnList
    }


    fun listFiles(apiUrl: String, authorizationToken: String, bucketId: String): FilesDTO? {

        val url = "$apiUrl/b2api/v2/b2_list_file_names"

        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.add("Authorization", authorizationToken)

        val bucketObject = BucketIdDTO(bucketId)

        val request: HttpEntity<BucketIdDTO> = HttpEntity<BucketIdDTO>(bucketObject, headers)
        val response = restTemplate.exchange(url, HttpMethod.POST, request, FilesDTO::class.java)

        return response.body

    }

    fun createConnection(): ObjectDTO? {

        val applicationKeyId = "003cd5502dfb9f90000000001" // Obtained from your B2 account page.

        val applicationKey = "K003k8wiQxhAh5o6q6fVXIUTEsde/nU" // Obtained from your B2 account page.

        val headerForAuthorizeAccount = "Basic " + Base64.getEncoder().encodeToString("$applicationKeyId:$applicationKey".toByteArray())

        val url = "https://api.backblazeb2.com/b2api/v2/b2_authorize_account"

        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.add("Authorization", headerForAuthorizeAccount)

        val request: HttpEntity<String> = HttpEntity<String>(headers)
        val response = restTemplate.exchange(url, HttpMethod.GET, request, ObjectDTO::class.java)

        return response.body
    }

    fun findBucketName(bucketsDTO: BucketsDTO?, username: String): String? {

        val hashmap = bucketsDTO!!.buckets.map { it.bucketName }

        if (hashmap.contains(username)) {
            bucketsDTO.buckets.forEach { item ->
                return if (item.bucketName == username) {
                    item.bucketId!!
                } else {
                    ""
                }
            }
        }
        return ""
    }


    fun listBuckets(apiUrl: String, authorizationToken: String, accountId: String): BucketsDTO? {

        val url = "$apiUrl/b2api/v2/b2_list_buckets"

        val accountDTO = AccountDTO(accountId)

        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.add("Authorization", authorizationToken)
        val request: HttpEntity<AccountDTO> = HttpEntity<AccountDTO>(accountDTO, headers)
        val response = restTemplate.exchange(url, HttpMethod.POST, request, BucketsDTO::class.java)

        return response.body
    }

}