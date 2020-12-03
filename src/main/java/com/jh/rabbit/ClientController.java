package com.jh.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ClientController {

    private final DirectExchange directExchange;
    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/getSettingsSync")
    public GenericEvent getNotificationSettingsSync(
            @RequestParam String routingKey,
            @RequestParam UUID userId) {

        log.info("getSettingsSync() - request userId: {}, routingKey: {} ", userId, routingKey);
        GenericEvent response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                routingKey,
                userId,
                new ParameterizedTypeReference<>(){
                });
        log.info("getSettingsSync() - response: {}", response);
        return response;
    }

}
