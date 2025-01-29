package dev.hyunec.townhall.support.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class IndexController {
    @GetMapping("/_health")
    fun health(): String {
        return Instant.now().toString()
    }

    @PostMapping("/echo")
    fun echo(@RequestBody request: Echo.Request): Echo.Response {
        return Echo.Response(request.message)
    }

    class Echo {
        data class Request(
            val message: String
        )

        data class Response(
            val message: String,
            val timestamp: Instant = Instant.now()
        )
    }
}
