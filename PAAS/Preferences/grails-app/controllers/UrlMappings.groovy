class UrlMappings {

  static mappings = {
    "/preferences"(resources: 'preferences')
    "/preferences/userName/$userName(.$format)?"(controller: 'preferences', action: 'show')
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    "/"(view: "/index")
    "500"(view: '/error')
    "404"(view: '/notFound')
  }
}
