package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Etudiant
import fr.lpmiar.projet.model.Groupe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
    interface CreneauDao: JpaRepository<Creneau, String> {
        fun findByIdCreneau(idCreneau: String): Creneau?
        fun save(creneau: Creneau): Creneau
    }