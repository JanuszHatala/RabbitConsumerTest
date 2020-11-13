package com.jh.rabbit;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Receiver {

    public void receiveMessage(String message) {
        log.info("\nReceived message in Receiver: {}", message);
    }
}
