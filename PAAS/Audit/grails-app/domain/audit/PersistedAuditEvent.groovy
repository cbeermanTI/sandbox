package audit

import com.cds.nexus.XSRef

class PersistedAuditEvent {

  String event
  String myRef
  String facilityRef

  static transients = ['myReference', 'facilityReference']

  static constraints = {
    myRef(nullable: true)
    facilityRef(nullable: true)
  }

  static mapping = {
    event type: 'text'
  }

  XSRef getMyReference() {
    if (myRef) {
      return new XSRef(myRef)
    } else {
      return null
    }
  }

  void setMyReference(XSRef ref) {
    myRef = ref?.getSerialized()
  }

  XSRef getFacilityReference() {
    if (facilityRef) {
      return new XSRef(facilityRef)
    } else {
      return null
    }
  }

  void setFacilityReference(XSRef ref) {
    facilityRef = ref?.getSerialized()
  }
}
