package com.reali.db.crmls.model

import java.time.LocalDateTime

import scalikejdbc.WrappedResultSet

case class WnCrmlsListingPropertiesModel(propertyId: String,
                                    city: String,
                                    county: String,
                                    displayAddress: String,
                                    lastUpdateDate: LocalDateTime,
                                    listingPrice:String)


