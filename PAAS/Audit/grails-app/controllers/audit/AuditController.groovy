package audit

import grails.rest.RestfulController

class AuditController extends RestfulController<AuditEvent> {

  AuditController() {
    super(AuditEvent)
  }

  @Override
  protected List<AuditEvent> listAllResources(Map params) {
    AuditEvent.where {
      if (params.facilityRef) {
        facilityRefS == params.facilityRef
      }
    }.list()
  }
}
