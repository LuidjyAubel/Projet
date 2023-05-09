package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.CoursDao
import fr.lpmiar.projet.dao.CreneauDao
import fr.lpmiar.projet.model.Cours
import fr.lpmiar.projet.model.Creneau
import fr.lpmiar.projet.model.Prof
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/cours")
class CoursController {
    @Autowired
    private lateinit var coursDao: CoursDao
    @Autowired
    private lateinit var creneauDao: CreneauDao

    @Operation(summary = "Method get all Cours")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Cours::class)
                )
            ])
    )
    @GetMapping
    fun index(): List<Cours>{
        val resCours = coursDao.findAll()
        resCours.forEach { cours ->
            Hibernate.initialize(cours.creneaux)
        }
        return resCours
    }

    @Operation(summary = "Method get a cours with his id")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Cours::class)
                )
            ]),
        ApiResponse(responseCode = "404",
            description = "Not Found",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"cours\":\"not found\"}" )
                )])
    )
    @GetMapping("/{id}")
    fun index(@PathVariable id: String): ResponseEntity<Any> {
        val cours = coursDao.findById(id)
        return if (cours == null) {
            ResponseEntity(hashMapOf(Pair("error", "cours non trouvé")), HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok(cours)
        }
    }
    @Operation(summary = "Method add a cours")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Cours::class)
                )
            ]),
        ApiResponse(responseCode = "404",
            description = "Not Found",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"cours\":\"not found\"}" )
                )])
    )
    @PostMapping
    fun post(@RequestBody(required = false) g: Cours?) : ResponseEntity<Any>{
        if (g== null)
            return  ResponseEntity(hashMapOf<String,String>(Pair("cours","invalide")), HttpStatus.BAD_REQUEST)
        try {
            coursDao.save(g)
        } catch (e : Exception) {
            return ResponseEntity(hashMapOf<String,String>(Pair("cours","not created")), HttpStatus.NOT_MODIFIED)
        }
        var resultGroupe = g.id?.let { coursDao.findById(it) }
        if (resultGroupe==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("cours","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(resultGroupe)
    }
    @Operation(summary = "Method get the creneau of a cours with his id")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Cours::class)
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
    @GetMapping("/{id}/creneaux")
    fun getCreneauxCours(@PathVariable id: String): ResponseEntity<Any> {
        var p =coursDao.findById(id)
        val creneau : MutableList<Creneau> = mutableListOf()
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("cours","not found")), HttpStatus.NOT_FOUND)
        coursDao.findAll().forEach { cours ->
            if(cours.id == id){
                creneau.addAll(cours.creneaux)
            }
        }
        if (creneau.isEmpty()){
            return ResponseEntity(hashMapOf<String,String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(creneau)
    }
    @Operation(summary = "Method get a creneau for a cours with his id")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Cours::class)
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
    @GetMapping("/{id}/creneaux/{num}")
    fun getCreneaux(@PathVariable id: String, @PathVariable num: String): ResponseEntity<Any> {
        var p = coursDao.findById(id)
        var f = creneauDao.findById(num).orElseThrow()
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("cours","not found")), HttpStatus.NOT_FOUND)
        coursDao.findAll().forEach { cours ->
            for(item in cours.creneaux){
                if(item == f){
                    return ResponseEntity.ok(cours.creneaux)
                }
            }
        }
        return ResponseEntity(hashMapOf<String,String>(Pair("creneau","not found !")), HttpStatus.NOT_FOUND)
    }
    @Operation(summary = "Method update a cours with his id")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Prof::class)
                )
            ]),
        ApiResponse(responseCode = "404",
            description = "Not Found",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"cours\":\"not found\"}" )
                )])
    )
    @PutMapping("/{id}")
    fun update(@PathVariable id: String,@RequestBody data:Cours): ResponseEntity<Any>{

        var resultatcours = coursDao.findById(id)
        if (resultatcours.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("cours","not found")), HttpStatus.NOT_FOUND)
        resultatcours = Optional.of(data)
        coursDao.save(resultatcours.get())

        return ResponseEntity.ok(data)
    }
    @Operation(summary = "Method delete a cours with his id")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Cours::class)
                )
            ]),
        ApiResponse(responseCode = "404",
            description = "Not Found",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"cours\":\"not found\"}" )
                )])
    )
    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable id: String):ResponseEntity<Any> {
        var resultcours = coursDao.findById(id)
        if (resultcours.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("cours","not found")), HttpStatus.NOT_FOUND)
        coursDao.deleteById(id)
        return ResponseEntity.ok(resultcours)
    }
}