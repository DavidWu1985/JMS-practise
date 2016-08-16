package DavidPractise.jms_enum;

import org.junit.Test;

public class JMSQueueUtilTest {

	@Test
	public void sendMessage(){
		JMSQueueUtil.sendMessage(JMSQueue.Test, "This is test");
	}
}
