package fr.lpmiar.projet.web

import fr.lpmiar.projet.dao.ProfDao
import fr.lpmiar.projet.model.Groupe
import fr.lpmiar.projet.model.Prof
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/prof")
class ProfController {
    @Autowired
    private lateinit var profDao : ProfDao

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
    fun index(): List<Prof> = profDao.findAll()



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
    fun index(@PathVariable numProf: String): ResponseEntity<Any> {
        var p =profDao.findById(numProf)
        if (p==null)
            return ResponseEntity(hashMapOf<String,String>(Pair("prof","not found")), HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(p)
    }

    @GetMapping("/{id}/favoris")
    fun getGroupesProfs(@PathVariable numProf: String): ResponseEntity<List<Groupe>> {
        var p =profDao.findById(numProf).orElseThrow()
        if (p==null)
            return ResponseEntity.notFound().build()
        var q = p.favoris
        return ResponseEntity.ok(q)
    }

}