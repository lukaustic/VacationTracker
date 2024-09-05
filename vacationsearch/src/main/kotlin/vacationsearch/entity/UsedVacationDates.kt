package vacationsearch.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "used_vacation_dates")
data class UsedVacationDates(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    val employee: Employee? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
) {
    constructor(employee: Employee, startDate: LocalDate, endDate: LocalDate): this(null, employee, startDate, endDate)
}