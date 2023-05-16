package fr.lpmiar.projet.model

import java.util.StringJoiner
import javax.persistence.*
import org.hibernate.annotations.BatchSize

@Entity
data class Prof(
        @Id
        var id: String,
        var username: String,
        var password: String,
        var role: String,
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "favoris",
                joinColumns = [JoinColumn(name = "prof_id")],
                inverseJoinColumns = [JoinColumn(name = "groupe_id")]
        )
        var favoris: MutableList<Groupe> = mutableListOf()
){
fun fetchGroupe() {
        favoris.size // Chargement de la collection avant la fermeture de la session Hibernate
}
}