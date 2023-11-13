package tech.vermorken.multidbconfig

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MultiDbConfigApplication

fun main(args: Array<String>) {
    runApplication<MultiDbConfigApplication>(*args)
}
