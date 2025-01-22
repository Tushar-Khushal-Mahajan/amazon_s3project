package com.s3.amazon_s3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.s3.amazon_s3.services.S3Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

	@Autowired
	private S3Service s3Service;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			Path tempPath = Files.createTempFile(file.getOriginalFilename(), ".tmp");
			Files.copy(file.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING);

			System.out.println("temp path = " + tempPath);

			s3Service.uploadFile(file.getOriginalFilename(), tempPath.toString());

			Files.deleteIfExists(tempPath);

			return ResponseEntity.ok("File uploaded successfully");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
		}
	}

	@GetMapping("/list")
	public ResponseEntity<?> listFiles() {
		try {
			var files = s3Service.listFiles();
			return ResponseEntity.ok(files);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error listing files: " + e.getMessage());
		}
	}

	@GetMapping("/download/{imageName}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String imageName) {
		try {

			Resource file = s3Service.downloadFile(imageName);

			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName + "\"").body(file);
		} catch (IOException e) {
			return ResponseEntity.status(500).body(null);
		}
	}
}
