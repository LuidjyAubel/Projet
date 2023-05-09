package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Cours(
    @Id
    var id: String,
    var nom: String,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "creneaux",
        joinColumns = [JoinColumn(name = "cours_id")],
        inverseJoinColumns = [JoinColumn(name = "creneau_id")]
    )
    var creneaux: MutableList<Creneau> = mutableListOf()
)