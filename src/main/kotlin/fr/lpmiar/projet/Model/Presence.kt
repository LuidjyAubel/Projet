package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Presence(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var idPresence: Long,
        @ManyToOne
        @JoinColumn(name = "etudiant_id")
        var etudiant: Etudiant,
        @ManyToOne
        @JoinColumn(name = "creneau_id")
        var creneau: Creneau,
        var estPresent: Boolean
)