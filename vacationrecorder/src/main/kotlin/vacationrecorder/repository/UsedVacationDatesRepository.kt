package vacationrecorder.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vacationrecorder.entity.UsedVacationDates

@Repository
interface UsedVacationDatesRepository: JpaRepository<UsedVacationDates, Long>