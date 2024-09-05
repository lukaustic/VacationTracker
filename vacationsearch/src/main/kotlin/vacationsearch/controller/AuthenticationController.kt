package vacationsearch.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vacationsearch.dto.EmployeeDto
import vacationsearch.service.AuthenticationService

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(
    @Autowired
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: EmployeeDto): ResponseEntity<String> {
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }
}