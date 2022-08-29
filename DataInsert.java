package quicksort.analytics;

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

public class data_insert implements RequestHandler<temp_reading, Void> {

public Void handleRequest(temp_reading reading, Context arg1) {

AmazonDynamoDB client =
AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_2)

.withCredentials(new
DefaultAWSCredentialsProviderChain()).build();

DynamoDB dynamoDB = new DynamoDB(client);
Table table = dynamoDB.getTable(&quot;sensor_data1&quot;);

try {
Item item = new Item().withPrimaryKey(&quot;time&quot;,

reading.getTime()).withInt(&quot;reading&quot;, reading.getReading())

.withString(&quot;unit&quot;, reading.getUnit());

System.out.println(&quot;PutItem succeeded: &quot; + reading.getTime());
}

catch (Exception e) {
System.err.println(e.getMessage());

}

return null;

}

}
