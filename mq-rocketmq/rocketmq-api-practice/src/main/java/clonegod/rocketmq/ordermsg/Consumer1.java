package clonegod.rocketmq.ordermsg;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

public class Consumer1 {
	
	// 集群消费时，消费端的GroupName要相同
	public static final String CONSUMER_GROUP_NAME = "CONSUMER_ordering";
	public static final String TOPIC_NAME = "Topic-ordering";
	
	public void start() {
        try {
        	DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_NAME);
        	consumer.setNamesrvAddr("192.168.1.201:9876;192.168.1.202:9876");
        	
        	consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        	// 订阅Topic，可以指定Tag对消息进行二次过滤
			consumer.subscribe(TOPIC_NAME, "*");
			
			// 注册消息处理器
			consumer.registerMessageListener(new Listener());
			
			// 启动consumer实例
			consumer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	
	/**
	 * MessageListenerOrderly - 消费端并发且有序消费
	 * 有序消息的底层实现:
	 * 	1、producer的保证：单线程顺序发送，将相同orderId的消息发送到broker的同一个队列上；
	 * 	2、broker端的保证：保证将消息按顺序发送给consumer；
	 *  3、consumer端的保证：相同orderId的消息，被绑定到一个专门的线程来进行处理；
	 *  在consumer端通过线程池提供多线程并发处理不同orderId的消息，不会发生消息乱序处理。
	 */
	class Listener implements MessageListenerOrderly {

		 @Override
         public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
             context.setAutoCommit(true);
             
             try {
            	 for(MessageExt msg : msgs) {
            		 System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msg 
            				 + ", content=" + new String(msg.getBody()));
            		 Thread.sleep(10);
            	 }
			} catch (Exception e) {
				e.printStackTrace();
				return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
			}
             
             return ConsumeOrderlyStatus.SUCCESS;
         }
	}

    public static void main(String[] args) throws InterruptedException, MQClientException {
    	new Consumer1().start();
        System.out.println("Consumer1 Started.");
    }
}
