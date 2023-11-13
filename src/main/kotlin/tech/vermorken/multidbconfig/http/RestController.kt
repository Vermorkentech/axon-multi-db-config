package tech.vermorken.multidbconfig.http

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.vermorken.multidbconfig.api.CreateBoringAggregate
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("boring")
class RestController(
    val commandGateway: CommandGateway
) {

    @PostMapping("/{id}")
    fun createBoringAggregate(@PathVariable("id") id: String) : CompletableFuture<Any> {
        return commandGateway.send(CreateBoringAggregate(id))
    }
}