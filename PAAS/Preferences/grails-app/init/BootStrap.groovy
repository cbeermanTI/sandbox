import preferences.Preferences

class BootStrap {

  def init = { servletContext ->
    new Preferences(
        userName: 'fred',
        preferences: ['color': 'blue', 'ride': 'rocket'] as Map
    ).save()
    new Preferences(
        userName: 'molly',
        preferences: ['color': 'ref', 'ride': 'pony'] as Map
    ).save()
  }

  def destroy = {
  }
}
