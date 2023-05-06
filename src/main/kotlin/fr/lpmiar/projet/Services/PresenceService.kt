package fr.lpmiar.projet.Services

import fr.lpmiar.projet.dao.PresenceDao
import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Etudiant
import fr.lpmiar.projet.model.Presence
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PresenceService(
        private val presenceRepository: PresenceDao
) {
    fun updatePresence(creneau: Creneau, etudiant: Etudiant, estPresent: Boolean): Presence {
        val presence = presenceRepository.findByCreneauAndEtudiant(creneau, etudiant)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Presence not found for creneau ${creneau.idCreneau} and etudiant ${etudiant.numEtudiant}")

        presence.estPresent = estPresent
        return presenceRepository.save(presence)
    }
}