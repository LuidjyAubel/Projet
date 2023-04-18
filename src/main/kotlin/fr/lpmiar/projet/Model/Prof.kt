package fr.lpmiar.projet.model

import java.util.StringJoiner
import javax.persistence.*

@Entity
data class Prof(@Id
                    var id : String,
                    var username : String,
                    var password : String,
                @ManyToMany
        @JoinTable(
                name = "prof_groupe_favoris",
                joinColumns = [JoinColumn(name= "prof_id")],
                inverseJoinColumns = [JoinColumn(name= "groupe_id")]
        )
        val favoris : MutableList<Groupe> = mutableListOf()
                /*
                    @OneToMany(mappedBy="numGroupe")
                    var groupes : MutableList<Groupe> = mutableListOf()*/
)