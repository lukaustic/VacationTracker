package vacationrecorder.dto

import com.opencsv.bean.CsvBindByName

data class VacationDto(
    @CsvBindByName(column = "Employee")
    val email: String? = null,
    @CsvBindByName(column = "Total vacation days")
    val totalVacationDays: Int? = null
)