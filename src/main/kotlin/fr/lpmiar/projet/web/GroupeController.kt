package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.CreneauDao
import fr.lpmiar.projet.dao.EtudiantDao
import fr.lpmiar.projet.dao.GroupeDao
import fr.lpmiar.projet.dao.ProfDao
import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Etudiant
import fr.lpmiar.projet.model.Groupe
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/groupe")
class GroupeController {
    @Autowired
    private lateinit var groupeDao: GroupeDao

    @Autowired
    private lateinit var profDao : ProfDao
    private lateinit var etudiantDao : EtudiantDao
    private lateinit var creneauDao : CreneauDao

    @Operation(summary = "Method get all Groupe")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Groupe::class)
                        )
                    ])
    )
    @GetMapping
    fun index(): List<Groupe> = groupeDao.findAll()

    @Operation(summary = "Method get a group with the id of the groupe")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Groupe::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"groupe\":\"not found\"}" )
                        )])
    )
    @GetMapping("/{groupeId}")
    fun index(@PathVariable groupeId: String): ResponseEntity<Any> {
        var g =groupeDao.findById(groupeId)
        if (g==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(g)
    }

    @Operation(summary = "Method for creating a groupe")

    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Groupe::class)
                        )
                    ]),
            ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"grouoe\":\"bad request\"}" )
                        )]),
            ApiResponse(responseCode = "304",
                    description = "Not Modified",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"groupe\":\"not modified\"}" )
                        )]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"groupe\":\"not found\"}" )
                        )])
    )
    @PostMapping
    fun post(@RequestBody(required = false) g: Groupe?) : ResponseEntity<Any>{
        if (g== null)
            return  ResponseEntity(hashMapOf<String,String>(Pair("groupe","invalide")), HttpStatus.BAD_REQUEST)
        try {
            groupeDao.save(g)
        } catch (e : Exception) {
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not created")), HttpStatus.NOT_MODIFIED)
        }
        var resultGroupe = g.numGroupe?.let { groupeDao.findById(it) }
        if (resultGroupe==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(resultGroupe)
    }

    @Operation(summary = "Method for delete a groupe with the id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Groupe::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"groupe\":\"not found\"}" )
                        )])
    )
    @DeleteMapping(value = ["/{groupeId}"])
    fun delete(@PathVariable groupeId: String):ResponseEntity<Any> {
        var resultGroupe = groupeDao.findById(groupeId)
        if (resultGroupe.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found")), HttpStatus.NOT_FOUND)
        groupeDao.deleteById(groupeId)
        return ResponseEntity.ok(resultGroupe)
    }
    @Operation(summary = "Method for update the groupe with an id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Groupe::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"groupe\":\"not found\"}" )
                        )])
    )
    @PutMapping("/{groupeId}")
    fun update(@PathVariable groupeId: String,@RequestBody data:Groupe): ResponseEntity<Any>{

        var resultGroupe = groupeDao.findById(groupeId)
        if (resultGroupe.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found")), HttpStatus.NOT_FOUND)
        resultGroupe = Optional.of(data)
        groupeDao.save(resultGroupe.get())

        return ResponseEntity.ok(data)
    }


    @PostMapping("/{groupeId}/favoris/{profId}")
    fun addGroupeToFav(@PathVariable groupeId: String, @PathVariable profId : String):ResponseEntity<String>{
        var groupe = groupeDao.findById(groupeId).orElse(null)
        var prof = profDao.findById(profId).orElse(null)
        if(groupe == null || prof == null){
            return ResponseEntity.notFound().build()
        }
        prof.favoris.add(groupe)
        profDao.save(prof)
        return ResponseEntity.ok("Groupe Ajouté aux favoris du prof")
    }

    @GetMapping("/{numGroupe}/etudiants")
    fun getEtudiantsByGroupe(@PathVariable numGroupe: String): ResponseEntity<List<Etudiant>> {
        val groupe = groupeDao.findByNumGroupe(numGroupe)
                ?: return ResponseEntity.notFound().build()
        val etudiants = etudiantDao.findByGroupe(groupe)
        return ResponseEntity.ok(etudiants)
    }

    @PostMapping("/{numGroupe}/etudiants")
    fun addEtudiantToGroupe(@PathVariable numGroupe: String, @RequestBody etudiant: Etudiant): ResponseEntity<Etudiant> {
        val groupe = groupeDao.findByNumGroupe(numGroupe)
                ?: return ResponseEntity.notFound().build()
        etudiant.groupe = groupe
        val savedEtudiant = etudiantDao.save(etudiant)
        return ResponseEntity.ok(savedEtudiant)
    }

    @GetMapping("/{numGroupe}/etudiants/{numEtudiant}")
    fun getEtudiantFromGroupe(@PathVariable numGroupe: String, @PathVariable numEtudiant: String): ResponseEntity<Etudiant> {
        val groupe = groupeDao.findByNumGroupe(numGroupe)
                ?: return ResponseEntity.notFound().build()
        val etudiant = etudiantDao.findByNumEtudiant(numEtudiant)
                ?: return ResponseEntity.notFound().build()
        if (etudiant.groupe != groupe) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(etudiant)
    }

    // Récupérer les créneaux d'un groupe
    @GetMapping("/{idGroupe}/creneaux")
    fun getCreneauxByGroupe(@PathVariable idGroupe: String): List<Creneau> {
        val groupe = groupeDao.findById(idGroupe).orElseThrow { ChangeSetPersister.NotFoundException() }
        return groupe.creneaux
    }

    @PostMapping("/{numGroupe}/creneaux")
    fun addCreneauToGroupe(@PathVariable numGroupe: String,
                           @RequestBody creneau: Creneau): ResponseEntity<Creneau> {
        val groupe = groupeDao.findById(numGroupe)
                .orElseThrow { ChangeSetPersister.NotFoundException() }

        creneau.groupe = groupe
        val savedCreneau = creneauDao.save(creneau)

        return ResponseEntity.ok(savedCreneau)
    }
}