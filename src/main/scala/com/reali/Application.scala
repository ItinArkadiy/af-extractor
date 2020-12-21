package com.reali

import com.reali.db.crmls.client.WnCrmlsClient
import com.reali.db.s3.Sink
import scalikejdbc.config.DBs


object Application extends App{
  DBs.setupAll()
  val listings = WnCrmlsClient.getListings
  Sink.saveToS3(listings)
}