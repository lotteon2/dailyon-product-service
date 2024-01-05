package com.dailyon.productservice.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Profile("!test")
@Configuration
public class AWSSqsConfig {
    // bus-refresh 적용된 부분
    private final Environment environment;

    @Autowired
    public AWSSqsConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Primary
    @RefreshScope
    public AmazonSQSAsync amazonSQSAsync() {

        String accessKey = environment.getProperty("cloud.aws.credentials.ACCESS_KEY_ID");
        String secretKey = environment.getProperty("cloud.aws.credentials.SECRET_ACCESS_KEY");
        String sqsRegion = environment.getProperty("cloud.aws.sqs.region");

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonSQSAsyncClientBuilder.standard()
                .withRegion(sqsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }
}
