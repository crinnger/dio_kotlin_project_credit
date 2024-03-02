package br.com.crinnger.dio_kotlin_project.dto

import br.com.crinnger.dio_kotlin_project.model.Credit
import java.math.BigDecimal
import java.util.*

data class CreditViewList(
        val creditCode: UUID,
        val creditValue: BigDecimal,
        val numberOfInstallment:Int
){
    constructor(credit: Credit): this(
        creditCode=credit.creditCode,
        creditValue=credit.creditValue,
        numberOfInstallment=credit.numberInstallments
    )
}
