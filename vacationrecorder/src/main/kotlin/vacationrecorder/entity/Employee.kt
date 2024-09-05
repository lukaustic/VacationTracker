package vacationrecorder.entity

import jakarta.persistence.*

@Entity
@Table(name= "employee_profiles")
data class Employee(
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    val id: Long? = null,
    val email: String? = null,
    val password: String? = null
) {
    constructor(email: String, passwordHash: String) : this(null, email, passwordHash)
}