package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Etudiant
import fr.lpmiar.projet.model.Groupe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
    interface EtudiantDao: JpaRepository<Etudiant, String> {
       //override fun findById(id: String): Optional<Etudiant>
       fun findByNumEtudiant(numEtudiant: String): Etudiant?
        fun findByGroupe(groupe: Optional<Groupe>): List<Etudiant>
        fun save(etudiant: Etudiant): Etudiant
    }
