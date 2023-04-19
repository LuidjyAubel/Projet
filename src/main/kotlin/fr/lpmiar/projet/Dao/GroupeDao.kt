package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Groupe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

    @Repository
    interface GroupeDao: JpaRepository<Groupe, String> {
       //override fun findById(id: String): Optional<Groupe>
       fun findByNumGroupe(numGroupe: String): Groupe?
    }