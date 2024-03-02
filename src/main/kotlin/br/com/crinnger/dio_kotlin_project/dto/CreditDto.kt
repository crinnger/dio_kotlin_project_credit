package br.com.crinnger.dio_kotlin_project.dto

import br.com.crinnger.dio_kotlin_project.model.Credit
import br.com.crinnger.dio_kotlin_project.model.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
        @field:NotNull(message = "creditValue is required")
        @field:Positive
        val creditValue: BigDecimal,

        @field:NotNull(message = "dayFirstOfInstallment is required")
        @field:Future
        val dayFirstOfInstallment: LocalDate,

        @field:NotNull(message = "numberOfInstallment is required")
        @field:Positive
        val numberOfInstallment: Int,

        @field:NotNull(message = "customerId is required")
        val customerId:Long
) {
    fun toEntity(): Credit =
            Credit(
                    creditValue = this.creditValue,
                    dayFirstInstallment = this.dayFirstOfInstallment,
                    numberInstallments = this.numberOfInstallment,
                    customer = Customer(id = this.customerId)
            )
}