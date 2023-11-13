package com.example.locationproducersvc.config;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class AzureBlobConfig {
    @Value("${app.blob.connection-string}")
    private String connectionString;

    @Value("${app.blob.source.container-name}")
    String sourceContainerName;

    @Value("${app.blob.archive.container-name}")
    String archiveContainerName;

    @Bean("containerSourceClient")
    public BlobContainerAsyncClient blobSourceContainerClient() {
        return new BlobContainerClientBuilder().connectionString(connectionString).containerName(sourceContainerName)
                .buildAsyncClient();
    }

    @Bean("containerArchiveClient")
    public BlobContainerAsyncClient blobArchiveContainerClient() {
        return new BlobContainerClientBuilder().connectionString(connectionString).containerName(archiveContainerName)
                .buildAsyncClient();
    }
}
