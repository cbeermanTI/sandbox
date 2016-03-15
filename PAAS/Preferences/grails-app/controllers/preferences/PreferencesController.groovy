package preferences

import grails.rest.RestfulController

class PreferencesController extends RestfulController<Preferences> {

  PreferencesController() {
    super(Preferences)
  }

  @Override
  protected Preferences queryForResource(Serializable theUserName) {
    Preferences.where {
      userName == theUserName
    }.find()
  }

}
