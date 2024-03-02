package br.com.crinnger.dio_kotlin_project.controller

import br.com.crinnger.dio_kotlin_project.dto.*
import br.com.crinnger.dio_kotlin_project.model.Credit
import br.com.crinnger.dio_kotlin_project.model.Customer
import br.com.crinnger.dio_kotlin_project.service.ICustomerService
import br.com.crinnger.dio_kotlin_project.service.impl.CreditService
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
import java.util.*

@RestController
@RequestMapping("/api/credit")
class CreditControler(
     private val   creditService: CreditService
) {

    @PostMapping
    fun save(@RequestBody @Valid credit: CreditDto):ResponseEntity<CreditView>{
        val saved= this.creditService.save(credit.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(CreditView(saved))
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId:Long): ResponseEntity<List<CreditViewList>> {
        return ResponseEntity.ok(this.creditService.findAllByCustomer(customerId)
                .map { credit: Credit ->  CreditViewList(credit) })
    }


    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): ResponseEntity<CreditView> =
            ResponseEntity.ok(CreditView(this.creditService.findByCreditCode(creditCode,customerId)))

}