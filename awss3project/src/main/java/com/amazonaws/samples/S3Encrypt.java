package com.amazonaws.samples;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.model.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * This will show Get bucket encryption requests to Amazon S3 using the
 * AWS SDK for Java.
 */
public class S3Encrypt {

	public static final String BUCKET_NAME = "bucketscott2"; //add your bucket name
    public static final String ENCRYPTED_KEY = "enc-key";
    public static final String NON_ENCRYPTED_KEY = "some-key";

    public static void main(String[] args) {
        System.out.println("calling encryption with customer managed keys");
        S3Encrypt encrypt = new S3Encrypt();

        try {
            //can change to call the other encryption methods
            encrypt.authenticatedEncryption_CustomerManagedKey();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Uses AES/GCM with AESWrap key wrapping to encrypt the key. Uses v2 metadata schema. Note that authenticated
     * encryption requires the bouncy castle provider to be on the classpath. Also, for authenticated encryption the size
     * of the data can be no longer than 64 GB.
     */
    public void authenticatedEncryption_CustomerManagedKey() throws NoSuchAlgorithmException {
        
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        AmazonS3Encryption s3Encryption = AmazonS3EncryptionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCryptoConfiguration(new CryptoConfiguration(CryptoMode.AuthenticatedEncryption))
                .withEncryptionMaterials(new StaticEncryptionMaterialsProvider(new EncryptionMaterials(secretKey)))
                .build();

        AmazonS3 s3NonEncrypt = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
        
        s3Encryption.putObject(BUCKET_NAME, ENCRYPTED_KEY, "some contents");
        s3NonEncrypt.putObject(BUCKET_NAME, NON_ENCRYPTED_KEY, "some other contents");
        System.out.println(s3Encryption.getObjectAsString(BUCKET_NAME, ENCRYPTED_KEY));
        System.out.println(s3Encryption.getObjectAsString(BUCKET_NAME, NON_ENCRYPTED_KEY));
    }

    /**
     * For ranged GET we do not use authenticated encryption since we aren't reading the entire message and can't produce the
     * is enabled, ranged GETs will not be allowed since they do not use authenticated encryption..
     */

    public void authenticatedEncryption_RangeGet_CustomerManagedKey() throws NoSuchAlgorithmException {
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        AmazonS3Encryption s3Encryption = AmazonS3EncryptionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCryptoConfiguration(new CryptoConfiguration(CryptoMode.AuthenticatedEncryption))
                .withEncryptionMaterials(new StaticEncryptionMaterialsProvider(new EncryptionMaterials(secretKey)))
                .build();

        AmazonS3 s3NonEncrypt = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();

        s3Encryption.putObject(BUCKET_NAME, ENCRYPTED_KEY, "some contents");
        s3NonEncrypt.putObject(BUCKET_NAME, NON_ENCRYPTED_KEY, "some other contents");
        System.out.println(s3Encryption.getObjectAsString(BUCKET_NAME, ENCRYPTED_KEY));
        System.out.println(s3Encryption.getObjectAsString(BUCKET_NAME, NON_ENCRYPTED_KEY));
    }
    }
    
