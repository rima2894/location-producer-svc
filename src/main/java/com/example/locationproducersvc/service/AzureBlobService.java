package com.example.locationproducersvc.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.example.locationproducersvc.config.AzureBlobConfig;
import com.example.locationproducersvc.dto.MessageDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
@EnableScheduling
public class AzureBlobService {

    @Autowired
    private final AzureBlobConfig azureBlobConfig;
    @Autowired
    private ReactiveProducerService reactiveProducerService;

    public Mono<String> readExcelFromAzureBlobAndArchive() {
        return azureBlobConfig.blobSourceContainerClient()
                .listBlobs(new ListBlobsOptions()
                        .setMaxResultsPerPage(2)
                        .setDetails(new BlobListDetails().setRetrieveDeletedBlobs(false)))
                .flatMap(blobItem ->
                        azureBlobConfig.blobSourceContainerClient()
                                .getBlobAsyncClient(blobItem.getName()).downloadContent()
                                .flatMap(data ->
                                        reactiveProducerService.send(new MessageDetails(blobItem.getName(), data.toString()))
                                )
                                .flatMap(this::upload)
                                .flatMap(this::delete)
                )
                .then(Mono.just("Blob moved to archive successfully"));
    }


    public Mono<MessageDetails> upload(MessageDetails messageDetails) {

        return Mono.just(azureBlobConfig.blobArchiveContainerClient().getBlobAsyncClient(messageDetails.getFileName()))
                .flatMap(blobAsyncClient -> {
                    return blobAsyncClient
                            .upload(BinaryData.fromString(messageDetails.getMsg()))
                            .doOnSuccess(blockBlobItem -> log.info("Successfully uploaded in azure blob. URI created {}", blobAsyncClient.getBlobUrl()))
                            .map(blockBlobItem -> blobAsyncClient.getBlobUrl()).map(x -> messageDetails);
                }).onErrorResume(exception -> {
                    log.error("Upload to Azure blob failed with error {}", exception.getMessage());
                    return Mono.empty();
                });
    }


    public Mono<String> delete(MessageDetails messageDetails) {
        String fileName = messageDetails.getFileName();
        return Mono.just(azureBlobConfig.blobSourceContainerClient().getBlobAsyncClient(fileName))
                .flatMap(blobAsyncClient -> {
                    return blobAsyncClient
                            .delete()
                            .doOnSuccess(blockBlobItem -> log.info("Archiving successfully in azure blob. ", blobAsyncClient.getBlobUrl()))
                            .map(blockBlobItem -> blobAsyncClient.getBlobUrl());
                }).onErrorResume(exception -> {
                    log.error("Deletion in Azure blob failed with error {}", exception.getMessage());
                    return Mono.empty();
                });
    }
}
