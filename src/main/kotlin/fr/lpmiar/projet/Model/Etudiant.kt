package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Etudiant(@Id
                        @Column(name = "numEtudiant")
                        var numEtudiant: String,
                        var nom: String,
                        var prenom: String,
                        var email: String,
                        var codeBar: String,
                        )
                        
