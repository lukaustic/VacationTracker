package vacationrecorder.service

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import vacationrecorder.*
import vacationrecorder.dto.EmployeeDto
import vacationrecorder.dto.VacationDto
import vacationrecorder.dto.UsedVacationDatesDto
import vacationrecorder.entity.Employee
import vacationrecorder.entity.UsedVacationDates
import vacationrecorder.entity.Vacation
import vacationrecorder.repository.EmployeeRepository
import vacationrecorder.repository.VacationRepository
import vacationrecorder.repository.UsedVacationDatesRepository
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.time.temporal.ChronoUnit

@Service
class VacationService(
    @Autowired
    private val employeeRepository: EmployeeRepository,
    @Autowired
    private val vacationRepository: VacationRepository,
    @Autowired
    private val usedVacationDatesRepository: UsedVacationDatesRepository,
    @Autowired
    private val passwordEncoder: PasswordEncoder
) {
    @Value("\${DATE_PATTERN}")
    private val datePattern: String? = null

    private fun throwIfFileEmpty(file: MultipartFile) {
        if (file.isEmpty)
            throw IllegalStateException("Empty file")
    }

    private fun createEmployeesCSVToBean(fileReader: BufferedReader?): CsvToBean<EmployeeDto> =
        CsvToBeanBuilder<EmployeeDto>(fileReader)
            .withType(EmployeeDto::class.java)
            .withSkipLines(1)
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private fun createVacationsCSVToBean(fileReader: BufferedReader?): CsvToBean<VacationDto> =
        CsvToBeanBuilder<VacationDto>(fileReader)
            .withType(VacationDto::class.java)
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private fun createUsedVacationDatesCSVToBean(fileReader: BufferedReader?): CsvToBean<UsedVacationDatesDto> =
        CsvToBeanBuilder<UsedVacationDatesDto>(fileReader)
            .withType(UsedVacationDatesDto::class.java)
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private fun closeFileReader(fileReader: BufferedReader?) {
        try {
            fileReader!!.close()
        } catch (ex: IOException) {
            throw IllegalStateException("Error during csv import")
        }
    }

    fun uploadEmployeesFile(file: MultipartFile): List<Employee> {
        throwIfFileEmpty(file)
        var fileReader: BufferedReader? = null
        val employees: MutableList<Employee> = mutableListOf()
        val checkEmployees = employeeRepository.findAll()

        try {
            fileReader = BufferedReader(InputStreamReader(file.inputStream))
            val csvToBean = createEmployeesCSVToBean(fileReader).parse()

            csvToBean.forEach { employeeDto ->
                employeeDto.validateEmployeeDto()
                checkEmployees.forEach { checkEmployee ->
                    if(checkEmployee.email == employeeDto.email) throw IllegalStateException("Email ''${checkEmployee.email}'' taken")
                }
                employees.add(Employee(employeeDto.email!!, passwordEncoder.encode(employeeDto.password)))
            }
            employeeRepository.saveAll(employees)
            return employees
        } catch (e: InvocationTargetException) {
            throw e
        }
        finally {
            closeFileReader(fileReader)
        }
    }

    fun uploadUsedVacationDatesFile(file: MultipartFile): List<UsedVacationDates> {
        throwIfFileEmpty(file)
        var fileReader: BufferedReader? = null
        val usedVacations: MutableList<UsedVacationDates> = mutableListOf()
        val checkEmployees = employeeRepository.findAll()
        val checkTotalVacationDays = vacationRepository.findAll()
        val checkVacations = usedVacationDatesRepository.findAll()

        try {
            fileReader = BufferedReader(InputStreamReader(file.inputStream))
            val csvToBean = createUsedVacationDatesCSVToBean(fileReader).parse()

            csvToBean.forEach { usedVacationDatesDto ->
                var employee: Employee? = null
                val fromDate = usedVacationDatesDto.startDate?.parseDate(datePattern!!)
                val toDate = usedVacationDatesDto.endDate?.parseDate(datePattern!!)
                usedVacationDatesDto.validateUsedVacationDatesDto(fromDate!!, toDate!!)

                checkVacations.forEach { checkVacation ->
                    if (checkVacation.employee?.email == usedVacationDatesDto.email && (
                                (checkVacation.startDate!! >= fromDate && checkVacation.startDate <= toDate) ||
                                        (checkVacation.endDate!! >= fromDate && checkVacation.endDate <= toDate) ||
                                        (checkVacation.startDate >= fromDate && checkVacation.endDate <= toDate)
                                )
                    ) throw IllegalStateException("Already taken vacation for asked days")

                }
                checkTotalVacationDays.forEach { checkTotalVacationDay ->
                    if (checkTotalVacationDay.employee?.email == usedVacationDatesDto.email &&
                        checkTotalVacationDay.year == fromDate.year && checkTotalVacationDay.remainingVacationDays!! < ChronoUnit.DAYS.between(
                            fromDate,
                            toDate
                        )
                    )
                        throw IllegalStateException("not enough remaining vacation days left ${checkTotalVacationDay.employee?.email}, $fromDate")
                }
                checkEmployees.forEach { checkEmployee ->
                    if (checkEmployee.email == usedVacationDatesDto.email) employee = checkEmployee
                }
                if (employee == null) throw IllegalStateException("Email ''${usedVacationDatesDto.email}'' does not exist")
                else usedVacations.add(UsedVacationDates(employee!!, fromDate, toDate))
                if(fromDate == toDate) {
                    vacationRepository.updateRemainingVacationDaysByYear(employee!!, 1, fromDate.year)
                } else {
                    vacationRepository.updateRemainingVacationDaysByYear(
                        employee!!,
                        ChronoUnit.DAYS.between(fromDate, toDate).toInt(),
                        fromDate.year
                    )
                }
            }
            usedVacationDatesRepository.saveAll(usedVacations)
            return usedVacations
        } catch (e: Exception) {
            throw e
        }
        finally {
            closeFileReader(fileReader)
        }
    }

    fun uploadVacationsFile(file: MultipartFile): List<Vacation> {
        throwIfFileEmpty(file)
        var fileReader: BufferedReader? = null
        val vacations: MutableList<Vacation> = mutableListOf()
        val checkEmployees = employeeRepository.findAll()
        val checkTotalVacationDays = vacationRepository.findAll()

        try {
            fileReader = BufferedReader(InputStreamReader(file.inputStream))
            val year = fileReader.readLine().validateHeader()
            year.validateYear()
            val csvToBean = createVacationsCSVToBean(fileReader).parse()

            csvToBean.forEach { usedVacationDates ->
                var employee: Employee? = null
                usedVacationDates.validateVacationDto()
                checkTotalVacationDays.forEach { checkTotalVacationDay ->
                    if(checkTotalVacationDay.employee?.email == usedVacationDates.email && checkTotalVacationDay.year == year)
                        throw IllegalStateException("Days for ${checkTotalVacationDay.year} for ${checkTotalVacationDay.employee?.email} already added")
                }
                checkEmployees.forEach { checkEmployee ->
                    if(checkEmployee.email == usedVacationDates.email) employee = checkEmployee
                }
                if(employee == null) throw IllegalStateException("Email ''${usedVacationDates.email}'' does not exist")
                else  vacations.add(Vacation(employee!!, year, usedVacationDates.totalVacationDays!!, usedVacationDates.totalVacationDays))
            }
            vacationRepository.saveAll(vacations)
            return vacations

        } catch (e: Exception) {
            throw e
        }
        finally {
            closeFileReader(fileReader)
        }
    }
}