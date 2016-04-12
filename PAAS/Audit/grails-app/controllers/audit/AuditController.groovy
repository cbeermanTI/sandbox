package audit

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.model.dstu2.resource.AuditEvent
import com.cds.nexus.XSRef
import grails.rest.RestfulController

class AuditController extends RestfulController<PersistedAuditEvent> {

  def fhirCtx = new FhirContext()

  AuditController() {
    super(PersistedAuditEvent)
  }

  @Override
  protected List<PersistedAuditEvent> listAllResources(Map params) {
    PersistedAuditEvent.createCriteria().list {
      params?.facilityRef ? eq("facilityRef", params.facilityRef) : isNull("facilityRef")
      if (params?.appId) { eq("appId", Short.valueOf(params.appId)) }
      if (params?.shardId) { eq("shardId", Short.valueOf(params.shardId)) }
      if (params?.instanceId) { eq("instanceId", Integer.valueOf(params.instanceId)) }
    }
  }

  @Override
  protected PersistedAuditEvent createResource() {
    AuditEvent fhirAuditEvent = getFhirBody(AuditEvent)
    PersistedAuditEvent instance = new PersistedAuditEvent()
    instance.event = fhirCtx.newJsonParser().encodeResourceToString(fhirAuditEvent)
    instance.facilityRef = params?.facilityRef
    instance.myRef = fhirAuditEvent.id.value.replaceAll('AuditEvent/', '')
    XSRef xsRef = new XSRef(instance.myRef)
    instance.appId = xsRef.serviceId
    instance.shardId = xsRef.shardId
    instance.instanceId = xsRef.instanceId
    instance
  }

  protected <T> T getFhirBody(Class<T> clazz) {
    request.withFormat {
      json { fhirCtx.newJsonParser().parseResource(request.reader) }
      xml { fhirCtx.newXmlParser().parseResource(request.reader) }
    } as T
  }

}
