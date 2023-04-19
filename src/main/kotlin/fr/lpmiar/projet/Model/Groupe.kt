package fr.lpmiar.projet.model

import fr.lpmiar.projet.Model.GroupeEtudiant
import javax.persistence.*

@Entity
data class Groupe(@Id
                        @Column(name = "numGroupe")
                        var numGroupe: String,
                  var nom: String,
                       // @OneToMany(cascade = [(CascadeType.ALL)])
                  @OneToMany(mappedBy="numEtudiant" )
                       //var etudiants : Set<Etudiant>
                  var etudiants: MutableList<GroupeEtudiant> = mutableListOf(),
                  @ManyToMany(mappedBy = "favoris")
                  val favoris : MutableList<Prof> = mutableListOf(),
                  @OneToMany(mappedBy = "groupe", cascade = [CascadeType.ALL])
                  var creneaux: List<Creneau> = ArrayList<Creneau>(),
                        )