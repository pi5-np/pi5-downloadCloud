package br.com.newtonpaiva.pi5receiveCloud.dto

data class FilesDTO(
        val files: List<ComponetsDTO>,
        val nextFileName: String? = null
)