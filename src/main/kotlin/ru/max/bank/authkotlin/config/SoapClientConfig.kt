package ru.max.bank.authkotlin.config

import jakarta.xml.bind.Marshaller
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.transport.http.HttpComponentsMessageSender

@Configuration
class SoapClientConfig {

    @Bean
    fun marshaller(): Jaxb2Marshaller {
        return Jaxb2Marshaller().apply {
            setPackagesToScan(
                "ru.max.bank.otp.model.request",
                "ru.max.bank.otp.model.response"
            )
            setMarshallerProperties(mapOf(
                Marshaller.JAXB_FORMATTED_OUTPUT to true,
                Marshaller.JAXB_ENCODING to "UTF-8"
            ))
        }
    }

    @Bean
    fun webServiceTemplate(marshaller: Jaxb2Marshaller): WebServiceTemplate {
        return WebServiceTemplate().apply {
            this.marshaller = marshaller
            this.unmarshaller = marshaller
            setDefaultUri("http://localhost:8002/ws")
        }
    }

}