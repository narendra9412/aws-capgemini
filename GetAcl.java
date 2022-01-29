package com.amazonaws.samples;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Grant;

import java.util.List;

/**
 * This will show Get bucket ACL requests to Amazon S3 using the
 * AWS SDK for Java.
 */
public class GetAcl {

	public static void getBucketAcl(String bucket_name) {
		
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
        System.out.println("Retrieving ACL for bucket: " + bucket_name);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
        try {
            AccessControlList acl = s3.getBucketAcl(bucket_name);
            List<Grant> grants = acl.getGrantsAsList();
            for (Grant grant : grants) {
                System.out.format("  %s: %s\n", grant.getGrantee().getIdentifier(),
                        grant.getPermission().toString());
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

    public static void getObjectAcl(String bucket_name, String object_key) {
        System.out.println("Retrieving ACL for object: " + object_key);
        System.out.println("                in bucket: " + bucket_name);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
        try {
            AccessControlList acl = s3.getObjectAcl(bucket_name, object_key);
            List<Grant> grants = acl.getGrantsAsList();
            for (Grant grant : grants) {
                System.out.format("  %s: %s\n", grant.getGrantee().getIdentifier(),
                        grant.getPermission().toString());
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        final String USAGE = "\n" +
                "Usage:\n" +
                "  GetAcl <bucket> [object]\n\n" +
                "Where:\n" +
                "  bucket - the bucket to get the access control list (ACL) for\n" +
                "  object - (optional) the object to get the ACL for.\n" +
                "           If object is specified, the retrieved ACL will be\n" +
                "           for the object, not the bucket.\n\n" +
                "Examples:\n" +
                "    GetAcl testbucket\n" +
                "    GetAcl testbucket testobject\n\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bucket_name = args[0];
        String object_key = (args.length > 1) ? args[1] : null;

        if (object_key != null) {
            getObjectAcl(bucket_name, object_key);
        } else {
            getBucketAcl(bucket_name);
        }

        System.out.println("Done!");
    }
	
}
