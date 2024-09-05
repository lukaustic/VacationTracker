package vacationsearch.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import vacationsearch.dto.EmployeeDto
import vacationsearch.repository.EmployeeRepository

@Service
class AuthenticationService(
    @Autowired
    private val employeeRepository: EmployeeRepository,
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val authenticationManager: AuthenticationManager
) {
    fun authenticate(request: EmployeeDto): String {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )
        val employee = employeeRepository.findEmployeeByEmail(request.email).get()
        return jwtService.generateToken(employee)
    }
}