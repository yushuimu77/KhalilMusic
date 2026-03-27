package cn.edu.seig.vibemusic.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqCongif {
    public static final String ADD_EXCHANGE = "vibe_music_exchange";
    public static final String ADD_QUEUE = "vibe_music_queue";
    public static final String ADD_KEY = "add_music";

    @Bean
    public Exchange exchange() {
        return new DirectExchange(ADD_EXCHANGE, true, false);
    }
    @Bean
    public Queue songAddQueue() {
        return QueueBuilder.durable(ADD_QUEUE).build();
    }
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(songAddQueue()).to(exchange()).with(ADD_KEY).noargs();
    }
}