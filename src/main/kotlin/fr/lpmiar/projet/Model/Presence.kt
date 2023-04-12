package fr.lpmiar.projet.model

import java.util.StringJoiner
import javax.persistence.*

@Entity
data class Presence(@Id
                        var idPresence : String,
                        var etudiant: String,
                        var creneau: String,
                        var estPresent: Boolean
                        )