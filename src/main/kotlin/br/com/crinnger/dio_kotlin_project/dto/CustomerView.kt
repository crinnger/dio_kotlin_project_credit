package br.com.crinnger.dio_kotlin_project.dto

import br.com.crinnger.dio_kotlin_project.model.Customer
import jakarta.persistence.*
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal

data class CustomerView(
        val firstName: String? = null,
        val lastName:String,
        val income:BigDecimal,
        val email: String,
        val cpf: String,
        val street: String,
        val zipCode: String,
        val id:Long?
) {
    constructor(customer: Customer): this(
        firstName=customer.firstName,
        lastName=customer.lastName,
        income=customer.income,
        email = customer.email,
        cpf = customer.cpf,
        street = customer.address.street,
        zipCode=customer.address.zip,
        id=customer.id
    )
}