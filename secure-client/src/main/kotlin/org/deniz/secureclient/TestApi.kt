package org.deniz.secureclient

import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@RestController
@RequestMapping("/")
class TestApi(private val httpClient: HttpClient) {

    @GetMapping
    @RequestMapping("/test/1")
    fun g1(): String? {
        val client = WebClient.create("http://localhost:8080")
        val x = client.get().retrieve()
        return x.bodyToMono(String::class.java).block()
    }

    @GetMapping
    @RequestMapping("/test/2")
    fun other(): String? {
        val client = WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient))
                .baseUrl("https://localhost:8443/invoices").build()

        val x = client.get().retrieve()

        return x.bodyToMono(String::class.java).block()
    }

}