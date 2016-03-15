package preferences

import grails.converters.JSON
import groovy.json.JsonSlurper

class Preferences {

  /**
   * globally unique user name
   */
  String userName

  Map<String, Object> preferences = null

  static transients = ['preferences']

  static mapping = {
  }

  static constraints = {
  }

  String getPreferencesJSON() {
    preferences ? (preferences as JSON).toString() : null
  }

  void setPreferencesJSON(String prefs) {
    preferences = prefs ? (new JsonSlurper().parseText(prefs)) as Map : null
  }

}
