package vacationsearch.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import vacationsearch.entity.UsedVacationDates
import java.time.LocalDate
import java.util.*

@Repository
interface UsedVacationDatesRepository: JpaRepository<UsedVacationDates, Long> {
    @Query("SELECT usedVacationDates FROM UsedVacationDates usedVacationDates WHERE usedVacationDates.employee.email = ?1 AND (usedVacationDates.startDate >= ?2 AND usedVacationDates.startDate <= ?3) OR (usedVacationDates.endDate >= ?2 AND usedVacationDates.endDate <= ?3) OR (usedVacationDates.startDate >= ?2 AND usedVacationDates.endDate <= ?3)")
    fun findVacationsByEmployeeAndRange(employee: String, fromDate: LocalDate, toDate: LocalDate): Optional<List<UsedVacationDates>>
}