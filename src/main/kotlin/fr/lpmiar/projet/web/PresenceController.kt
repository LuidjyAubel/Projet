package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.CreneauDao
import fr.lpmiar.projet.dao.EtudiantDao
import fr.lpmiar.projet.dao.PresenceDao
import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Groupe
import fr.lpmiar.projet.model.Presence
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/presence")
class PresenceController {
    @Autowired
    private lateinit var presenceDao: PresenceDao
    @Autowired
    private lateinit var etudiantDao: EtudiantDao
    @Autowired
    private lateinit var creneauDao: CreneauDao

    @Operation(summary = "Method get all Presence")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Presence::class)
                        )
                    ])
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    fun index(): List<Presence> = presenceDao.findAll()

    @Operation(summary = "Method get a presence with his id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Presence::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"presence\":\"not found\"}" )
                        )])
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    fun index(@PathVariable id: Long): ResponseEntity<Any> {
        var p =presenceDao.findById(id)
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("presence","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(p)
    }
    @Operation(summary = "Method get a presence with his id of the etudiant and id of the creneau")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Presence::class)
                )
            ]),
        ApiResponse(responseCode = "404",
            description = "Not Found",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"presence\":\"not found\"}" )
                )])
    )

    @GetMapping("/{numEtudiant}/{idCreneau}")
    fun getPresenceEtuCre(@PathVariable numEtudiant: String,@PathVariable idCreneau: String) : ResponseEntity<Any>{
        presenceDao.findAll().forEach { presence ->
            if (presence.creneau.idCreneau == idCreneau && presence.etudiant.numEtudiant == numEtudiant)
                return ResponseEntity.ok(presence)
        }
        return ResponseEntity(hashMapOf<String,String>(Pair("presence","not found")), HttpStatus.NOT_FOUND)
    }

    @Operation(summary = "Method for creating a presence")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Presence::class)
                        )
                    ]),
            ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"presence\":\"bad request\"}" )
                        )]),
            ApiResponse(responseCode = "304",
                    description = "Not Modified",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"presence\":\"not modified\"}" )
                        )]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"presence\":\"not found\"}" )
                        )])
    )
    @PostMapping
    fun post(@RequestBody(required = false) p: Presence?) : ResponseEntity<Any>{
        if (p== null)
            return  ResponseEntity(hashMapOf<String,String>(Pair("presence","invalide")), HttpStatus.BAD_REQUEST)
        try {
            presenceDao.save(p)
        } catch (e : Exception) {
            return ResponseEntity(hashMapOf<String,String>(Pair("presence","not created")), HttpStatus.NOT_MODIFIED)
        }
        var resultPresence = p.idPresence?.let { presenceDao.findById(it) }
        if (resultPresence==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("presence","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(resultPresence)
    }

    @Operation(summary = "Method for delete a Presence with the id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Presence::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"presence\":\"not found\"}" )
                        )])
    )
    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable numEtudiant : Long):ResponseEntity<Any> {
        var resultPresence = presenceDao.findById(numEtudiant)
        if (resultPresence.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("presence","not found")), HttpStatus.NOT_FOUND)
        presenceDao.deleteById(numEtudiant)
        return ResponseEntity.ok(resultPresence)
    }
    @Operation(summary = "Method update a presence with his id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Presence::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"presence\":\"not found\"}" )
                        )])
    )
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long,@RequestBody data: Presence): ResponseEntity<Any>{

        var resultatpresence = presenceDao.findById(id)
        if (resultatpresence.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("presence","not found")), HttpStatus.NOT_FOUND)
        resultatpresence = Optional.of(data)
        presenceDao.save(resultatpresence.get())

        return ResponseEntity.ok(data)
    }
}