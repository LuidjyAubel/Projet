package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Groupe(@Id
                        @Column(name = "numGroupe")
                        var numGroupe: String,
                        var nom: String,
                       // @OneToMany(cascade = [(CascadeType.ALL)])
                        @OneToMany(mappedBy="numEtudiant" )
                       //var etudiants : Set<Etudiant>
                      var listEtudiant: ArrayList<Etudiant> = ArrayList<Etudiant>()
                        )