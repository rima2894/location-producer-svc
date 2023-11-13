package com.example.locationproducersvc.service;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.ListBlobsOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Slf4j
@Service
public class AzureBlobService {

    @Autowired
    private BlobContainerClient blobContainerClient;
    @Qualifier("sourceBlobContainer")
    @Autowired
    private BlobClientBuilder sourceBlobClientBuilder;
    @Qualifier("archiveBlobContainer")
    @Autowired
    private BlobClientBuilder archiveblobClientBuilder;
    @Autowired
    private ReactiveProducerService reactiveProducerService;

    @Scheduled(cron = "${app.blob.read.cron}")
    public void sendMessageToKafkaTopic() {
        log.info("method sendMessageToKafkaTopic starts={}", LocalDateTime.now());
        ListBlobsOptions options = new ListBlobsOptions().setMaxResultsPerPage(2).setDetails(new BlobListDetails().setRetrieveDeletedBlobs(false));
        PagedIterable<BlobItem> items = blobContainerClient.listBlobs(options, null);
        for (BlobItem item : items) {
            String data = getFileDataFromStorage(blobContainerClient, item);
            try {
                reactiveProducerService.send(data);
                archive(data, item.getName(), archiveblobClientBuilder);
                deleteFile(sourceBlobClientBuilder, item.getName());
            } catch (Exception e) {
                log.info("Error while producing to Kafka Topic={}", e.getMessage());
            }
        }
    }

    private String getFileDataFromStorage(BlobContainerClient blobContainerClient, BlobItem item) {
        String data = null;
        BlobClient blob = blobContainerClient.getBlobClient(item.getName());
        if (blob.exists()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.download(outputStream);
            byte[] bytes = outputStream.toByteArray();
            if (null != bytes) {
                data = new String(bytes, StandardCharsets.UTF_8);
            }
        }
        return data;
    }

    private void archive(String json, String fileName, BlobClientBuilder blobClientBuilder) {
        try {
            final Path tempFile = Files.createTempFile(null, fileName);
            Files.write(tempFile, json.getBytes(StandardCharsets.UTF_8));
            blobClientBuilder.blobName(fileName).buildClient().uploadFromFile(tempFile.toAbsolutePath().toString(), true);
        } catch (Exception e) {
            log.error("Exception occurred while uploading blob data : {}", e.getMessage());
        }
    }

    private void deleteFile(BlobClientBuilder sourceBlobClientBuilder, String name) {
        try {
            BlobClient blobClient = this.sourceBlobClientBuilder.blobName(name).buildClient();
            if (blobClient.exists())
                blobClient.delete();
        } catch (Exception e) {
            log.error("Exception occurred while deleting blob data : {}", e.getMessage());
        }
    }
}
