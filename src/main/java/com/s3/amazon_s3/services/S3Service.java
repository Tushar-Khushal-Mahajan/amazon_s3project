package com.s3.amazon_s3.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class S3Service {

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	private final S3Client s3Client;

	public S3Service(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	public PutObjectResponse uploadFile(String fileName, String filePath) {
		Path path = Paths.get(filePath);
		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(fileName).build();

		return s3Client.putObject(putObjectRequest, path);
	}

	public List<String> listFiles() {
		ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();

		ListObjectsV2Response response = s3Client.listObjectsV2(listObjectsV2Request);
		return response.contents().stream().map(object -> object.key()).toList();
	}

	public Resource downloadFile(String imageName) throws IOException {

		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(imageName).build();

		InputStream inputStream = s3Client.getObject(getObjectRequest);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, length);
		}
		byte[] fileBytes = byteArrayOutputStream.toByteArray();
		ByteArrayResource resource = new ByteArrayResource(fileBytes);

		return resource;
	}
}
