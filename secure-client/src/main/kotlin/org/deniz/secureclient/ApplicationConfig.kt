package org.deniz.secureclient

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import reactor.ipc.netty.http.client.HttpClient
import java.io.File

@Configuration
class ApplicationConfig {

    @Bean
    fun httpClient(@Value("\${client.ssl.cert-file}") certFilePath: String,
                   @Value("\${client.ssl.pem-file}") keyFilePath: String): HttpClient {
        val certFile = getFile(certFilePath)
        val keyFile = getFile(keyFilePath)

        val sslContext = SslContextBuilder
                .forClient()
                .keyManager(certFile, keyFile)
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build()
        return HttpClient.builder().options { opts -> opts.sslContext(sslContext) }
                .build()
    }

    fun getFile(fileName: String): File {
        val resource = ClassPathResource(fileName)
        return File(resource.uri)
    }
}