package fr.lpmiar.projet.Services

import fr.lpmiar.projet.dao.ProfDao
import fr.lpmiar.projet.model.Prof
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import org.springframework.security.core.authority.SimpleGrantedAuthority

@Service
class ProfDetailsService(private val profRepository: ProfDao) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val prof: Prof = profRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Prof not found")

        val authorities = mutableListOf<SimpleGrantedAuthority>()
        authorities.add(SimpleGrantedAuthority("ROLE_${prof.role}"))

        return User(prof.username, prof.password, authorities)
    }
}