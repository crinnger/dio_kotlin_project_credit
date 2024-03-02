package br.com.crinnger.dio_kotlin_project.service

import br.com.crinnger.dio_kotlin_project.dto.CustomerDto
import br.com.crinnger.dio_kotlin_project.dto.CustomerUpdateDto
import br.com.crinnger.dio_kotlin_project.dto.CustomerView
import br.com.crinnger.dio_kotlin_project.model.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun update(customerUpdated: Customer): Customer
    fun findById(customerId: Long): Customer
    fun delete(customerId: Long)

}