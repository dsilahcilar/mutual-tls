package org.deniz.secureclient

import org.springframework.http.HttpMethod
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.net.URI
import java.net.URISyntaxException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/")
class ProxyApi(private val httpClient: HttpClient, private val prop: ProxyProperties) {

    @RequestMapping("{path}")
    @Throws(URISyntaxException::class)
    fun proxy(@RequestBody(required = false) body: String?, method: HttpMethod,
              @PathVariable path: String,
              request: HttpServletRequest,
              httpServletResponse: HttpServletResponse) {
        val redirectURI = URI(prop.schema.value, null, prop.host, prop.port.toInt(), "/$path", request.queryString, null)
        val client = WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient))
                .baseUrl(redirectURI.toASCIIString()).build()
                .method(method)

        request.headerNames.toList().forEach { headerName -> client.header(headerName, request.getHeader(headerName)) }
        val responseStr = client
                .body(BodyInserters.fromObject(body.toString()))
                .exchange()
                .doOnSuccess { response ->
                    response.headers().asHttpHeaders().forEach { t, u -> httpServletResponse.addHeader(t, u[0]) }
                }
                .flatMap { response -> response.bodyToMono(String::class.java) }
                .block()
        val writer = httpServletResponse.writer
        writer.write(responseStr.orEmpty())
        writer.flush()
        writer.close()
    }

}