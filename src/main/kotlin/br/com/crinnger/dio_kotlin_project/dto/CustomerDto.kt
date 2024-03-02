package br.com.crinnger.dio_kotlin_project.dto

import br.com.crinnger.dio_kotlin_project.model.Address
import br.com.crinnger.dio_kotlin_project.model.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "firstName is required")
    val firstName:String,
    @field:NotEmpty(message = "lastName is required")
    val lastName:String,
    @field:NotEmpty(message = "cpf is required")
    @field:CPF(message = "Cpf Invalid")
    val cpf:String,
    @field:NotEmpty(message = "email is required")
    @field:Email
    val email:String,
    @field:NotEmpty(message = "passwork is required")
    val password:String,
    @field:NotEmpty(message = "zipCode is required")
    val zipCode:String,
    @field:NotEmpty(message = "street is required")
    val street: String,
    @field:NotNull(message = "income is required")
    val income: BigDecimal
){
    fun toEntity():Customer=
            Customer(
                    firstName = this.firstName,
                    lastName = this.lastName,
                    cpf = this.cpf,
                    income = this.income,
                    email = this.email,
                    password = this.password,
                    address= Address(this.zipCode,this.street)
            )
}