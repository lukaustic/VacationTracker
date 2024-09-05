package vacationsearch.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import vacationsearch.entity.Employee
import java.util.*

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {
    @Query("SELECT employee FROM Employee employee WHERE employee.email = ?1")
    fun findEmployeeByEmail(email: String): Optional<Employee>
}