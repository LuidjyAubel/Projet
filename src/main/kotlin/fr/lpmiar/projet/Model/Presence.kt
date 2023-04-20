package fr.lpmiar.projet.model

import javax.persistence.*

@Entity
data class Presence(@Id @GeneratedValue(strategy = GenerationType.AUTO)
                    val idPresence: Long = 0,
                        @OneToOne(cascade = [CascadeType.ALL])
                        @JoinColumn(name = "etudiant_id", referencedColumnName = "numEtudiant")
                        var etudiant: Etudiant,
                        @OneToOne(cascade = [CascadeType.ALL])
                        @JoinColumn(name = "creneau_id", referencedColumnName = "idCreneau")
                        var creneau: Creneau,
                        var estPresent: Boolean
                        )