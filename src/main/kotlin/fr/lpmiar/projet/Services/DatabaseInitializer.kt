package fr.lpmiar.projet.Services

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class DatabaseInitializer (private val entityManager : EntityManager) : ApplicationRunner{
    override fun run(args: ApplicationArguments?) {
        val scriptcontent = this::class.java.getResource("/data.sql").readText()
        entityManager.createNativeQuery(scriptcontent).executeUpdate()
    }
}