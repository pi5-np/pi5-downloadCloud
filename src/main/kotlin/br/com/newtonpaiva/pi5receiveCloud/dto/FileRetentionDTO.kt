package br.com.newtonpaiva.pi5receiveCloud.dto

data class FileRetentionDTO(
        val isClientAuthorizedToRead: Boolean? = null,
        val value: ValueDTO? = null
)