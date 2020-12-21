package com.reali.db.crmls.client

import java.time.{LocalDate, LocalDateTime}

import com.reali.db.crmls.model.WnCrmlsListingPropertiesModel
import scalikejdbc._

object WnCrmlsClient {

  implicit val session = AutoSession

  def getListings: List[WnCrmlsListingPropertiesModel] = {
    val listings =  NamedDB('crmls) readOnly { implicit session =>
      val pointer = sql"SELECT date_prod FROM wolfnet_mockup.mlsds_pointers".map(rs => rs.localDateTime("date_prod"))
        .single().apply().getOrElse(LocalDateTime.now()).minusDays(10)
      sql"SELECT property_id, city, county, display_address, last_update_date, listing_price FROM wolfnet_mockup.mlsds_properties WHERE last_update_date > ${pointer} :: timestamp"
        .stripMargin
        .map(rs =>
          WnCrmlsListingPropertiesModel(
            rs.string("property_id"),
            rs.string("city"),
            rs.string("county"),
            rs.string("display_address"),
            rs.localDateTime("last_update_date"),
            rs.string("listing_price"))
        ).list.apply()
    }
    NamedDB('crmls) localTx { implicit session =>
      sql"UPDATE wolfnet_mockup.mlsds_pointers SET date_prod=${LocalDateTime.now()}".update().apply()
    }
    listings
  }

}

