package fr.lpmiar.projet.Model

import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Groupe
import javax.persistence.*

@Entity
data class GroupeCreneau(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne
        @JoinColumn(name = "groupe_id")
        var groupe: Groupe,

        @ManyToOne
        @JoinColumn(name = "creneau_id")
        var creneau: Creneau,

        // Autres attributs spécifiques à cette relation, si nécessaire
)