package lambda.data;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.IoTButtonEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class IotHandler implements RequestHandler<Temperature_Reading, Void> {

public Void handleRequest(Temperature_Reading reading, Context arg1) {
//System.out.println("message: "+arg0.getReading());
arg1.getLogger().log("message: "+reading.getReading());
if(reading.getReading() > 70)
{
arg1.getLogger().log("ALERT: TEMPERATURE ABOVE 70

C !!");

boolean alert= true;

AmazonDynamoDB client =
AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_2).with
Credentials(new DefaultAWSCredentialsProviderChain()).build();
arg1.getLogger().log("ALERT2");

DynamoDB dynamoDB = new DynamoDB(client);
Table table = dynamoDB.getTable("alert");
arg1.getLogger().log("ALERT3");

try {
Item item= new Item().withPrimaryKey ("time", reading.getTime())
.withInt("reading",reading.getReading())
.withString("unit", reading.getUnit())
.withBoolean("alert",alert);
table.putItem(item);
System.out.println("PutItem succeeded: "+reading.getTime());
}

catch (Exception e) {
System.err.println(e.getMessage());
}

AmazonSNS snsClient =
AmazonSNSClient.builder().withRegion(Regions.US_EAST_2).withCredentials(new
DefaultAWSCredentialsProviderChain()).build();
// Publish a message to an Amazon SNS topic.
final String msg = "Temperature above 70°C";
final PublishRequest publishRequest = new
PublishRequest("arn:aws:sns:us-east-2:186865583359:test_sms", msg);
final PublishResult publishResponse =
snsClient.publish(publishRequest);

// Print the MessageId of the message.
System.out.println("MessageId: " + publishResponse.getMessageId());

/* AmazonSNS snsClient1 =
AmazonSNSClient.builder().withRegion(Regions.US_EAST_1).withCredentials(new
DefaultAWSCredentialsProviderChain()).build();
// Publish a message to an Amazon SNS topic.
final String msg1 = "Temperature above 70°C";
final PublishRequest publishRequest1 = new
PublishRequest("arn:aws:sns:us-east-1:186865583359:SMStopic", msg1);
final PublishResult publishResponse1 =
snsClient1.publish(publishRequest1);

// Print the MessageId of the message.
System.out.println("SMS_MessageId: " +
publishResponse.getMessageId());*/

/*//publishing message to aws sns topic in different region

AmazonSNS snsClient1 =
AmazonSNSClient.builder().withRegion(Regions.US_EAST_1).withCredentials(new
DefaultAWSCredentialsProviderChain()).build();
String message = "Temperature is above 70 C";
String phoneNumber = "+918861045636";
Map<String, MessageAttributeValue> smsAttributes =
new HashMap<String, MessageAttributeValue>();
//<set SMS attributes>
sendSMSMessage(snsClient1, message, phoneNumber,
smsAttributes);
*/
}
return null;
}

/* public static void sendSMSMessage(AmazonSNS snsClient, String
message,
String phoneNumber, Map<String, MessageAttributeValue>
smsAttributes) {
PublishResult result = snsClient.publish(new PublishRequest()
.withMessage(message)
.withPhoneNumber(phoneNumber)
.withMessageAttributes(smsAttributes));
System.out.println(result); // Prints the message ID.
}*/
}
