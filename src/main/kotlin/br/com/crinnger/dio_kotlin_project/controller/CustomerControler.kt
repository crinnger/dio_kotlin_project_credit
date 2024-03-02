package br.com.crinnger.dio_kotlin_project.controller

import br.com.crinnger.dio_kotlin_project.config.logger
import br.com.crinnger.dio_kotlin_project.dto.CustomerDto
import br.com.crinnger.dio_kotlin_project.dto.CustomerUpdateDto
import br.com.crinnger.dio_kotlin_project.dto.CustomerView
import br.com.crinnger.dio_kotlin_project.model.Customer
import br.com.crinnger.dio_kotlin_project.service.ICustomerService
import br.com.crinnger.dio_kotlin_project.service.impl.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customer")
class CustomerControler(
     private val   customerService: ICustomerService
) {

    private val log = logger()

    @PostMapping
    fun save(@RequestBody @Valid customer: CustomerDto): ResponseEntity<CustomerView>{
        val saved= this.customerService.save(customer.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerView(saved))
    }

    @GetMapping("/{id}")
    fun findByID(@PathVariable(name = "id") customerId:Long): ResponseEntity<CustomerView> {
        log.info("call find by id customer $customerId")
        val customer= this.customerService.findById(customerId)
        return ResponseEntity.ok(CustomerView(customer))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long):ResponseEntity<Void>{
        log.info("call delete customer $id")
        this.customerService.delete(id)
        return ResponseEntity.noContent().build()
    }


    @PatchMapping("/{id}")
    fun updateCustomer(@PathVariable id:Long,
                       @RequestBody @Valid  customerUpdate: CustomerUpdateDto): ResponseEntity<CustomerView> {
        val customer= this.customerService.findById(id)
        customer.apply {
            firstName=customerUpdate.firstName
            lastName=customerUpdate.lastName
            income=customerUpdate.income
            address.street=customerUpdate.street
            address.zip=customerUpdate.zipCode
        }
        return ResponseEntity.ok(CustomerView(this.customerService.update(customer)))
    }
}