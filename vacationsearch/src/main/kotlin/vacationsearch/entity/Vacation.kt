package vacationsearch.entity

import jakarta.persistence.*

@Entity
@Table(name = "vacations")
data class Vacation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    val employee: Employee? = null,
    val year: Int? = null,
    val totalVacationDays: Int? = null,
    val remainingVacationDays: Int? = null
) {
    constructor(employee: Employee, year: Int, totalVacationDays: Int, remainingVacationDays: Int): this(null, employee, year, totalVacationDays, remainingVacationDays)
}