package DavidPractise.jms_enum;

public enum JMSQueue {

	Test("JSMQueue.test");
	
	
	/**
	 * 消息队列名称
	 */
	private String queueName;
	
	/**
	 * 私有构造方法
	 * @param queueName
	 */
	private JMSQueue(String queueName){
		this.queueName = queueName;
	}

	public String getQueueName() {
		return queueName;
	}
	
}
