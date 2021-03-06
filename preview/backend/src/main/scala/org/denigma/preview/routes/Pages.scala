package org.denigma.preview.routes

import akka.http.extensions.pjax.PJax
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import org.denigma.controls.Twirl
import play.twirl.api.Html

class Pages extends Directives with PJax{

  def defaultPage: Option[Html] = {
    Some(html.readme())
  }

  def index =  pathSingleSlash{ctx=>
    ctx.materializer.executionContext
    ctx.complete {
      HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`.withCharset(HttpCharsets.`UTF-8`), html.index(defaultPage).body  ))
    }
  }

  val loadPage: Html=>Html = h=>html.index(Some(h))


  def test = pathPrefix("test"~Slash) { ctx=>
      pjax[Twirl](Html(s"<h1>${ctx.unmatchedPath}</h1>"),loadPage){h=>c=>
        val resp = HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`.withCharset(HttpCharsets.`UTF-8`), h.body  ))
        c.complete(resp)
      }(ctx)
    }


  def routes = index ~ test


}