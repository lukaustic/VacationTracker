package vacationrecorder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import vacationrecorder.dto.EmployeeDto
import vacationrecorder.dto.VacationDto
import vacationrecorder.dto.UsedVacationDatesDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SpringBootApplication
class VacationRecorderApplication

fun main(args: Array<String>) {
	runApplication<VacationRecorderApplication>(*args)
}

fun EmployeeDto.validateEmployeeDto() {
	this.email?.validateEmail()
	this.password?.validatePassword()
}

fun String.validateEmail() {
	if (this.isEmpty()) throw IllegalStateException("Email is required")
	if (!Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}").matches(this)) throw IllegalStateException("Invalid email address '$this'")
}

fun String.validatePassword() {
	if(this.isEmpty()) throw IllegalStateException("Email is required")
	if(this.length <6 || this.length > 20) throw IllegalStateException("Bad password: '${this}', password must have between 6 and 20 characters")
}

fun String.parseDate(datePattern: String): LocalDate {
	try {
		return LocalDate.parse(this, DateTimeFormatter.ofPattern(datePattern))
	} catch (e: Exception) {
		throw IllegalStateException("Invalid dates '$this'")
	}
}

fun UsedVacationDatesDto.validateUsedVacationDatesDto(fromDate: LocalDate, toDate: LocalDate) {
	this.email?.validateEmail()
	if (toDate.isBefore(fromDate)) {
		throw IllegalArgumentException("Vacation end day cannot come before the starting day, '$fromDate', '$toDate'")
	}
	if (fromDate.year != toDate.year) throw IllegalArgumentException("Vacation must start and end in the same year, '$fromDate', '$toDate'")
}

fun String.validateHeader(): Int {
	try {
		return this.substringAfter(",").toInt()
	} catch (e: Exception) {
		throw IllegalStateException("Invalid Header '$this'")
	}
}

fun Int.validateYear() {
	if(this > 2100 || this < 2000) {
		throw IllegalStateException("Year must be between 2000 and 2100")
	}
}

fun VacationDto.validateVacationDto() {
	this.email?.validateEmail()
	this.totalVacationDays?.validateDays(this.email!!)
}

fun Int.validateDays(email: String) {
	if(this > 100 || this < 0 ) {
		throw IllegalStateException("Invalid number of days for '${email}', days must be between 0 and 100")
	}
}