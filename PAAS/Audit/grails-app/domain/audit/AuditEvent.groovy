package audit

import com.cds.nexus.XSRef

class AuditEvent {

  String event
  XSRef myRef
  XSRef facilityRef

  static transients = ['myRef', 'facilityRef']

  static constraints = {
  }

  String getMyRefS() {
    myRef?.getSerialized()
  }

  void setMyRefS(String s) {
    if (!myRef) {
      myRef = new XSRef(s)
    }
  }

  String getFacilityRefS() {
    facilityRef?.getSerialized()
  }

  void setFacilityRefS(String s) {
    if (!facilityRef) {
      facilityRef = new XSRef(s)
    }
  }
}
