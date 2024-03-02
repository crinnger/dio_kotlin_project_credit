package br.com.crinnger.dio_kotlin_project.exception

import br.com.crinnger.dio_kotlin_project.config.logger
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.validation.method.MethodValidationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime


@RestControllerAdvice
class RestExceptionHandler {

    private val log = logger()

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ExceptionDetail>{
        val erros:MutableMap<String,String?> =HashMap()
        e.bindingResult.allErrors.stream()
                .forEach{
                    erro: ObjectError->
                    val fieldName: String=(erro as FieldError).field
                    val messageError:String?= erro.defaultMessage
                    erros[fieldName]=messageError
                }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionDetail(title = "Bad Request Method Argument Invalid",
                        timestamp = LocalDateTime.now(),
                        status = HttpStatus.BAD_REQUEST.value(),
                        exception = e.objectName.toString(),
                        details = erros
                        ))
    }

    @ExceptionHandler(DataAccessException::class)
    fun handlerDataAccessException(e: DataAccessException): ResponseEntity<ExceptionDetail>{
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionDetail(title = "Conflic Data Acess",
                        timestamp = LocalDateTime.now(),
                        status = HttpStatus.CONFLICT.value(),
                        exception = e.javaClass.toString(),
                        details = mutableMapOf(e.cause.toString() to e.message)
                ))
    }

    @ExceptionHandler(BusinessException::class)
    fun handlerBusinessException(e: BusinessException): ResponseEntity<ExceptionDetail>{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionDetail(title = "Bad Request",
                        timestamp = LocalDateTime.now(),
                        status = HttpStatus.BAD_REQUEST.value(),
                        exception = e.javaClass.toString(),
                        details = mutableMapOf(e.cause.toString() to e.message)
                ))
    }

    @ExceptionHandler(Exception::class)
    fun handlerException(e: Exception): ResponseEntity<ExceptionDetail>{

        log.info("Application Error: ${e.cause} - ${e.message} ")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionDetail(title = "Error API",
                        timestamp = LocalDateTime.now(),
                        status = HttpStatus.BAD_REQUEST.value(),
                        exception = e.javaClass.toString(),
                        details = mutableMapOf(e.cause.toString() to e.message)
                ))
    }
}