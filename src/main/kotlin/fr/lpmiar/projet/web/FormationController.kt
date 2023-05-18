package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.CoursDao
import fr.lpmiar.projet.dao.FormationDao
import fr.lpmiar.projet.model.*
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
@RequestMapping("/formation")
class FormationController {
    @Autowired
    private lateinit var formationDao : FormationDao
    @Autowired
    private  lateinit var coursDao : CoursDao

    @Operation(summary = "Method get all formation")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Formation::class)
                )
            ])
    )
    @GetMapping
    fun index(): List<Formation>{
        val resFormation = formationDao.findAll()
        resFormation.forEach { form ->
            Hibernate.initialize(form.CoursFormation)
        }
        return resFormation
    }
    @Operation(summary = "Method get a formation with the id")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Formation::class)
                )
            ]),
        ApiResponse(responseCode = "404",
            description = "Not Found",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"formation\":\"not found\"}" )
                )])
    )
    @GetMapping("/{id}")
    fun index(@PathVariable id: String): ResponseEntity<Any> {
        val formation = formationDao.findById(id)
        return if (formation == null) {
            ResponseEntity(hashMapOf(Pair("formation", "not found")), HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok(formation)
        }
    }
    @Operation(summary = "Method get the cours of a formation with his id")
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
                        example = "{\"formation\":\"not found\"}" )
                )])
    )
    @GetMapping("/{id}/cours")
    fun getCreneauxCours(@PathVariable id: String): ResponseEntity<Any> {
        var p =formationDao.findById(id)
        val formationlist : MutableList<Cours> = mutableListOf()
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("formation","not found")), HttpStatus.NOT_FOUND)
        formationDao.findAll().forEach { formation ->
            if(formation.id == id){
                formationlist.addAll(formation.CoursFormation)
            }
        }
        if (formationlist.isEmpty()){
            return ResponseEntity(hashMapOf<String,String>(Pair("cours","not found")), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(formationlist)
    }
    @Operation(summary = "Method get a cours for a formation with his id")
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
                        example = "{\"formation\":\"not found\"}" )
                )])
    )
    @GetMapping("/{id}/cours/{num}")
    fun getCreneaux(@PathVariable id: String, @PathVariable num: String): ResponseEntity<Any> {
        var p = formationDao.findById(id)
        var f = coursDao.findById(num).orElseThrow()
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("formation","not found")), HttpStatus.NOT_FOUND)
        formationDao.findAll().forEach { form ->
            for(item in form.CoursFormation){
                if(item.id == f.id){
                    return ResponseEntity.ok(form.CoursFormation)
                }
            }
        }
        return ResponseEntity(hashMapOf<String,String>(Pair("cours","not found !")), HttpStatus.NOT_FOUND)
    }
    @Operation(summary = "Method for creating an formation")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Formation::class)
                )
            ]),
        ApiResponse(responseCode = "400",
            description = "Bad Request",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"formation\":\"bad request\"}" )
                )]),
        ApiResponse(responseCode = "304",
            description = "Not Modified",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"formation\":\"not created\"}" )
                )]),
        ApiResponse(responseCode = "404",
            description = "Not Found",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"formation\":\"not found\"}" )
                )])
    )
    @PostMapping
    fun post(@RequestBody(required = false) p: Formation?) : ResponseEntity<Any> {
        if (p== null)
            return  ResponseEntity(hashMapOf<String,String>(Pair("formation","bad request")), HttpStatus.BAD_REQUEST)
        try {
            formationDao.save(p)
        } catch (e : Exception) {
            return ResponseEntity(hashMapOf<String,String>(Pair("formation","not created")), HttpStatus.NOT_MODIFIED)
        }

        var resFormation = p.id?.let { formationDao.findById(it) }
        if (resFormation==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("formation","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(resFormation)
    }
    @Operation(summary = "Method update a formation with his id")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Formation::class)
                )
            ]),
        ApiResponse(responseCode = "404",
            description = "Not Found",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"formation\":\"not found\"}" )
                )])
    )
    @PutMapping("/{id}")
    fun update(@PathVariable id: String,@RequestBody data:Formation): ResponseEntity<Any>{

        var resultatFormation = formationDao.findById(id)
        if (resultatFormation.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("formation","not found")), HttpStatus.NOT_FOUND)
        resultatFormation = Optional.of(data)
        formationDao.save(resultatFormation.get())

        return ResponseEntity.ok(data)
    }
    @Operation(summary = "Method delete a formation with his id")
    @ApiResponses(
        ApiResponse(responseCode = "200",
            description = "OK",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = Formation::class)
                )
            ]),
        ApiResponse(responseCode = "404",
            description = "Not Found",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(type = "object",
                        example = "{\"formation\":\"not found\"}" )
                )])
    )
    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable id: String):ResponseEntity<Any> {
        var resultatFormation = formationDao.findById(id)
        if (resultatFormation.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("formation","not found")), HttpStatus.NOT_FOUND)
        formationDao.deleteById(id)
        return ResponseEntity.ok(resultatFormation)
    }
}