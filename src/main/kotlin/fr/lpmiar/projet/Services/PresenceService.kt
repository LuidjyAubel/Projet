package fr.lpmiar.projet.Services

import fr.lpmiar.projet.dao.EtudiantDao
import fr.lpmiar.projet.dao.PresenceDao
import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Etudiant
import fr.lpmiar.projet.model.Presence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PresenceService{
    @Autowired
    private lateinit var presenceDao: PresenceDao
    @Autowired
    private lateinit var etudiantDao: EtudiantDao
    private var cpt: Long = 0;
    fun CreateAll(creneau: Creneau) {
        etudiantDao.findAll().forEach{etu ->
            if (etu.groupe?.numGroupe == creneau.groupe.numGroupe){
                val presence = Presence(idPresence = cpt,creneau = creneau, etudiant = etu, estPresent = false)
                cpt++
                presenceDao.save(presence)
            }
        }
    }
    fun updatePresence(creneau: Creneau, etudiant: Etudiant, estPresent: Boolean): Presence {
        val presence = presenceDao.findByCreneauAndEtudiant(creneau, etudiant)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Presence not found for creneau ${creneau.idCreneau} and etudiant ${etudiant.numEtudiant}")

        presence.estPresent = estPresent
        return presenceDao.save(presence)
    }
}