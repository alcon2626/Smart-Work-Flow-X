//
// Copyright 2016 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.9
//
package com.amazonaws.mobile;

import com.amazonaws.regions.Regions;

/**
 * This class defines constants for the developer's resource
 * identifiers and API keys. This configuration should not
 * be shared or posted to any public source code repository.
 */
public class AWSConfiguration {

    // AWS MobileHub user agent string
    public static final String AWS_MOBILEHUB_USER_AGENT =
        "MobileHub 25746f59-54f4-4997-a0ec-266353ca8d5a aws-my-sample-app-android-v0.9";
    // AMAZON COGNITO
    public static final Regions AMAZON_COGNITO_REGION =
      Regions.fromName("us-east-1");
    public static final String  AMAZON_COGNITO_IDENTITY_POOL_ID =
        "us-east-1:783bec23-9f5c-473f-9bfc-97b725a259c8";
    // Custom Developer Provided Authentication ID
    public static final String DEVELOPER_AUTHENTICATION_PROVIDER_ID =
        "smartworkflow.leonel.com";
    // Developer Authentication - URL for Create New Account
    public static final String DEVELOPER_AUTHENTICATION_CREATE_ACCOUNT_URL =
        "aws.amazon.com";
    // Developer Authentication - URL for Forgot Password
    public static final String DEVELOPER_AUTHENTICATION_FORGOT_PASSWORD_URL =
        "aws.amazon.com";
    // Account ID
    public static final String DEVELOPER_AUTHENTICATION_ACCOUNT_ID =
        "568101406044";
    public static String DEVELOPER_AUTHENTICATION_DISPLAY_NAME = "Custom";
    // AMAZON MOBILE ANALYTICS
    public static final String  AMAZON_MOBILE_ANALYTICS_APP_ID =
        "053e7166b1584b24bfc1daf1c1a56346";
    // Amazon Mobile Analytics region
    public static final Regions AMAZON_MOBILE_ANALYTICS_REGION = Regions.US_EAST_1;
    // Google Client ID for Web application
    public static final String GOOGLE_CLIENT_ID =
        "390858261168-u6ju4oioebajagm6ht4kb0v40o5dq14k.apps.googleusercontent.com";
    // GOOGLE CLOUD MESSAGING SENDER ID
    public static final String GOOGLE_CLOUD_MESSAGING_SENDER_ID =
        "390858261168";

    // SNS PLATFORM APPLICATION ARN
    public static final String AMAZON_SNS_PLATFORM_APPLICATION_ARN =
        "arn:aws:sns:us-east-1:568101406044:app/GCM/smartworkflow_MOBILEHUB_1238073234";
    public static final Regions AMAZON_SNS_REGION =
         Regions.fromName("us-east-1");
    // SNS DEFAULT TOPIC ARN
    public static final String AMAZON_SNS_DEFAULT_TOPIC_ARN =
        "arn:aws:sns:us-east-1:568101406044:smartworkflow_alldevices_MOBILEHUB_1238073234";
    // SNS PLATFORM TOPIC ARNS
    public static final String[] AMAZON_SNS_TOPIC_ARNS =
        {};
    public static final String AMAZON_CONTENT_DELIVERY_S3_BUCKET =
        "smartworkflow-contentdelivery-mobilehub-1238073234";
    public static final Regions AMAZON_CONTENT_DELIVERY_S3_REGION =
       Regions.fromName("us-east-1");
    public static final String AMAZON_CLOUD_FRONT_DISTRIBUTION_DOMAIN =
        "d3izrrru5zhya1.cloudfront.net";
    public static final Regions AMAZON_DYNAMODB_REGION =
       Regions.fromName("us-east-1");
}