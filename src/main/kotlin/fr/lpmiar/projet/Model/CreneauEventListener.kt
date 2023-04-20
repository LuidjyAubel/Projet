package fr.lpmiar.projet.Model

import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Presence
import org.hibernate.event.spi.*
import org.hibernate.persister.entity.EntityPersister
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.EntityManagerFactory

@Component
class CreneauEventListener : PostInsertEventListener {

    @Autowired
    lateinit var entityManagerFactory: EntityManagerFactory

    override fun requiresPostCommitHanding(p0: EntityPersister?): Boolean {
        return false
    }

    override fun onPostInsert(event: PostInsertEvent?) {
        val entity = event?.entity as? Creneau ?: return
        val entityManager = entityManagerFactory.createEntityManager()

        try {
            entityManager.transaction.begin()
            entity.groupe?.etudiants?.forEach { etudiant ->
                entityManager.persist(Presence(etudiant = etudiant.etudiant , creneau = entity, estPresent = false))
            }
            entityManager.transaction.commit()
        } catch (e: Exception) {
            entityManager.transaction.rollback()
        } finally {
            entityManager.close()
        }
    }
}