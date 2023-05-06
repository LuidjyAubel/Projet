package fr.lpmiar.projet.component

import fr.lpmiar.projet.dao.EtudiantDao
import fr.lpmiar.projet.dao.GroupeDao
import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Presence
import fr.lpmiar.projet.model.Etudiant
import org.hibernate.event.spi.*
import org.hibernate.persister.entity.EntityPersister
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.EntityManagerFactory

@Component
class CreneauEventListener : PostInsertEventListener {

    @Autowired
    lateinit var entityManagerFactory: EntityManagerFactory
    @Autowired
    private lateinit var etudiantDao : EtudiantDao
    @Autowired
    private lateinit var groupeDao : GroupeDao

    override fun requiresPostCommitHanding(p0: EntityPersister?): Boolean {
        return false
    }
    override fun onPostInsert(event: PostInsertEvent?) {
          val entity = event?.entity as? Creneau ?: return
          val entityManager = entityManagerFactory.createEntityManager()

          try {
              entityManager.transaction.begin()
              entity.groupe?.numGroupe.forEach { num ->
                  groupeDao.findAll().forEach {group ->
                      etudiantDao.findAll().forEach{ etudiant ->
                          var cpt : Long = 0
                          if (etudiant.groupe?.numGroupe == group.numGroupe){
                              entityManager.persist(Presence(idPresence = cpt,etudiant = etudiant , creneau = entity, estPresent = false))
                          }
                          cpt++
                      }
                  }
              }
              entityManager.transaction.commit()
          } catch (e: Exception) {
              entityManager.transaction.rollback()
          } finally {
              entityManager.close()
          }
      }
}