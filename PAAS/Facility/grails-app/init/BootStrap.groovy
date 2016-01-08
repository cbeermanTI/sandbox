import com.cds.nexus.XSRef.XSRefIssuer
import facility.Facility

class BootStrap {

  def init = { servletContext ->
    short facilityServiceId = 10
    XSRefIssuer facilityRefIssuer = new XSRefIssuer(facilityServiceId)

    new Facility(
        body: 'Facility 1',
        name: 'Lost Altos Clinic',
        myRef: facilityRefIssuer.issueId()).save()
    new Facility(
        body: 'Facility 2',
        name: 'Jet Propulsion Lab',
        myRef: facilityRefIssuer.issueId()).save()
  }

  def destroy = {
  }
}
