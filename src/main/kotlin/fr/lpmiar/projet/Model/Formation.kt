package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Formation(
    @Id
    var id: String,
    var nom: String,
    var Description: String,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "CoursFormation",
        joinColumns = [JoinColumn(name = "formation_id")],
        inverseJoinColumns = [JoinColumn(name = "cours_id")]
    )
    var CoursFormation: MutableList<Cours> = mutableListOf()
)
