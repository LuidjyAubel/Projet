package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Creneau(@Id var idCreneau: String,
                        var matiere: String,
                        var nomProf: String,
                        var heureDebut: String,
                        var heureFin: String,
                        var salle: String,
                        var date: String,
                        @OneToMany(mappedBy="numGroupe")
                          var listGroupe: List<Groupe> = ArrayList<Groupe>()
                        )