package DavidPractise.jms_enum;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

public class JMSQueueUtil {
	public static final String jmsuser = "admin";
	public static final String jmspassword = "admin";
	public static final String jsmurl = "tcp://localhost:61616";
	public static final boolean transacted = false;

	//消息队列连接池
	private static PooledConnectionFactory pooledConnectionFactory;
	
	/**
	 * 初始化JMS连接池
	 */
	static {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(jmsuser, jmspassword, jsmurl);
		pooledConnectionFactory = new PooledConnectionFactory(factory);
		//设置最大连接数为5
		pooledConnectionFactory.setMaxConnections(5);
		//设置每个连接中最大session数目为20
		pooledConnectionFactory.setMaximumActiveSessionPerConnection(20);
		//没有设置connection过期时间，默认过期时间为30s
	}
	/**
	 * 调用工厂2端队列
	 * 
	 * @param queue
	 *            队列名
	 * @param messageBody
	 *            队列参数
	 */
	public static void sendMessage(JMSQueue queue, String messageBody) {
		Connection connection = null;
		Session session = null;
		try {
			//从连接池中获取连接
			connection = pooledConnectionFactory.createConnection();
			connection.start();
			session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(queue.getQueueName());
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			Message message = session.createTextMessage(messageBody);
			producer.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//需要将会话关闭，如果不关闭，在使用连接池时，发送两次时只能成功发送一次，造成堵塞
				if (session != null) {
					session.close();
				}
				//使用连接池，不需要关闭connection，连接池会自动进行管理
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}	
	}
}
