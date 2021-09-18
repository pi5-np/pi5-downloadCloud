package br.com.newtonpaiva.pi5receiveCloud.dto


data class AtributesDTO(
        val accountId: String? = null,
        val bucketId: String? = null,
        val bucketInfo: Any,
        val bucketName: String? = null,
        val bucketType: String? = null,
        val corsRules: List<Any>? = null,
        val defaultServerSideEncryption: AtributesDefaultDTO? = null,
        val fileLockConfiguration: AtributesFileDTO? = null,
        val lifecycleRules: List<Any>? = null,
        val options: List<Any>? = null,
        val revision: String? = null
)