package com.example.locationproducersvc.controller;

import com.example.locationproducersvc.service.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Controller {
	@Autowired
	private AzureBlobService azureBlobService;

	@GetMapping("/loadLocationData")
	public  Mono<String> loadJson()
	{
		 return azureBlobService.readExcelFromAzureBlobAndArchive();
	}
	
}
