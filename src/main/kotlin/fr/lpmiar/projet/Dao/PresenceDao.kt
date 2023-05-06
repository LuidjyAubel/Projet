package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Etudiant
import fr.lpmiar.projet.model.Presence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

    @Repository
    interface PresenceDao: JpaRepository<Presence, Long> {
       //override fun findById(id: String): Optional<Prensence>
       fun findByCreneauAndEtudiant(creneau: Creneau, etudiant: Etudiant): Presence?
    }