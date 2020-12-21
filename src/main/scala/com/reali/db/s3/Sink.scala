package com.reali.db.s3

import java.time.LocalDate

import com.reali.config.AWSConfig
import com.reali.db.crmls.model.WnCrmlsListingPropertiesModel
import org.json4s._
import org.json4s.jackson.Serialization.write

import scala.util.Try

object Sink extends AWSConfig {

  implicit val formats = DefaultFormats

  def saveToS3 (listings : List[WnCrmlsListingPropertiesModel]): Try[Unit] =
    Try {
      val listingsJSON = write(listings)
      val fileName = "wn_crmls/"+LocalDate.now().toString
      s3Client.putObject(s3.bucket, s"$fileName.json", listingsJSON)
    }








}
