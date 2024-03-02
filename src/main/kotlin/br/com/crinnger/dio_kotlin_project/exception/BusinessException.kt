package br.com.crinnger.dio_kotlin_project.exception

data class BusinessException(override val message:String?):RuntimeException(message) {
}