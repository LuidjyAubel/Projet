package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Groupe(
        @Id
        var numGroupe: String,
        var nom: String
)