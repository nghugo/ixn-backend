package com.hng.ixn.s3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    private static final Region REGION = Region.EU_WEST_2;
    private static final String BUCKET_NAME = System.getenv("IXN_AWS_BUCKET_NAME");

    @Bean
    public S3Client s3Client() {
        String accessKey = System.getenv("IXN_AWS_USER_ACCESS_KEY");
        String secretKey = System.getenv("IXN_AWS_USER_SECRET_KEY");

        if (accessKey == null || secretKey == null) {
            throw new IllegalArgumentException("AWS credentials not set in environment variables");
        }

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    @Bean
    public String bucketName() {
        return BUCKET_NAME;
    }

}
