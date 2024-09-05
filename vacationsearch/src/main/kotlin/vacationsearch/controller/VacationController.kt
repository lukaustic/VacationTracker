package vacationsearch.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import vacationsearch.dto.UsedVacationDatesDto
import vacationsearch.dto.VacationDto
import vacationsearch.entity.UsedVacationDates
import vacationsearch.service.VacationService
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1")
class VacationController(
    @Autowired
    private val vacationService: VacationService
) {
    @GetMapping("/vacations")
    fun getVacations(): List<VacationDto> {
        val authentication = SecurityContextHolder.getContext().authentication
        val employee = (authentication.principal as UserDetails).username
        return vacationService.getDaysByEmployee(employee)
    }
    @GetMapping("/used-vacation-dates")
    fun getUsedVacationDates(
        @RequestParam("From-Date") fromDate: LocalDate,
        @RequestParam("To-Date") toDate: LocalDate
    ): List<UsedVacationDatesDto> {

        val authentication = SecurityContextHolder.getContext().authentication
        val employee = (authentication.principal as UserDetails).username

        return vacationService.getVacationsByEmployeeAndRange(employee, fromDate, toDate);
    }
    @PostMapping("/vacation")
    fun createVacation(
    @RequestBody usedVacationDatesDto: UsedVacationDatesDto
    )/*: ResponseEntity<UsedVacationDates> */{

        val authentication = SecurityContextHolder.getContext().authentication
        val employee = (authentication.principal as UserDetails).username

        val vacation = vacationService.createVacation(employee, usedVacationDatesDto.startDate!!,
            usedVacationDatesDto.endDate!!
        )
        //return ResponseEntity.status(HttpStatus.CREATED).body(vacation);
    }
}