package edu.nwmissouri.isl.santhosh.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Paging;
import twitter4j.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.*;

/**
 * Custom Producer using Kafka for messaging. 
 * Reads properties from the run.properties file in 
 * src/main/resources.
 */
public class CustomProducer {
  private static Scanner in;

  public static void main(String[] argv) throws Exception {
    if (argv.length != 1) {
      System.err.println("Please specify 1 parameter (the name of the topic)");
      System.exit(-1);
    }
    String topicName = argv[0];
    in = new Scanner(System.in);
    System.out.println("Thank you for providing the topic " + topicName + "\n");
    System.out.println("Enter message (type exit to quit).\n");

    // Configure the Producer
    Properties configProperties = new Properties();
    configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.ByteArraySerializer");
    configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    System.out.println("The configuration properties are: " + configProperties.toString());
    System.out.println("\nWill use this configuration to create a producer.\n");

    org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(configProperties);

    // Make our own messages - create your custom logic here

    for (int i = 1; i <= 10; i++) {
      String message = createSentence();
      ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName, message);
      producer.send(rec);
    }

    // still allow input from keyboard

    String line = in.nextLine();
    while (!line.equals("exit")) {
      ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName, line);
      producer.send(rec);
      line = in.nextLine();
    }

    in.close();
    producer.close();

  }

  private static String createSentence() {
    String[] subjects = { "Big Data", "Kafka", "Lambda architecture", "Kappa architecture", "Spark" };
    String[] verbs = { "is", "was", "will be", "isn't", "will never be" };
    String[] objs = { "hard", "easy", "fun", "challenging", "awesome" };

    Random r = new Random();

    int count = 3;
    int minIndex = 0;
    int maxIndex = 4;

    int[] randoms = r.ints(count, minIndex, maxIndex).toArray();

    return subjects[randoms[0]] + " " + verbs[randoms[1]] + " " + objs[randoms[2]] + ".";
  }
}
