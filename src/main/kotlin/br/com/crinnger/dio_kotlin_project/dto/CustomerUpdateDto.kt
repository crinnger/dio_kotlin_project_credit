package br.com.crinnger.dio_kotlin_project.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "firstName is required")
    val firstName: String,
    @field:NotEmpty(message = "lastName is required")
    val lastName: String,
    @field:NotNull(message = "income is required")
    val income: BigDecimal,
    @field:NotEmpty(message = "zipCode is required")
    val zipCode: String,
    @field:NotEmpty(message = "street is required")
    val street:String
)