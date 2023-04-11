package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Etudiant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

    @Repository
    interface EtudiantDao: JpaRepository<Etudiant, String> {
       //override fun findById(id: String): Optional<Etudiant>
    }
