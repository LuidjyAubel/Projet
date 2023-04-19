package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Etudiant(@Id
                        @Column(name = "numEtudiant")
                        var numEtudiant: String,
                        var nom: String,
                        var prenom: String,
                        var email: String,
                        @Column(unique = true)
                        var codeBar: String,
                        @ManyToOne
                        var groupe: Groupe?
                        )
                        
