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
import org.hibernate.Hibernate
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
    private lateinit var etudiantDao : EtudiantDao
    @Autowired
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
                                        example = "{\"groupe\":\"not created\"}" )
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
            return  ResponseEntity(hashMapOf<String,String>(Pair("groupe","bad request")), HttpStatus.BAD_REQUEST)
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
    @Operation(summary = "Method for get all etudiants of the groupe with id")
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
    @GetMapping("/{numGroupe}/etudiants")
    fun getEtudiantsByGroupe(@PathVariable numGroupe: String): ResponseEntity<Any> {
        val groupe = groupeDao.findById(numGroupe)
        val etudiants : MutableList<Etudiant> = mutableListOf()
        if(groupe == null){
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found")), HttpStatus.NOT_FOUND)
        }
        etudiantDao.findAll().forEach{ etudiant ->
            if (etudiant.groupe?.numGroupe == numGroupe){
                etudiants.add(etudiant)
            }
        }
        if (etudiants.isEmpty()){
            return  ResponseEntity(hashMapOf<String, String>(Pair("Etudiant","not found")), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(etudiants)
    }
    @Operation(summary = "Method for get an etudiant of the groupe with id")
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
    @GetMapping("/{numGroupe}/etudiants/{numEtudiant}")
    fun getEtudiantFromGroupe(@PathVariable numGroupe: String, @PathVariable numEtudiant: String): ResponseEntity<Any> {
        val groupe = groupeDao.findByNumGroupe(numGroupe)
                ?: return ResponseEntity(hashMapOf<String, String>(Pair("Etudiant","not found")), HttpStatus.NOT_FOUND)
        val etudiant = etudiantDao.findByNumEtudiant(numEtudiant)
                ?: return ResponseEntity(hashMapOf<String, String>(Pair("Etudiant","not found")), HttpStatus.NOT_FOUND)
        if (etudiant.groupe != groupe) {
            return ResponseEntity(hashMapOf<String, String>(Pair("Etudiant","not found")), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(etudiant)
    }

    @Operation(summary = "Method for get all creneaux of the groupe with id")
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
    @GetMapping("/{numGroupe}/creneaux")
    fun GroupeCreneau(@PathVariable numGroupe: String): ResponseEntity<Any> {
        val groupe = groupeDao.findById(numGroupe)
        val creneaux : MutableList<Creneau> = mutableListOf()
        if(groupe == null){
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found")), HttpStatus.NOT_FOUND)
        }
        creneauDao.findAll().forEach{ creneau ->
            if (creneau.groupe.numGroupe == numGroupe){
                creneaux.add(creneau)
            }
        }
        if (creneaux.isEmpty()){
            return  ResponseEntity(hashMapOf<String, String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(creneaux)
    }
    @Operation(summary = "Method for get a creneaux of the groupe with id")
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
    @GetMapping("/{numGroupe}/creneaux/{id}")
    fun getCreneautFromGroupe(@PathVariable numGroupe: String, @PathVariable id: String): ResponseEntity<Any> {
        val groupe = groupeDao.findByNumGroupe(numGroupe)
                ?: return ResponseEntity(hashMapOf<String, String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
        val creneau = creneauDao.findByIdCreneau(id)
                ?: return ResponseEntity(hashMapOf<String, String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
        if (creneau.groupe != groupe) {
            return ResponseEntity(hashMapOf<String, String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(creneau)
    }
}