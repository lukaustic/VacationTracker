package vacationsearch.service

import org.hibernate.NotImplementedYetException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import vacationsearch.dto.UsedVacationDatesDto
import vacationsearch.dto.VacationDto
import vacationsearch.entity.Employee
import vacationsearch.entity.UsedVacationDates
import vacationsearch.repository.UsedVacationDatesRepository
import vacationsearch.repository.VacationRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Service
class VacationService(
    @Autowired
    private val vacationRepository: VacationRepository,
    @Autowired
    private val usedVacationDatesRepository: UsedVacationDatesRepository
) {
    fun getDaysByEmployee(employee: String): List<VacationDto> {
        val totalVacationDays = vacationRepository.findDaysByEmployee(employee).get()
        val totalVacationDaysDto = ArrayList<VacationDto>()

        for (vacationDays in totalVacationDays) {
            totalVacationDaysDto.add(VacationDto(vacationDays.year, vacationDays.totalVacationDays, vacationDays.remainingVacationDays))
        }

        return totalVacationDaysDto
    }
    fun getVacationsByEmployeeAndRange(employee: String, fromDate: LocalDate, toDate: LocalDate): List<UsedVacationDatesDto> {
        val vacations: List<UsedVacationDates> = usedVacationDatesRepository.findVacationsByEmployeeAndRange(employee, fromDate, toDate).get()
        val vacationsDto: MutableList<UsedVacationDatesDto> = mutableListOf()

        for (vacation in vacations) {
            vacationsDto.add(UsedVacationDatesDto(vacation.startDate.toString(), vacation.endDate.toString()))
        }

        return vacationsDto
    }
    fun createVacation(employee: String, fromDate: String, toDate: String)/*: UsedVacationDates */{
        val from: LocalDate
        val to: LocalDate

        try {
            from = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
            to = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
        } catch (e: Exception) {
            throw IllegalStateException("Invalid date")
        }

        if (from.year != to.year) {
            throw IllegalStateException("Vacation must start and end in the same year")
        }

        if (to.isBefore(from)) {
            throw IllegalArgumentException("Vacation end day cannot come before the starting day")
        }

        val totalVacationDaysOptional = vacationRepository.findDaysByEmployeeAndYear(employee, from.year)

        if (!totalVacationDaysOptional.isPresent || totalVacationDaysOptional.get().remainingVacationDays!! < ChronoUnit.DAYS.between(from, to)) {
            throw IllegalStateException("Not enough remaining vacation days left")
        }

        val vacationOptional = usedVacationDatesRepository.findVacationsByEmployeeAndRange(employee, from, to)

        if (vacationOptional.isPresent && vacationOptional.get().isNotEmpty()) {
            throw IllegalStateException("Already taken vacation for asked days")
        }

        //val vacation = UsedVacationDates(employee, from, to)

        if (from == to) {
            vacationRepository.updateRemainingTotalVacationDays(employee, 1)
        } else {
            vacationRepository.updateRemainingTotalVacationDays(employee, ChronoUnit.DAYS.between(from, to))
        }

        //usedVacationDatesRepository.save(vacation)

        //return vacation
    }
}