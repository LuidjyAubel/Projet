package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Presence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

    @Repository
    interface PresenceDao: JpaRepository<Presence, String> {
       //override fun findById(id: String): Optional<Prensence>
    }