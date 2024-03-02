package br.com.crinnger.dio_kotlin_project.service

import br.com.crinnger.dio_kotlin_project.model.Credit
import java.util.UUID


interface ICreditService {
    fun save(credit: Credit): Credit

    fun findAllByCustomer(customerId: Long): List<Credit>

    fun findByCreditCode(creditCode: UUID,customerId:Long): Credit
}