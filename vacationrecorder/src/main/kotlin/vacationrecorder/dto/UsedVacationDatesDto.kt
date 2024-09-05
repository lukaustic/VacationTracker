package vacationrecorder.dto

import com.opencsv.bean.CsvBindByName

data class UsedVacationDatesDto(
    @CsvBindByName(column = "Employee")
    val email: String? = null,
    @CsvBindByName(column = "Vacation start date")
    val startDate: String? = null,
    @CsvBindByName(column = "Vacation end date")
    val endDate: String? = null
)