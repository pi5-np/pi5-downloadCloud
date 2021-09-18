package br.com.newtonpaiva.pi5receiveCloud.dto

data class ObjectDTO(
        val absoluteMinimumPartSize: String? = null,
        val accountId: String? = null,
        val allowed: AllowedDTO? = null,
        val apiUrl: String? = null,
        val authorizationToken: String? = null,
        val downloadUrl: String? = null,
        val recommendedPartSize: String? = null,
        val s3ApiUrl: String? = null
)