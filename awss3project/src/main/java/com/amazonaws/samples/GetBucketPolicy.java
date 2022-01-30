package com.amazonaws.samples;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketPolicy;

/**
 * This will show Get bucket policy requests to Amazon S3 using the
 * AWS SDK for Java.
 */
public class GetBucketPolicy {

	public static void main(String[] args) {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    GetBucketPolicy <bucket>\n\n" +
                "Where:\n" +
                "    bucket - the bucket to get the policy from.\n\n" +
                "Example:\n" +
                "    GetBucketPolicy testbucket\n\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bucket_name = args[0];
        String policy_text = null;

        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located
         */
        
        /*
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\naren\\.aws\\credentials), and is in valid format.",
                    e);
        }

        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion("us-east-1")
            .build();
        */
        System.out.format("Getting policy for bucket: \"%s\"\n\n", bucket_name);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
        try {
            BucketPolicy bucket_policy = s3.getBucketPolicy(bucket_name);
            policy_text = bucket_policy.getPolicyText();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        if (policy_text == null) {
            System.out.println("The specified bucket has no bucket policy.");
        } else {
            System.out.println("Returned policy:");
            System.out.println("----");
            System.out.println(policy_text);
            System.out.println("----\n");
        }

        System.out.println("Done!");
    }
	
}
