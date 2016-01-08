class UrlMappings {

  static mappings = {
    "/facility"(resources: 'facility')
    "/facility/ref/$myRefS(.$format)?"(controller: 'facility', action: 'show')
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
