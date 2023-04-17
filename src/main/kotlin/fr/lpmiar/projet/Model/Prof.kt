package fr.lpmiar.projet.model

import java.util.StringJoiner
import javax.persistence.*

@Entity
data class Prof(@Id
                    var id : String,
                    var username : String,
                    var password : String,
                    @OneToMany(mappedBy="numGroupe")
                    var groupes : ArrayList<Groupe> = ArrayList<Groupe>()
)