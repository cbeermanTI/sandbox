package com.cds.nexus

import grails.transaction.Transactional
import grails.plugins.rest.client.RestBuilder
import groovy.util.logging.Log4j

/**
 * Application interface to the Facility service
 */
@Transactional
class FacilityService {

  // value defined in application.yml
  String facilityServiceUrl

  // @Cacheable('facilityData')
  def getFacility(String facilityRef) {
    def rest = new RestBuilder()
    def url = "${facilityServiceUrl}/${facilityRef}.json"
    def facility = rest.get(url).json
    if (facility) {
      return [facilityName: facility.name, body: facility.body, facilityRef: facility.myRefS]
    } else {
      return [facilityName: 'not found', body: 'bodiless', facilityRef: facilityRef]
    }
  }

  // @Cacheable('facilityData')
  def getFacilities() {
    def rest = new RestBuilder()
    def url = "${facilityServiceUrl}.json"
    def json = rest.get(url).json
    def facilities = json.collect { facility ->
      [facilityName: facility.name, body: facility.body, facilityRef: facility.myRefS]
    }
    facilities
  }

}
