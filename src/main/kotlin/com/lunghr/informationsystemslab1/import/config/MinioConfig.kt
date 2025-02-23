package com.lunghr.informationsystemslab1.import.config

import io.minio.*
import io.minio.messages.DeleteObject
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class MinioConfig {

    @Value("\${minio.url}")
    lateinit var minioUrl: String

    @Value("\${minio.accessKey}")
    lateinit var accessKey: String

    @Value("\${minio.secretKey}")
    lateinit var secretKey: String

    @Value("\${minio.bucket}")
    lateinit var bucketName: String

    @Bean
    @Lazy
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(accessKey, secretKey)
            .build()
    }

}
