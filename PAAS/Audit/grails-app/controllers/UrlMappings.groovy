class UrlMappings {

  static mappings = {
    "/audit"(resources: 'audit')
    "/audit/forFacility/$facilityRef(.$format)?"(controller: 'audit', action: 'index')

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
