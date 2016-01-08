package com.cds.nexus

import grails.transaction.Transactional
import grails.plugins.rest.client.RestBuilder

/**
 * Application interface to the Audit service
 */
@Transactional
class AuditService {

  // value defined in application.yml
  String auditServiceUrl
  String eventsForFacilityUrl

  def getAuditRecord(String auditRecordRef) {
    def rest = new RestBuilder()
    def auditRecord = rest.get("${auditServiceUrl}/${auditRecordRef}.json").json
    [event     : auditRecord.event,
     ref       : auditRecord.myRefS,
     facilityId: auditRecord.facilityRefS]
  }

  def getAuditRecords() {
    def rest = new RestBuilder()
    def json = rest.get("${auditServiceUrl}.json").json
    def auditRecords = json.collect { auditRecord ->
      [event     : auditRecord.event,
       ref       : auditRecord.myRefS,
       facilityId: auditRecord.facilityRefS]
    }
  }

  def getRecordsForFacility(String facilityRef) {
    def rest = new RestBuilder()
    def url = "${eventsForFacilityUrl}/${facilityRef}.json"
    def auditEvents = rest.get(url).json
    auditEvents.collect { auditEvent ->
      [event: auditEvent.event]
    }
  }
}
