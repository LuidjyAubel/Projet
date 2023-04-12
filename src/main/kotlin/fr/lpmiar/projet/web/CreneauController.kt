package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.CreneauDao
import fr.lpmiar.projet.model.Creneau
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
@RequestMapping("/creneau")
class CreneauController {
    @Autowired
    private lateinit var creneauDao : CreneauDao

    @Operation(summary = "Method get all Creneau")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Creneau::class)
                        )
                    ])
    )
    @GetMapping
    fun index(): List<Creneau> = creneauDao.findAll()

    @Operation(summary = "Method get a creneau with his id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Creneau::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"creneau\":\"not found\"}" )
                        )])
    )
    @GetMapping("/{id}")
    fun index(@PathVariable id: String): ResponseEntity<Any> {
        var c =creneauDao.findById(id)
        if (c==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(c)
    }

    @Operation(summary = "Method for creating a creneau")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Creneau::class)
                        )
                    ]),
            ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"creneau\":\"bad request\"}" )
                        )]),
            ApiResponse(responseCode = "304",
                    description = "Not Modified",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"creneau\":\"not modified\"}" )
                        )]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"creneau\":\"not found\"}" )
                        )])
    )
    @PostMapping
    fun post(@RequestBody(required = false) c: Creneau?) : ResponseEntity<Any>{
        if (c== null)
            return  ResponseEntity(hashMapOf<String,String>(Pair("creneau","invalide")), HttpStatus.BAD_REQUEST)
        try {
            creneauDao.save(c)
        } catch (e : Exception) {
            return ResponseEntity(hashMapOf<String,String>(Pair("creneau","not created")), HttpStatus.NOT_MODIFIED)
        }
        var resultCreneau = c.idCreneau?.let { creneauDao.findById(it) }
        if (resultCreneau==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(resultCreneau)
    }

    @Operation(summary = "Method for delete a creneau with the id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Creneau::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"creneau\":\"not found\"}" )
                        )])
    )
    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable idCreneau : String):ResponseEntity<Any> {
        var resultCreneau = creneauDao.findById(idCreneau)
        if (resultCreneau.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
        creneauDao.deleteById(idCreneau)
        return ResponseEntity.ok(resultCreneau)
    }

    @Operation(summary = "Method for update the creneau with an id")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Creneau::class)
                        )
                    ]),
            ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(type = "object",
                                        example = "{\"creneau\":\"not found\"}" )
                        )])
    )
    @PutMapping("/{id}")
    fun update(@PathVariable id: String,@RequestBody data:Creneau): ResponseEntity<Any>{

        var resultCreneau = creneauDao.findById(id)
        if (resultCreneau.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
        resultCreneau = Optional.of(data)
        creneauDao.save(resultCreneau.get())

        return ResponseEntity.ok(data)
    }
}