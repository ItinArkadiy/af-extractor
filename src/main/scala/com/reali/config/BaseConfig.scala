package com.reali.config

import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient

import scala.util.{Failure, Success, Try}

object BaseConfig {
  private val log = LoggerFactory.getLogger(this.getClass)

  private val envConf = {
    val cfgKey = "config.resource"
    sys.env.get(cfgKey).foreach(cfg => sys.props.put(cfgKey, cfg))
    ConfigFactory.load(sys.props(cfgKey))
  }

  private val baseConf = {
    Try(loadSecret(envConf)) match {
      case Success(secret) =>
        log.info("Merging secret with {}", envConf.origin)
        secret.withFallback(envConf)
      case Failure(ex) =>
        log.warn(s"Failed to load secret, will use local ${envConf.origin}", ex)
        envConf
    }
  }

  private def loadSecret(envConf: Config): Config = {
    val clientBuilder = SecretsManagerClient.builder
    val secretConf = ConfigFactory.load("secret.conf")
    val key = "aws.secret.access-key"
    val secKey = "aws.secret.secret-key"
    if(secretConf.hasPath(key) && secretConf.hasPath(secKey)) clientBuilder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(secretConf.getString(key), secretConf.getString(secKey))))
    val regKey = "aws.secret.region"
    if(envConf.hasPath(regKey)) clientBuilder.region(Region.of(envConf.getString(regKey)))
    log.info("Loading secret...")
    val request = GetSecretValueRequest.builder.secretId(envConf.getString("aws.secret.name")).build
    val response = clientBuilder.build.getSecretValue(request)
    ConfigFactory.parseString(response.secretString).resolve
  }
}

trait BaseConfig {
  protected[this] val baseConf: Config = BaseConfig.baseConf
}
