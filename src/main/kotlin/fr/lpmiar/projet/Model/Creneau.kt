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
)/*{
    // ...
    @Transient
    lateinit var entityManager: EntityManager

    @PostPersist
    fun createPresences() {
        // Récupérer les élèves du groupe associé au créneau
        val etudiants = groupe?.etudiants

        // Créer une présence pour chaque élève
        etudiants?.forEach { etudiant ->
            val presence = Presence(etudiant = etudiant.etudiant, creneau = this, estPresent = false)
            entityManager.persist(presence)
        }
    }
}*/