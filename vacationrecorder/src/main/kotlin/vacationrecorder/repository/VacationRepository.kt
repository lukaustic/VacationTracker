package vacationrecorder.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import vacationrecorder.entity.Employee
import vacationrecorder.entity.Vacation

@Repository
interface VacationRepository: JpaRepository<Vacation, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Vacation vacation SET vacation.remainingVacationDays = vacation.remainingVacationDays - ?2 WHERE vacation.employee = ?1 AND vacation.year = ?3")
    fun updateRemainingVacationDaysByYear(employee: Employee, usedVacationDays: Int, year: Int)
}