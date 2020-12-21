package com.reali.config

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.AwsRegionProvider
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManagerBuilder

case class S3Info(bucket: String, region: String)

trait AWSConfig extends BaseConfig {
  private val awsConfig = baseConf.getConfig("extractor.aws")

  val s3 = S3Info(
    awsConfig.getString("s3.bucket"),
    awsConfig.getString("s3.region")
  )

  val regionProvider = new AwsRegionProvider {
    def getRegion: String = s3.region
  }

  val awsCredentialsProvider = DefaultAWSCredentialsProviderChain.getInstance()

  val s3Client = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(regionProvider.getRegion).build()

  val transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).withMultipartUploadThreshold(5 * 1024 * 1025).build()

}
