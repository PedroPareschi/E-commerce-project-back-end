package br.dev.pedropareschi.cursomc.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {

    @Value("${google.access_key_id}")
    private String googleId;
    @Value("${google.secret_access_key}")
    private String googleKey;
    @Value("${gcp.region}")
    private String region;

    @Bean
    public AmazonS3 googleClient(){
        BasicAWSCredentials googleCred = new BasicAWSCredentials(googleId, googleKey);
        AmazonS3 googleClient =  AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                "https://storage.googleapis.com", "auto"))
                .withCredentials(new AWSStaticCredentialsProvider(googleCred))
                .build();
        return googleClient;
    }
}
