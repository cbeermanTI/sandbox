package auditTrail

class AuditTrailController {

  def facilityService
  def auditService

  def index() {
    def facilities = facilityService.facilities
    [facilities: facilities]
  }

  def search(String facilityRef) {
    def facility = facilityService.getFacility(facilityRef)
    def facName = facility ? facility.facilityName : 'No Facility'
    def auditRecords = auditService.getRecordsForFacility(facilityRef)
    render view: 'results', model: [auditRecords: auditRecords, facility: facName]
  }
}
