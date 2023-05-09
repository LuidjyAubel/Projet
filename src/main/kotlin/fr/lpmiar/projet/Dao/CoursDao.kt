package fr.lpmiar.projet.dao


import fr.lpmiar.projet.model.Cours
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CoursDao : JpaRepository<Cours, String> {
}