package facility

import com.cds.nexus.XSRef

class Facility {

  XSRef myRef
  String name
  String body

  static transients = ['myRef']

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
}
