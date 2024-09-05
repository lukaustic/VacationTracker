package vacationrecorder.dto

import com.opencsv.bean.CsvBindByName

data class EmployeeDto(
    @CsvBindByName(column = "Employee Email")
    val email: String? = null,
    @CsvBindByName(column = "Employee Password")
    val password: String? = null
)