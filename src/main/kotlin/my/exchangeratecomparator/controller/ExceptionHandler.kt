package my.exchangeratecomparator.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import my.exchangeratecomparator.exception.PairNotFoundException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(PairNotFoundException::class)
    fun handlePairNotFoundException(ex: PairNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }
}