package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.EtudiantDao
import fr.lpmiar.projet.dao.PresenceDao
import fr.lpmiar.projet.model.Etudiant
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
import org.springframework.web.bind.annotation.*
import java.util.Optional


@RestController
@RequestMapping("/etudiant")
class EtudiantController {
    @Autowired
    private lateinit var etudiantDao: EtudiantDao
    @Autowired
    private  lateinit var presenceDao: PresenceDao

    @Operation(summary = "Method get all Etudiant")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Etudiant::class)
                        )
                    ])
    )
    @GetMapping
    fun index(): List<Etudiant> = etudiantDao.findAll()

    @Operation(summary = "Method get a etudiant with the numEtudiant")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Etudiant::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"etudiant\":\"not found\"}" )
                        )])
    )
    @GetMapping("/{id}")
    fun index(@PathVariable id: String): ResponseEntity<Any> {
        val etudiant = etudiantDao.findById(id)
        return if (etudiant == null) {
            ResponseEntity(hashMapOf(Pair("error", "etudiant not found")), HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok(etudiant)
        }
    }
    @Operation(summary = "Method for creating an etudiant")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Etudiant::class)
                        )
                    ]),
            ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"etudiant\":\"bad request\"}" )
                        )]),
            ApiResponse(responseCode = "304",
                    description = "Not Modified",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"etudiant\":\"not created\"}" )
                        )]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"etudiant\":\"not found\"}" )
                        )])
    )
    @PostMapping
    fun post(@RequestBody(required = false) p: Etudiant?) : ResponseEntity<Any>{
        if (p== null)
            return  ResponseEntity(hashMapOf<String,String>(Pair("etudiant","bad request")), HttpStatus.BAD_REQUEST)
        try {
            etudiantDao.save(p)
        } catch (e : Exception) {
            return ResponseEntity(hashMapOf<String,String>(Pair("etudiant","not created")), HttpStatus.NOT_MODIFIED)
        }

        var resultetudiant = p.numEtudiant?.let { etudiantDao.findById(it) }
        if (resultetudiant==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("etudiant","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(resultetudiant)
    }
    @Operation(summary = "Method for delete an etudiant with the id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Etudiant::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"etudiant\":\"not found\"}" )
                        )]),
            ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"etudiant\":\"server error\"}" )
                        )])
    )
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Any> {
        val etudiant = etudiantDao.findById(id)
        if (etudiant.isEmpty) {
            return ResponseEntity(hashMapOf<String,String>(Pair("etudiant","not found")), HttpStatus.NOT_FOUND)
        }
        etudiantDao.deleteById(id)
        if (etudiantDao.findById(id).isEmpty) {
            return ResponseEntity(hashMapOf<String,String>(Pair("etudiant","created")), HttpStatus.OK)
        }
        return ResponseEntity(hashMapOf<String,String>(Pair("etudiant","server error")), HttpStatus.INTERNAL_SERVER_ERROR)
    }
    @Operation(summary = "Method for update the etudiant with an id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Etudiant::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"etudiant\":\"not found\"}" )
                        )])
    )
    @PutMapping("/{id}")
    fun update(@PathVariable id: String,@RequestBody data:Etudiant): ResponseEntity<Any>{

        var resultEtudiant = etudiantDao.findById(id)
        if (resultEtudiant.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("etudiant","not found")), HttpStatus.NOT_FOUND)
        resultEtudiant = Optional.of(data)
        etudiantDao.save(resultEtudiant.get())

        return ResponseEntity.ok(data)
    }
    @Operation(summary = "Method for get the groupe of an Etudiant with his id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Etudiant::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"groupe etudiant\":\"not found\"}" )
                        )])
    )
    @GetMapping("/{numEtudiant}/groupe")
    fun getGroupeByNumEtudiant(@PathVariable numEtudiant: String): ResponseEntity<Any> {
        val etudiant = etudiantDao.findByNumEtudiant(numEtudiant)
        if (etudiant != null && etudiant.groupe != null) {
            return ResponseEntity.ok(etudiant.groupe!!)
        }
        return ResponseEntity(hashMapOf<String,String>(Pair("groupe etudiant","not found")), HttpStatus.NOT_FOUND)
    }
    @GetMapping("/{numEtudiant}/presence")
    fun getPresenceByNumEtudiant(@PathVariable numEtudiant: String): ResponseEntity<Any> {
        val etudiant = etudiantDao.findById(numEtudiant)
        if (etudiant == null) {
            return ResponseEntity(hashMapOf(Pair("error", "etudiant not found")), HttpStatus.NOT_FOUND)
        }
        val presences : MutableList<Presence> = mutableListOf()
        presenceDao.findAll().forEach { presence ->
            if (presence.etudiant.numEtudiant == numEtudiant){
                presences.add(presence)
            }
        }
        if (presences.isEmpty()){
            return ResponseEntity(hashMapOf<String,String>(Pair("presence","not found")), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(presences)
    }
    @GetMapping("/{numEtudiant}/presence/{id}")
    fun getPresences(@PathVariable numEtudiant: String, @PathVariable id :String): ResponseEntity<Any> {
        val etudiant = etudiantDao.findById(numEtudiant)
        if (etudiant == null) {
            return ResponseEntity(hashMapOf(Pair("error", "etudiant not found")), HttpStatus.NOT_FOUND)
        }
        val presences : MutableList<Presence> = mutableListOf()
        presenceDao.findAll().forEach { presence ->
            if (presence.etudiant.numEtudiant == numEtudiant){
                if (presence.idPresence == id.toLong()){
                    return ResponseEntity.ok(presence)
                }
            }
        }
            return ResponseEntity(hashMapOf<String,String>(Pair("presence","not found")), HttpStatus.NOT_FOUND)
    }
}
