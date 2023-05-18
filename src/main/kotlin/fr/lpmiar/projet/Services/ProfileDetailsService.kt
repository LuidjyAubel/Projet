package fr.lpmiar.projet.Services

import fr.lpmiar.projet.dao.ProfDao
import fr.lpmiar.projet.model.Prof
import fr.lpmiar.projet.web.ProfController
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import org.springframework.security.core.authority.SimpleGrantedAuthority

@Service
class ProfDetailsService(private val profRepository: ProfDao) : UserDetailsService {

    var logger: Logger = LoggerFactory.getLogger(ProfController::class.java)
    override fun loadUserByUsername(username: String): UserDetails {
        if (profRepository.findByUsername(username) == null){
            logger.info("Prof non trouvez !")
        }
        val prof: Prof = profRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Prof not found")

        val authorities = mutableListOf<SimpleGrantedAuthority>()
        authorities.add(SimpleGrantedAuthority("ROLE_${prof.role}"))

        return User(prof.username, prof.password, authorities)
    }
}