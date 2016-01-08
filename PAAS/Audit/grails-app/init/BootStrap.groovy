import audit.AuditEvent
import com.cds.nexus.XSRef.XSRefIssuer

class BootStrap {

  def init = { servletContext ->
    short facilityServiceId = 10
    XSRefIssuer facilityRefIssuer = new XSRefIssuer(facilityServiceId)
    short auditServiceId = 7
    XSRefIssuer auditRefIssuer = new XSRefIssuer(auditServiceId)
    byte[] macAddress = XSRefIssuer.getMyMacAddress()
    new AuditEvent(
        event: 'this happened',
        facilityRef: facilityRefIssuer.issueId(),
        myRef: auditRefIssuer.issueTestId(macAddress, (short) 1, 19)).save()
    new AuditEvent(
        event: 'something happened',
        facilityRef: facilityRefIssuer.issueId(),
        myRef: auditRefIssuer.issueTestId(macAddress, (short) 1, 22)).save()
  }

  def destroy = {
  }
}
