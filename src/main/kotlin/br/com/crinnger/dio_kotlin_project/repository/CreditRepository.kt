package br.com.crinnger.dio_kotlin_project.repository

import br.com.crinnger.dio_kotlin_project.model.Credit
import br.com.crinnger.dio_kotlin_project.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CreditRepository: JpaRepository<Credit,Long>{

    fun findByCreditCode(creditCode: UUID) : Credit?

    @Query(value = "SELECT * FROM CREDIT WHERE CUSTOMER_ID = ?1", nativeQuery = true)
    fun findAllByCustomerId(customerId: Long): List<Credit>
}