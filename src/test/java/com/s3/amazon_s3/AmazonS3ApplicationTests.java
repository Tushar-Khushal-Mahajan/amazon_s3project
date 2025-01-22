package com.s3.amazon_s3;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

@SpringBootTest
class AmazonS3ApplicationTests {

	@Test
	void contextLoads() {
		S3Client s3Client = S3Client.builder().region(Region.of("eu-west-1")).build();

		ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();

		try {
			ListBucketsResponse response = s3Client.listBuckets(listBucketsRequest);
			System.out.println(response.buckets());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
