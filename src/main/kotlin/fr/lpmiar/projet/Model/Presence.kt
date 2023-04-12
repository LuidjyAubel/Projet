package fr.lpmiar.projet.model

import java.util.StringJoiner
import javax.persistence.*

@Entity
data class Presence(@Id
                        var idPresence : String,
                        @OneToOne(cascade = [CascadeType.ALL])
                        @JoinColumn(name = "etudiant_id", referencedColumnName = "numEtudiant")
                        var etudiant: Etudiant,
                        @OneToOne(cascade = [CascadeType.ALL])
                        @JoinColumn(name = "creneau_id", referencedColumnName = "idCreneau")
                        var creneau: Creneau,
                        var estPresent: Boolean
                        )