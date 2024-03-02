package br.com.crinnger.dio_kotlin_project.controller

import br.com.crinnger.dio_kotlin_project.dto.CustomerDto
import br.com.crinnger.dio_kotlin_project.dto.CustomerUpdateDto
import br.com.crinnger.dio_kotlin_project.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

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

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerControlerTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper:ObjectMapper

    companion object{
        const val URL="/api/customer"
    }

    @BeforeEach
    fun setup()=customerRepository.deleteAll()

    @AfterEach
    fun tearDown()=customerRepository.deleteAll()

    @Test
    fun `should create customer and return` () {
        val customerDto=createCustomerDTO("teste@teste.com","09285145019")
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerDto.firstName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerDto.lastName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(customerDto.income))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerDto.email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(customerDto.cpf))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(customerDto.street))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value(customerDto.zipCode))

    }

    @Test
    fun `should not save customer with cpf invalid return 400` () {
        val customerDto=createCustomerDTO("teste@teste.com","1111")
        `validTestSave400`(customerDto,"cpf")
    }

    @Test
    fun `should not save customer with email invalid return 400` () {
        val customerDto=createCustomerDTO("testeteste.com","1111")
        `validTestSave400`(customerDto,"email")
    }

    @Test
    fun `should not save customer with cpf empty return 400` () {
        val customerDto=createCustomerDTO("teste@teste.com","")
        `validTestSave400`(customerDto,"cpf")
    }


    @Test
    fun `should not save customer with email empty return 400` () {
        val customerDto=createCustomerDTO("","09285145019")
        `validTestSave400`(customerDto,"email")
    }

    @Test
    fun `should not save customer with firstName Empty return 400` () {
        val customerDto=createCustomerDTO("teste@teste.com","09285145019", firstName = "")

        `validTestSave400`(customerDto,"firstName")
    }

    @Test
    fun `should not save customer with lastName Empty return 400` () {
        val customerDto=createCustomerDTO("teste@teste.com","09285145019", lastName = "")

        `validTestSave400`(customerDto,"lastName")
    }

    @Test
    fun `should not save customer with zipcode Empty return 400` () {
        val customerDto=createCustomerDTO("teste@teste.com","09285145019", zipCode ="" )

        `validTestSave400`(customerDto,"zipCode")
    }

    @Test
    fun `should not save customer with street Empty return 400` () {
        val customerDto=createCustomerDTO("teste@teste.com","09285145019", street ="", )

        `validTestSave400`(customerDto,"street")
    }

    @Test
    fun `should not save customer with same cpf return 409` () {

        customerRepository.save(createCustomerDTO("teste2@teste.com","09285145019").toEntity())
        val customerDto=createCustomerDTO("teste@teste.com","09285145019")
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(MockMvcResultMatchers.status().`is`(HttpStatus.CONFLICT.value()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflic Data Acess"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.dao.DataIntegrityViolationException"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should not save customer with same email return 409` () {
        customerRepository.save(createCustomerDTO("teste@teste.com","44358249060").toEntity())
        val customerDto=createCustomerDTO("teste@teste.com","09285145019")
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(MockMvcResultMatchers.status().`is`(HttpStatus.CONFLICT.value()))
    }

    @Test
    fun `should find customer and return` () {
        val customer=customerRepository.save(createCustomerDTO("teste@teste.com","09285145019").toEntity())
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customer.firstName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customer.lastName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customer.email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(customer.cpf))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(customer.address.street))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value(customer.address.zip))

    }

    @Test
    fun `should not find customer and return 400`() {
        val customer=customerRepository.save(createCustomerDTO("teste@teste.com","09285145019").toEntity())
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/2220")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class br.com.crinnger.dio_kotlin_project.exception.BusinessException"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)


    }

    @Test
    fun `should delete customer and return BadRequest` () {
        val customer=customerRepository.save(createCustomerDTO("teste@teste.com","09285145019").toEntity())
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent)

    }

    @Test
    fun `should try delete and not find customer and return BadRequest` () {
        val customer=customerRepository.save(createCustomerDTO("teste@teste.com","09285145019").toEntity())
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/222")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class br.com.crinnger.dio_kotlin_project.exception.BusinessException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)

    }

    @Test
    fun `should update customer and return ok`() {
        val customer=customerRepository.save(createCustomerDTO("teste@teste.com","09285145019").toEntity())
        val customerDto=CustomerUpdateDto(
            income = BigDecimal.ONE,
            street = "teste2",
            zipCode = "teste2",
            lastName = "teste2",
            firstName = "teste2")
        mockMvc.perform(MockMvcRequestBuilders.patch("$URL/${customer.id}")
            .content(objectMapper.writeValueAsString(customerDto))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerDto.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerDto.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(customerDto.street))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value(customerDto.zipCode))
    }

    @Test
    fun `should not update customer with firstName Empty return 400` () {
        val customer=customerRepository.save(createCustomerDTO("teste@teste.com","09285145019").toEntity())
        val customerDto=CustomerUpdateDto(
            income = BigDecimal.ONE,
            street = "teste2",
            zipCode = "teste2",
            lastName = "teste2",
            firstName = "")

        mockMvc.perform(MockMvcRequestBuilders.patch("$URL/${customer.id}")
            .content(objectMapper.writeValueAsString(customerDto))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.firstName").exists())
    }

    @Test
    fun `should not update customer with lastName Empty return 400` () {
        val customer=customerRepository.save(createCustomerDTO("teste@teste.com","09285145019").toEntity())
        val customerDto=CustomerUpdateDto(
            income = BigDecimal.ONE,
            street = "teste2",
            zipCode = "teste2",
            lastName = "",
            firstName = "teste2")

        mockMvc.perform(MockMvcRequestBuilders.patch("$URL/${customer.id}")
            .content(objectMapper.writeValueAsString(customerDto))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.lastName").exists())
    }

    @Test
    fun `should not update customer with zipcode Empty return 400` () {
        val customer=customerRepository.save(createCustomerDTO("teste@teste.com","09285145019").toEntity())
        val customerDto=CustomerUpdateDto(
            income = BigDecimal.ONE,
            street = "teste2",
            zipCode = "",
            lastName = "teste2",
            firstName = "teste2")

        mockMvc.perform(MockMvcRequestBuilders.patch("$URL/${customer.id}")
            .content(objectMapper.writeValueAsString(customerDto))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.zipCode").exists())
    }

    @Test
    fun `should not update customer with street Empty return 400` () {
        val customer=customerRepository.save(createCustomerDTO("teste@teste.com","09285145019").toEntity())
        val customerDto=CustomerUpdateDto(
            income = BigDecimal.ONE,
            street = "",
            zipCode = "teste2",
            lastName = "teste2",
            firstName = "teste2")

        mockMvc.perform(MockMvcRequestBuilders.patch("$URL/${customer.id}")
            .content(objectMapper.writeValueAsString(customerDto))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.street").exists())
    }

    fun createCustomerDTO(email:String,
                          cpf:String,
                          firstName:String="teste",
                          lastName:String="teste",
                          income: BigDecimal=BigDecimal.TEN,
                          zipCode:String="teste",
                          street:String="Teste"): CustomerDto =
            CustomerDto(
                    zipCode = zipCode,
                    street = street,
                    income = income,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = "teste",
                    cpf = cpf
            )

    private fun `validTestSave400`(customerDto: CustomerDto, field:String="") {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.details.$field").exists())

    }
}