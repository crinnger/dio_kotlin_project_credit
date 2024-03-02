package br.com.crinnger.dio_kotlin_project.exception

import java.sql.Timestamp
import java.time.LocalDateTime

data class ExceptionDetail(
        var title:String,
        var timestamp: LocalDateTime,
        var status: Int,
        var exception: String,
        var details: MutableMap<String,String?>
)
