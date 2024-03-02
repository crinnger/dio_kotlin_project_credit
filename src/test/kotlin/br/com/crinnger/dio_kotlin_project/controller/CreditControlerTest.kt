package br.com.crinnger.dio_kotlin_project.controller

import br.com.crinnger.dio_kotlin_project.dto.CreditDto
import br.com.crinnger.dio_kotlin_project.dto.CustomerDto
import br.com.crinnger.dio_kotlin_project.model.Address
import br.com.crinnger.dio_kotlin_project.model.Credit
import br.com.crinnger.dio_kotlin_project.model.Customer
import br.com.crinnger.dio_kotlin_project.repository.CreditRepository
import br.com.crinnger.dio_kotlin_project.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class CreditControlerTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var creditRepository: CreditRepository
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object{
        const val URL="/api/credit"
    }

    @BeforeEach
    fun setup(){
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @AfterEach
    fun tearDown(){
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun `should create credit`() {
        val customer=customerRepository.save(createCustomer())
        val credit=createCreditDto(customer.id!!)
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(credit)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customer.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallment").value(credit.numberOfInstallment))

    }

    @Test
    fun `should not create credit because dont exist customer`() {
        val credit=createCreditDto(1L)
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credit)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should not create credit because creditvalue is 0`() {
        val customer=customerRepository.save(createCustomer())
        val credit=createCreditDto(customer.id!!, creditValue = BigDecimal.ZERO)
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credit)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.creditValue").exists())
    }

    @Test
    fun `should not create credit because numberofinstallment is 0`() {
        val customer=customerRepository.save(createCustomer())
        val credit=createCreditDto(customer.id!!, numberInstallment = 0)
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credit)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.numberOfInstallment").exists())
    }

    @Test
    fun `should not create credit because dayFirstOfInstallment is yesterday`() {
        val customer=customerRepository.save(createCustomer())
        val credit=createCreditDto(customer.id!!, dayFirstOfInstallment = LocalDate.now().minusDays(1))
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credit)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.dayFirstOfInstallment").exists())
    }


    @Test
    fun `should find credit by customer`() {
        val customer = customerRepository.save(createCustomer())
        val credit = creditRepository.save(createCreditDto(customer.id!!).toEntity())
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", customer.id.toString())
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").value(credit.creditCode.toString()))

    }

    @Test
    fun `should find 2 credit by customer`() {
        val customer = customerRepository.save(createCustomer())
        val credit1 = creditRepository.save(createCreditDto(customer.id!!).toEntity())
        val credit2 = creditRepository.save(createCreditDto(customer.id!!).toEntity())
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", customer.id.toString())
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Int>(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[*].creditCode", Matchers.hasItems(credit1.creditCode.toString(), credit2.creditCode.toString())))

    }

    @Test
    fun `should not find credit because customer dont exits`() {
        val customer = customerRepository.save(createCustomer())
        val credit = creditRepository.save(createCreditDto(customer.id!!).toEntity())
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "2222")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Int>(0)))

    }

    @Test
    fun `should not find credit because credit dont exits`() {
        val customer = customerRepository.save(createCustomer())

        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", customer.id.toString())
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Int>(0)))

    }

    @Test
    fun `should find credit by creditCode`() {
        val customer = customerRepository.save(createCustomer())
        val credit = creditRepository.save(createCreditDto(customer.id!!).toEntity())
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", customer.id.toString())
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").value(credit.creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customer.email))

    }

    @Test
    fun `should not find credit by creditCode`() {
        val customer = customerRepository.save(createCustomer())
        val credit = creditRepository.save(createCreditDto(customer.id!!).toEntity())
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${UUID.randomUUID().toString()}")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", customer.id.toString())
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)

    }

    @Test
    fun `should not find credit by creditCode because customer dont exits`() {
        val customer = customerRepository.save(createCustomer())
        val credit = creditRepository.save(createCreditDto(customer.id!!).toEntity())
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "22222222")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)

    }

    fun createCreditDto(customerId: Long,
                        creditValue: BigDecimal=BigDecimal.TEN,
                        numberInstallment: Int=1,
                        dayFirstOfInstallment: LocalDate=LocalDate.now().plusMonths(1)): CreditDto =
        CreditDto(
            creditValue = creditValue,
            numberOfInstallment = numberInstallment,
            dayFirstOfInstallment = dayFirstOfInstallment,
            customerId = customerId
        )
    fun createCustomer(email:String="teste@teste.com",
                          cpf:String="09285145019",
                          firstName:String="teste",
                          lastName:String="teste",
                          income: BigDecimal=BigDecimal.TEN,
                          zipCode:String="teste",
                          street:String="Teste"): Customer =
        Customer(
            address = Address(zip = zipCode,street = street),
            income = income,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = "teste",
            cpf = cpf
        )
}