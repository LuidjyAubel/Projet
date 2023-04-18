package fr.lpmiar.projet.Model

import fr.lpmiar.projet.model.Groupe
import fr.lpmiar.projet.model.Prof
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class ProfGroupeFavori (
        @Id @GeneratedValue val id : Long,
        @ManyToOne val prof : Prof,
        @ManyToOne val groupe: Groupe
)