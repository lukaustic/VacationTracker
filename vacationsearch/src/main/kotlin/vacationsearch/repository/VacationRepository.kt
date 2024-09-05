package vacationsearch.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import vacationsearch.entity.Employee
import vacationsearch.entity.Vacation
import java.util.*

@Repository
interface VacationRepository: JpaRepository<Vacation, Long> {
    @Query("SELECT vacation FROM Vacation vacation WHERE vacation.employee.email = ?1")
    fun findDaysByEmployee(employee: String): Optional<List<Vacation>>

    @Query("SELECT vacation FROM Vacation vacation WHERE vacation.employee.email = ?1 AND vacation.year = ?2")
    fun findDaysByEmployeeAndYear(employee: String, year: Int): Optional<Vacation>

    @Transactional
    @Modifying
    @Query("UPDATE Vacation vacation SET vacation.remainingVacationDays = vacation.remainingVacationDays - ?2 WHERE vacation.employee.email = ?1")
    fun updateRemainingTotalVacationDays(employee: String, between: Long)
}