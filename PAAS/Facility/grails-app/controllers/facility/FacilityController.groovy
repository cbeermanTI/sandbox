package facility

import grails.rest.RestfulController

class FacilityController extends RestfulController<Facility> {

  FacilityController() {
    super(Facility)
  }

  @Override
  protected Facility queryForResource(Serializable reference) {
    Facility.where {
      myRefS == reference
    }.find()
  }

}
