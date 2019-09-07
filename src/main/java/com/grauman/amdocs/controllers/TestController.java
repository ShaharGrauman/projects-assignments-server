package com.grauman.amdocs.controllers;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@GetMapping("/string")
	public ResponseEntity<String> test1() throws IOException {
		return ResponseEntity
				.ok()
				.body("Hello there");
	}
	
	@GetMapping("/string-array")
	public ResponseEntity<List<String>> test2() {
		throw new IndexOutOfBoundsException("You're kookoo");
//		return ResponseEntity
//				.ok()
//				.body(Arrays.asList("Shahar", "Grauman"));
	}
	
	@GetMapping("/bad-request")
	public ResponseEntity<String> test3() {
		return ResponseEntity
					.badRequest()
					.body("Bad request");
	}
	
	@GetMapping("/with-headers")
	public ResponseEntity<String> test4() {
		return ResponseEntity.ok()
				.header("My-Header", "my value")
				.body("Check out response header");
	}
}
