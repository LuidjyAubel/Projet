package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Etudiant(
        @Id
        var numEtudiant: String,
        var nom: String,
        var prenom: String,
        var email: String,
        var codeBar: String,
        @ManyToOne
        @JoinColumn(name = "groupe_id")
        var groupe: Groupe? = null
)
                        
