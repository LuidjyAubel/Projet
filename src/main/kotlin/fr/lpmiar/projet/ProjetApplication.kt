package fr.lpmiar.projet

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(info = Info(
		title="Lpmiar iut nantes projet API",
		version = "1.0.0")
)
class ProjetApplication{

}
fun main(args: Array<String>) {
	runApplication<ProjetApplication>(*args)
}
