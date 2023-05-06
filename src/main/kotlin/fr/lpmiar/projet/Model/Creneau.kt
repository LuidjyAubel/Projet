package fr.lpmiar.projet.model

import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.*

@Entity
data class Creneau(
        @Id
        var idCreneau: String,
        var matiere: String,
        var nomProf: String,
        var heureDebut: String,
        var heureFin: String,
        var salle: String,
        var date: String,
        @ManyToOne
        @JoinColumn(name = "groupe_id")
        var groupe: Groupe
)