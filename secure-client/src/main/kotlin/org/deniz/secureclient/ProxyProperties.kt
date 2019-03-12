package org.deniz.secureclient

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties
@ConfigurationProperties("proxy")
class ProxyProperties {
    lateinit var schema: Schema
    lateinit var host: String
    lateinit var port:Integer
}

enum class Schema(var value: String) {
    HTTP("http"), HTTPS("https")
}