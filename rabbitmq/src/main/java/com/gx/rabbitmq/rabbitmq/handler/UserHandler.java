package com.gx.rabbitmq.rabbitmq.handler;

import com.gx.rabbitmq.rabbitmq.config.RabbitConfig;
import com.gx.rabbitmq.rabbitmq.entity.User;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

/**
 * USER_QUEUE 消费者
 *
 * @author: gaoxu
 * @date: 2018/9/5
 */
public class UserHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);

    /**
     * <p>TODO 该方案是 spring-boot-data-amqp 默认的方式,不太推荐。具体推荐使用  listenerManualAck()</p>
     * 默认情况下,如果没有配置手动ACK, 那么Spring Data AMQP 会在消息消费完毕后自动帮我们去ACK
     * 存在问题：如果报错了,消息不会丢失,但是会无限循环消费,一直报错,如果开启了错误日志很容易就吧磁盘空间耗完
     * 解决方案：手动ACK,或者try-catch 然后在 catch 里面讲错误的消息转移到其它的系列中去
     * spring.rabbitmq.listener.simple.acknowledge-mode=manual
     * <p>
     *
     * @param user 监听的内容
     */
    @RabbitListener(queues = {RabbitConfig.DEFAULT_USER_QUEUE})
    public void listenerAutoAck(User user, Message message, Channel channel) {
        // TODO 如果手动ACK,消息会被监听消费,但是消息在队列中依旧存在,如果 未配置 acknowledge-mode 默认是会在消费完毕后自动ACK掉
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("[listenerAutoAck 监听的消息] - [{}]", user.toString());
            // TODO 通知 MQ 消息已被成功消费,可以ACK了
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                // TODO 处理失败,重新压入MQ
                channel.basicRecover();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RabbitListener(queues = {RabbitConfig.MANUAL_USER_QUEUE})
    public void listenerManualAck(User user, Message message, Channel channel) {
        log.info("[listenerManualAck 监听的消息] - [{}]", user.toString());
        try {
            // TODO 通知 MQ 消息已被成功消费,可以ACK了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            // TODO 如果报错了,那么我们可以进行容错处理,比如转移当前消息进入其它队列
        }
    }
}
