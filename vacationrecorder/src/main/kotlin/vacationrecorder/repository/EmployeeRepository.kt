package vacationrecorder.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vacationrecorder.entity.Employee

@Repository
interface EmployeeRepository: JpaRepository<Employee, Long>