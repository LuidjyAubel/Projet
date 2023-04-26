package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Etudiant
import fr.lpmiar.projet.model.Groupe
import fr.lpmiar.projet.model.Prof
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
    interface ProfDao:JpaRepository<Prof,String> {
    }