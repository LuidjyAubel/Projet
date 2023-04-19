package fr.lpmiar.projet.Model

import fr.lpmiar.projet.model.Etudiant
import fr.lpmiar.projet.model.Groupe
import javax.persistence.*

@Entity
@Table(name = "GroupeEtudiant")
data class GroupeEtudiant(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @ManyToOne
        val groupe: Groupe,

        @ManyToOne
        val etudiant: Etudiant
)