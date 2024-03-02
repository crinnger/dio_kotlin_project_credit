package br.com.crinnger.dio_kotlin_project.dto

import br.com.crinnger.dio_kotlin_project.enums.Status
import br.com.crinnger.dio_kotlin_project.model.Credit
import java.math.BigDecimal
import java.util.*

data class CreditView(
        val creditCode: UUID,
        val creditValue:BigDecimal,
        val numberOfInstallment: Int,
        val status: Status,
        val email:String,
        val income: BigDecimal,
        val id: Long
){
    constructor(credit: Credit): this(
            creditCode=credit.creditCode,
            creditValue=credit.creditValue,
            numberOfInstallment=credit.numberInstallments,
            status=credit.status,
            email= credit.customer?.email!!,
            income=credit.customer?.income!!,
            id=credit.customer?.id!!
    )
}
