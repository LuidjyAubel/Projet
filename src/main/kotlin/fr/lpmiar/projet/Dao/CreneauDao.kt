package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Groupe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

    @Repository
    interface CreneauDao: JpaRepository<Creneau, String> {
       //override fun findById(id: String): Optional<Creneau>
       fun findByListGroupeContains(groupe: Groupe): List<Creneau>
    }