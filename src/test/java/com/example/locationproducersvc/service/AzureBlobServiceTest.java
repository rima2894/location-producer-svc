package com.example.locationproducersvc.service;

import com.azure.core.http.rest.PagedFlux;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.models.BlobItem;
import com.example.locationproducersvc.config.AzureBlobConfig;
import com.example.locationproducersvc.dto.MessageDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AzureBlobServiceTest {

	@Mock
	private AzureBlobConfig azureBlobConfig;

	@Mock
	private ReactiveProducerService reactiveProducerService;

	@Mock
	private PagedFlux<BlobItem> pagedFlux;

	@InjectMocks
	private AzureBlobService azureBlobService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/*@Test
	void testReadExcelFromAzureBlobAndArchive() {
		// Mocking the BlobContainerClient and BlobItem
		BlobContainerAsyncClient blobSourceContainerAsyncClient = mock(BlobContainerAsyncClient.class);
		when(azureBlobConfig.blobSourceContainerClient()).thenReturn(blobSourceContainerAsyncClient);
		when(azureBlobConfig.blobArchiveContainerClient()).thenReturn(blobSourceContainerAsyncClient);

		BlobItem blobItem = new BlobItem();
		blobItem.setName("testBlobItem");
		when(blobSourceContainerAsyncClient.listBlobs(any())).thenReturn(pagedFlux);

		// Mocking the reactiveProducerService
		when(reactiveProducerService.send(any())).thenReturn(Mono.empty());

		MessageDetails obj = new MessageDetails("testFileName","testMsg");
		// Mocking upload and delete methods
		when(azureBlobService.upload(obj)).thenReturn(Mono.just(obj));
		when(azureBlobService.delete(obj)).thenReturn(Mono.just(any()));

		// Testing the readExcelFromAzureBlobAndArchive method
		StepVerifier.create(azureBlobService.readExcelFromAzureBlobAndArchive())
				.expectNext("Upload and delete completed successfully")
				.verifyComplete();

		// Verifying that the methods are called with the correct arguments
		verify(blobSourceContainerAsyncClient).getBlobAsyncClient("testBlobItem");
		verify(reactiveProducerService).send(new MessageDetails("testBlobItem", "testMsg"));
		verify(azureBlobService).upload(new MessageDetails("testBlobItem", "testMsg"));
		verify(azureBlobService).delete(new MessageDetails("testBlobItem", "testMsg"));
	}*/
}
