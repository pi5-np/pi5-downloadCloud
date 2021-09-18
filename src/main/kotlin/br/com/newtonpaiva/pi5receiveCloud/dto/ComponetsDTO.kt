package br.com.newtonpaiva.pi5receiveCloud.dto

data class ComponetsDTO(
        val accountId: String? = null,
        val action: String? = null,
        val bucketId: String? = null,
        val contentLength: String? = null,
        val contentMd5: String? = null,
        val contentSha1: String? = null,
        val contentType: String? = null,
        val fileId: String? = null,
        val fileInfo: Any? = null,
        val fileName: String? = null,
        val fileRetention: FileRetentionDTO? = null,
        val legalHold: LegalHoldDTO? = null,
        val serverSideEncryption: ServerSideEncryptionDTO? = null,
        val uploadTimestamp: String? = null
)