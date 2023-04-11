package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Presence(@Id var numEtudiant: String,
                        var idCreneau: String,
                        var estPresent: Boolean
                        )