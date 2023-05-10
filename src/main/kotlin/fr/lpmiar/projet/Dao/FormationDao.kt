package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Formation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FormationDao :JpaRepository<Formation, String> {
}