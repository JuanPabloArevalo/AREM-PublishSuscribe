package arem.proyecto.publishsuscribe;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class Publish {
    
//    private static final Logger LOGGER = LoggerFactory.getLogger(Publish.class);
    private String clientId;
    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    
    public void create(String clientId, String topicName) throws JMSException {
        this.clientId = clientId;
        // create a Connection Factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://54.186.68.167:61616?jms.useAsyncSend=true");
        // create a Connection
        connection = connectionFactory.createConnection();
        connection.setClientID(clientId);
        // create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // create the Topic to which messages will be sent
        Topic topic = session.createTopic(topicName);
        // create a MessageProducer for sending messages
        messageProducer = session.createProducer(topic);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    
    public void sendName(String firstName, String lastName) throws JMSException {
//        String text = firstName + " " + lastName;
        String text = "{'nombre':'Juan Pablo', 'apellido':'Arevalo Merchan', 'edad':15}";
        // create a JMS TextMessage
        TextMessage textMessage = session.createTextMessage(text);
        // send the message to the topic destination
        messageProducer.send(textMessage);
        System.out.println("Mensaje enviado: "+text);
//        LOGGER.debug(clientId + ": sent message with text='{}'", text);
    }
    
     public static void main(String[] args) throws Exception { 
         Publish publish = new Publish();
         publish.create("publisher-publishsubscribe","publishsubscribe.t");
//         
//         Suscribe suscribe = new Suscribe();
//         suscribe.create("publisher-publishsubscribe", "publishsubscribe.t");
         
         try {
            publish.sendName("Peregrin", "Took");
            publish.closeConnection();
//            String greeting1 = suscribe.getGreeting(1000);


        } catch (JMSException e) {
        }
         
     }
}
