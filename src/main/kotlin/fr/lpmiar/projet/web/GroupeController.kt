package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.GroupeDao
import fr.lpmiar.projet.dao.ProfDao
import fr.lpmiar.projet.model.Groupe
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
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
    @GetMapping("/{id}")
    fun index(@PathVariable id: String): ResponseEntity<Any> {
        var g =groupeDao.findById(id)
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
    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable numGroupe: String):ResponseEntity<Any> {
        var resultGroupe = groupeDao.findById(numGroupe)
        if (resultGroupe.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found")), HttpStatus.NOT_FOUND)
        groupeDao.deleteById(numGroupe)
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
    @PutMapping("/{id}")
    fun update(@PathVariable id: String,@RequestBody data:Groupe): ResponseEntity<Any>{

        var resultGroupe = groupeDao.findById(id)
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
}