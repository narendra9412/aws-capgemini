package com.amazonaws.samples;

import java.io.IOException;
import java.util.List;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetBucketLoggingConfigurationRequest;
;


/**
 * This will show list of bucket and logging information of your aws account requests to Amazon S3 using the
 * AWS SDK for Java.
 */
public class S3Sample {

    public static void main(String[] args) throws IOException {

        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located
         */
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

        String bucketName = "device-s3-bucke";
        //String key = "MyObjectKey";
        System.out.println("Getting with Amazon S3");
        System.out.println("===========================================\n");

        try {
            /*
             * List the buckets in your account
             */
            
            System.out.println("Listing buckets");
            
            List<Bucket> buckets = s3.listBuckets();
            System.out.println("Your Amazon S3 buckets are:");
            for (Bucket b : buckets) {
                System.out.println("* " + b.getName());
            }
            
            System.out.println();
            
            GetBucketLoggingConfigurationRequest request = new GetBucketLoggingConfigurationRequest(bucketName);
            s3.getBucketLoggingConfiguration(bucketName);
            System.out.println(request);
         /*                                   
         // get list of bucket permission
            List<String> bucketPermissions = s3
                .getBucketAcl(bucketName)
                .getGrantsAsList().stream().distinct()
                .map(t -> t.getPermission().toString())
                .collect(Collectors.toList());
            System.out.println("bucketPermissions---->"+bucketPermissions);

            // check read/write or full control permission
            if (
                !((bucketPermissions.contains("READ")
                && bucketPermissions.contains("WRITE"))
                || (bucketPermissions.contains("FULL_CONTROL")))) {
            throw new NoPermissionException();
            }
   */         

    } catch (AmazonServiceException ase) {
         System.out.println("Caught an AmazonServiceException, which means your request made it "
                  + "to Amazon S3, but was rejected with an error response for some reason.");
    } 
        //catch (NoPermissionException e) {
		
		//e.printStackTrace();
	//}
}

}
