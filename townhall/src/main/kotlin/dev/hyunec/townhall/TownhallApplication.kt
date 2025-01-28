package dev.hyunec.townhall

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TownhallApplication

fun main(args: Array<String>) {
    runApplication<TownhallApplication>(*args)
}
