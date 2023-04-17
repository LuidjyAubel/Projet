package fr.lpmiar.projet.dao

import fr.lpmiar.projet.model.Prof
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
    interface ProfDao:JpaRepository<Prof,String> {

    }