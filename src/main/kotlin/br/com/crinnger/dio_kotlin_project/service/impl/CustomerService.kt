package br.com.crinnger.dio_kotlin_project.service.impl

import br.com.crinnger.dio_kotlin_project.config.logger
import br.com.crinnger.dio_kotlin_project.exception.BusinessException
import br.com.crinnger.dio_kotlin_project.model.Customer
import br.com.crinnger.dio_kotlin_project.repository.CustomerRepository
import br.com.crinnger.dio_kotlin_project.service.ICustomerService
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service

@Service
@Slf4j
class CustomerService(
        private val customerRepository: CustomerRepository
): ICustomerService {
    private val log = logger()


    override fun save(customer: Customer): Customer =
            this.customerRepository.save(customer)

    override fun update(customerUpdated: Customer): Customer {
        return this.customerRepository.save(customerUpdated)
    }

    override fun findById(customerId: Long): Customer =
            this.customerRepository.findById(customerId).orElseThrow {
                throw BusinessException("Id $customerId not Found")
            }

    override fun delete(customerId: Long) {
        log.info("call delete customer $customerId")
        val finded=this.findById(customerId)
        log.info("customer found: $finded")
        this.customerRepository.delete(finded)
        log.info("customer $customerId deleted")
    }
}