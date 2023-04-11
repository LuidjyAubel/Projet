package fr.lpmiar.projet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProjetApplication

fun main(args: Array<String>) {
	runApplication<ProjetApplication>(*args)
}
