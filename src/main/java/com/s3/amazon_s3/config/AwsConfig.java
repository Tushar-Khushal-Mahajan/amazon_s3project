package com.s3.amazon_s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

	@Value("${aws.accessKeyId}")
	private String keyId;
	@Value("${aws.secretAccessKey}")
	private String keySecret;
	@Value("${aws.region}")
	private String region;

	@Bean
	public S3Client client() {

		return S3Client.builder().region(Region.of(region))
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(keyId, keySecret)))
				.build();
	}
}
