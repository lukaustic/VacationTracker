package vacationrecorder.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import vacationrecorder.entity.Employee
import vacationrecorder.entity.UsedVacationDates
import vacationrecorder.entity.Vacation
import vacationrecorder.service.VacationService

@RestController
@RequestMapping("/api/v1")
class VacationController(
    @Autowired
    private val vacationService: VacationService
) {
    @PostMapping("/employees")
    fun uploadEmployees(
        @RequestParam("employee_profiles") file: MultipartFile
    ): ResponseEntity<List<Employee>> {
        val importedEntries = vacationService.uploadEmployeesFile(file)
        return ResponseEntity.status(HttpStatus.CREATED).body(importedEntries)
    }

    @PostMapping("/vacations")
    fun uploadVacations(
        @RequestParam("vacations") file: MultipartFile
    ): ResponseEntity<List<Vacation>>{
        val importedEntries = vacationService.uploadVacationsFile(file)
        return ResponseEntity.status(HttpStatus.CREATED).body(importedEntries)
    }

    @PostMapping("/used-vacation-dates")
    fun uploadUsedVacationDates(
        @RequestParam("used_vacation_dates") file: MultipartFile
    ): ResponseEntity<List<UsedVacationDates>>{
        val importedEntries = vacationService.uploadUsedVacationDatesFile(file)
        return ResponseEntity.status(HttpStatus.CREATED).body(importedEntries)
    }
}