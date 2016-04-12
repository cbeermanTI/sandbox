package audit

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.model.dstu2.resource.AuditEvent
import grails.rest.RestfulController

class AuditController extends RestfulController<PersistedAuditEvent> {

  def fhirCtx = new FhirContext()

  AuditController() {
    super(PersistedAuditEvent)
  }

  @Override
  protected List<PersistedAuditEvent> listAllResources(Map params) {
    PersistedAuditEvent.findAll {
      if (params?.facilityRef) {
        facilityRef == params.facilityRef
      }
    }?.asList()
  }

  @Override
  protected PersistedAuditEvent createResource() {
    AuditEvent fhirAuditEvent = getFhirBody(AuditEvent)
    PersistedAuditEvent instance = new PersistedAuditEvent()
    instance.event = fhirCtx.newJsonParser().encodeResourceToString(fhirAuditEvent)
    instance.facilityRef = params?.facilityRef
    instance
  }

  protected <T> T getFhirBody(Class<T> clazz) {
    request.withFormat {
      json { fhirCtx.newJsonParser().parseResource(request.reader) }
      xml { fhirCtx.newXmlParser().parseResource(request.reader) }
    } as T
  }

}
