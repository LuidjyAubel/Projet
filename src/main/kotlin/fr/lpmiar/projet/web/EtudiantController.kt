package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.EtudiantDao
import fr.lpmiar.projet.model.Etudiant
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
class ParkingController {
    @Autowired
    private lateinit var etudiantDao: EtudiantDao

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
    fun index(@PathVariable numEtudiant: String): ResponseEntity<Any> {
        var p =etudiantDao.findById(numEtudiant)
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("etudiant","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(p)
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
                                        example = "{\"etudiant\":\"not modified\"}" )
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
            return  ResponseEntity(hashMapOf<String,String>(Pair("etudiant","invalide")), HttpStatus.BAD_REQUEST)
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
                        )])
    )
    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable numEtudiant: String):ResponseEntity<Any> {
        var resultEtudiant = etudiantDao.findById(numEtudiant)
        if (resultEtudiant.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("etudiant","not found")), HttpStatus.NOT_FOUND)
        etudiantDao.deleteById(numEtudiant)
        return ResponseEntity.ok(resultEtudiant)
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
}
