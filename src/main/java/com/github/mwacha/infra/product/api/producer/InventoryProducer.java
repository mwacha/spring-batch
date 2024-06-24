//package com.github.mwacha.infra.product.api.producer;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//
////@Service
//public class InventoryProducer {
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange("inventory-exchange");
//    }
//
//    @Bean
//    public Binding producerBinding(Queue producerQueue, TopicExchange exchange) {
//        return BindingBuilder.bind(producerQueue).to(exchange).with("inventory.producer.*");
//    }
//
//    @Bean
//    public Binding consumerBinding(Queue consumerQueue, TopicExchange exchange) {
//        return BindingBuilder.bind(consumerQueue).to(exchange).with("inventory.consumer.*");
//    }
//}
