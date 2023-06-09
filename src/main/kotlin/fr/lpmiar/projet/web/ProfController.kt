package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.CreneauDao
import fr.lpmiar.projet.dao.GroupeDao
import fr.lpmiar.projet.dao.ProfDao
import fr.lpmiar.projet.model.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.hibernate.Hibernate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/prof")
class ProfController {
    @Autowired
    private lateinit var profDao : ProfDao
    @Autowired
    private lateinit var groupeDao :GroupeDao
    @Autowired
    private lateinit var creneauDao :CreneauDao
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    var logger: Logger = LoggerFactory.getLogger(ProfController::class.java)

    @Operation(summary = "Method get all Prof")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Prof::class)
                        )
                    ])
    )
    @GetMapping
    fun index(): List<Prof>{
        val profs = profDao.findAll()
        //profs.forEach { it.fetchGroupe() } // fonctionne pas
        profs.forEach { prof ->
            Hibernate.initialize(prof.favoris)
        }
        logger.info("Display all prof")
        return profs
    }

    @Operation(summary = "Method get one Prof")
    @ApiResponses(
            ApiResponse(responseCode = "200",
                    description = "OK",
                    content = [
                        Content(mediaType = "application/json",
                                schema = Schema(implementation = Prof::class)
                        )
                    ])
    )
    @GetMapping("/{id}")
    fun index(@PathVariable id: String): ResponseEntity<Any> {
        val prof = profDao.findById(id)
        return if (prof == null) {
            logger.error("Le prof n'existe pas")
            ResponseEntity(hashMapOf(Pair("error", "prof non trouvé")), HttpStatus.NOT_FOUND)
        } else {
            logger.info("Display the prof with the Id : "+id)
            ResponseEntity.ok(prof)
        }
    }
    @Operation(summary = "Method add a prof")
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
                                        example = "{\"prof\":\"not found\"}" )
                        )])
    )
    @PostMapping
    fun post(@RequestBody(required = false) g: Prof) : ResponseEntity<Any>{
        if (g== null)
            logger.error("le prof n'existe pas")
            return  ResponseEntity(hashMapOf<String,String>(Pair("prof","invalide")), HttpStatus.BAD_REQUEST)
        try {
            g.password = passwordEncoder.encode(g.password)
            profDao.save(g)
            logger.info("Prof enregistré avec succès !")
        } catch (e : Exception) {
            logger.error("le prof n'as pas était créer")
            return ResponseEntity(hashMapOf<String,String>(Pair("prof","not created")), HttpStatus.NOT_MODIFIED)
        }
        var resultGroupe = g.id?.let { profDao.findById(it) }
        if (resultGroupe==null)
            logger.error("le prof n'as pas était créer")
            return ResponseEntity(hashMapOf<String,String>(Pair("prof","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(resultGroupe)
    }
    @Operation(summary = "Method get the favoris of a prof with his id")
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
    @GetMapping("/{id}/favoris")
    fun getGroupesProfs(@PathVariable id: String): ResponseEntity<Any> {
        var p =profDao.findById(id)
        val groupes : MutableList<Groupe> = mutableListOf()
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("prof","not found")), HttpStatus.NOT_FOUND)
        profDao.findAll().forEach { prof ->
            if(prof.id == id){
                groupes.addAll(prof.favoris)
            }
        }
        if (groupes.isEmpty()){
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found")), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(groupes)
    }
    @Operation(summary = "Method get all creneau for a prof with his id")
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
    @GetMapping("/{id}/creneaux")
    fun getCreneauProf(@PathVariable id: String): ResponseEntity<Any> {
        var p =profDao.findById(id)
        val creneaux : MutableList<Creneau> = mutableListOf()
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("prof","not found")), HttpStatus.NOT_FOUND)
        creneauDao.findAll().forEach { creneau ->
            profDao.findAll().forEach{prof ->
                if(creneau.nomProf == prof.username){
                    creneaux.add(creneau)
                }
            }
        }
        if (creneaux.isEmpty()){
            return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found")), HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(creneaux)
    }
    @Operation(summary = "Method get a creneau for a prof with his id")
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
    @GetMapping("/{id}/creneaux/{num}")
    fun getCreneau(@PathVariable id: String, @PathVariable num : String): ResponseEntity<Any> {
        var p =profDao.findById(id)
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("prof","not found")), HttpStatus.NOT_FOUND)
        creneauDao.findAll().forEach { creneau ->
            profDao.findAll().forEach{prof ->
                if(creneau.nomProf == prof.username){
                    if (creneau.idCreneau == num){
                        return ResponseEntity.ok(creneau)
                    }
                }
            }
        }
            return ResponseEntity(hashMapOf<String,String>(Pair("creneau","not found")), HttpStatus.NOT_FOUND)
    }
    @Operation(summary = "Method get a favoris for a prof with his id")
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
    @GetMapping("/{id}/favoris/{num}")
    fun getGroupeProfs(@PathVariable id: String, @PathVariable num: String): ResponseEntity<Any> {
        var p =profDao.findById(id)
        var f = groupeDao.findById(num).orElseThrow()
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("prof","not found")), HttpStatus.NOT_FOUND)
        profDao.findAll().forEach { prof ->
            for(item in prof.favoris){
                if(item == f){
                    return ResponseEntity.ok(prof.favoris)
                }
            }
        }
         return ResponseEntity(hashMapOf<String,String>(Pair("groupe","not found !")), HttpStatus.NOT_FOUND)
    }
    @Operation(summary = "Method get the prof who was connected")
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
                        example = "{\"prof\":\"not found\"}" )
                )])
    )
    @GetMapping("/current")
    fun getCurrentProf(): ResponseEntity<Any> {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.isAuthenticated) {
            val username = authentication.name
            val prof: Prof? = profDao.findByUsername(username)
            if (prof != null) {
                return ResponseEntity.ok(prof)
            }
        }
        return ResponseEntity(hashMapOf<String, String>(Pair("prof", "not found !")), HttpStatus.NOT_FOUND)
    }
    @PostMapping("/{profId}/favoris")
    fun addFavoris(@PathVariable profId: String, @RequestBody groupe: Groupe): ResponseEntity<Any> {
        val prof: Prof? = profDao.findById(profId).orElse(null)
        if (prof != null) {
            prof.favoris.add(groupe)
            profDao.save(prof)
            return ResponseEntity.ok("Favoris ajoutés avec succès")
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
    @Operation(summary = "Method update a prof with his id")
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
                                        example = "{\"prof\":\"not found\"}" )
                        )])
    )
    @PutMapping("/{id}")
    fun update(@PathVariable id: String,@RequestBody data:Prof): ResponseEntity<Any>{

        var resultatprof = profDao.findById(id)
        if (resultatprof.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("prof","not found")), HttpStatus.NOT_FOUND)
        resultatprof = Optional.of(data)
        profDao.save(resultatprof.get())

        return ResponseEntity.ok(data)
    }
    @Operation(summary = "Method delete a prof with his id")
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
                                        example = "{\"prof\":\"not found\"}" )
                        )])
    )
    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable id: String):ResponseEntity<Any> {
        var resultatprof = profDao.findById(id)
        if (resultatprof.isEmpty)
            return ResponseEntity(hashMapOf<String,String>(Pair("prof","not found")), HttpStatus.NOT_FOUND)
        profDao.deleteById(id)
        return ResponseEntity.ok(resultatprof)
    }
}