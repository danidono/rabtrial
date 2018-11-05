package sk.didi.rabtrial.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sk.didi.rabtrial.Sender;

@Slf4j
@RestController
public class SenderController {
    private Sender sender;

    public SenderController(Sender sender) {
        this.sender = sender;
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendMessage() throws InterruptedException {
        log.info("started to send message...");
        String msg = sender.sendMsg();
        return "This is message : " + msg;
    }

}
