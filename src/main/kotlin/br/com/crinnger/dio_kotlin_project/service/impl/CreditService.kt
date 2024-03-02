package br.com.crinnger.dio_kotlin_project.service.impl

import br.com.crinnger.dio_kotlin_project.exception.BusinessException
import br.com.crinnger.dio_kotlin_project.model.Credit
import br.com.crinnger.dio_kotlin_project.model.Customer
import br.com.crinnger.dio_kotlin_project.repository.CreditRepository
import br.com.crinnger.dio_kotlin_project.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditService(
        private val creditRepository: CreditRepository,
        private val customerService: CustomerService
): ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer=customerService.findById(credit.customer?.id!!)
        }

       return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> =
            this.creditRepository.findAllByCustomerId(customerId)


    override fun findByCreditCode(creditCode: UUID,customerId:Long): Credit {
        val credit=this.creditRepository.findByCreditCode(creditCode)
                ?: throw BusinessException("Credit Code not Found $creditCode")
        return if(credit.customer?.id==customerId) credit
        else throw  BusinessException("Credit Code Invalid $creditCode")
    }

}