package de.eemkeen.configuration;

import one.microstream.integrations.spring.boot.types.aws.Aws;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public final class LocalStackUtils {

    private LocalStackUtils() {
    }

    public static S3Client s3Client(Aws awsConfig, String bucketName) throws URISyntaxException {
        S3Client client =
                S3Client.builder()
                        .endpointOverride(new URI(awsConfig.getS3().getEndpointOverride()))
                        .credentialsProvider(
                                StaticCredentialsProvider.create(
                                        AwsBasicCredentials.create(
                                                awsConfig.getS3().getCredentials().getAccessKeyId(),
                                                awsConfig.getS3().getCredentials().getSecretAccessKey())))
                        .region(Region.of(awsConfig.getS3().getRegion()))
                        .build();
        try {
            // localstack seems to require one of these that fails before the next one (from MicroStream)
            // passes. I have no idea why.
            List<Bucket> buckets = client.listBuckets().buckets();
            if (buckets.isEmpty()) {
                client.createBucket(b -> b.bucket(bucketName));
                client.putObject(b -> b.bucket(bucketName).key("/"), RequestBody.empty());
            }
        } catch (Exception e) {
            // just catch it!
        }
        return client;
    }
}
