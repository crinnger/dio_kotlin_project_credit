package br.com.crinnger.dio_kotlin_project.service.impl

import br.com.crinnger.dio_kotlin_project.exception.BusinessException
import br.com.crinnger.dio_kotlin_project.model.Address
import br.com.crinnger.dio_kotlin_project.model.Credit
import br.com.crinnger.dio_kotlin_project.model.Customer
import br.com.crinnger.dio_kotlin_project.repository.CreditRepository
import br.com.crinnger.dio_kotlin_project.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.servlet.tags.form.OptionTag
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK
    lateinit var customerService: CustomerService

    @MockK
    lateinit var creditRepository: CreditRepository

    @InjectMockKs
    lateinit var creditService: CreditService

    @Test
    fun `should create credit`() {
        val fakeCustomer=createCustomer()
        val fakeCredit=createCredit(fakeCustomer,UUID.randomUUID())

        every { customerService.findById(any()) } returns fakeCustomer
        every { creditRepository.save(any()) } returns fakeCredit

        val actual=creditService.save(fakeCredit)
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isEqualTo(fakeCredit)
        verify (exactly = 1){ creditRepository.save(any()) }
    }

    @Test
    fun `should not create credit because dont find customer`() {
        val fakeCustomer=createCustomer()
        val fakeCredit=createCredit(fakeCustomer,UUID.randomUUID())

        every { customerService.findById(any()) } throws BusinessException("Id ${fakeCustomer.id} not Found")
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.save(fakeCredit)}
            .withMessage("Id 1 not Found")
    }

    @Test
    fun `should find all credit by customer`() {
        val fakeCustomer=createCustomer()
        val fakeCredit=createCredit(fakeCustomer,UUID.randomUUID())

        every { customerService.findById(any()) } returns fakeCustomer
        every { creditRepository.findAllByCustomerId(any()) } returns mutableListOf( fakeCredit)

        val actual=creditService.findAllByCustomer(fakeCustomer.id!!)
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).contains(fakeCredit)
        verify (exactly = 1){ creditRepository.findAllByCustomerId(any()) }
    }


    @Test
    fun `should find credit by credit code`() {
        val fakeCustomer=createCustomer()
        val fakeCredit=createCredit(fakeCustomer,UUID.randomUUID())

        every { creditRepository.findByCreditCode(any()) } returns fakeCredit

        val actual=creditService.findByCreditCode(fakeCredit.creditCode,fakeCustomer.id!!)
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isEqualTo(fakeCredit)
        verify (exactly = 1){ creditRepository.findByCreditCode(any()) }
    }

    @Test
    fun `should find credit by credit code with Customer not equal`() {
        val fakeCustomer=createCustomer()
        val fakeCredit=createCredit(fakeCustomer,UUID.randomUUID())

        every { creditRepository.findByCreditCode(any()) } returns fakeCredit

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeCredit.creditCode,5L)}
            .withMessage("Credit Code Invalid ${fakeCredit.creditCode}")

        verify (exactly = 1){ creditRepository.findByCreditCode(any()) }
    }

    @Test
    fun `should not find credit by credit code`() {
        val fakeCustomer=createCustomer()
        val fakeCredit=createCredit(fakeCustomer,UUID.randomUUID())
        every { creditRepository.findByCreditCode(any()) } returns null

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeCredit.creditCode,1L)}
            .withMessage("Credit Code not Found ${fakeCredit.creditCode}")

        verify (exactly = 1){ creditRepository.findByCreditCode(any()) }
    }

    fun createCredit(customer:Customer,creditCode: UUID): Credit =
        Credit(
            creditValue = BigDecimal.TEN,
            dayFirstInstallment = LocalDate.now(),
            numberInstallments = 1,
            creditCode = creditCode,
            customer = customer,
            id = 1L
        )
    fun createCustomer() : Customer =
        Customer(firstName = "teste",
            lastName = "tst",
            cpf="111111111",
            email = "teste@gmail.com",
            password = "eerr",
            income = BigDecimal.TEN,
            id = 1L,
            address = Address(zip = "54545", street = "teste")
        )
}