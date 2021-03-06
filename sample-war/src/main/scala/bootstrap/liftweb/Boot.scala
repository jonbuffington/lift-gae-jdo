/*
 * Copyright 2007-2008 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package bootstrap.liftweb

import _root_.net.liftweb._
import _root_.net.liftweb.common._
import http._
import sitemap._
import provider._

import com.jcraft.lift.model._

import javax.jdo.JDOHelper
import org.scala_libs.jdo.{JdoConfig, RequestPersistenceManagerSource}

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    JdoConfig.defaultPmSource = new RequestPersistenceManagerSource(JDOHelper.getPersistenceManagerFactory("transactions-optional"))

    LiftRules.addToPackages("com.jcraft.lift")


    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
    Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
    Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    LiftRules.setSiteMap(SiteMap(MenuInfo.menu :_*))

  }

  private def makeUtf8(req: HTTPRequest) = {
    req.setCharacterEncoding("UTF-8")
  }
}

object MenuInfo {
  import Loc._

  def menu: List[Menu] =  
       Menu(Loc("Home", "index" :: Nil, "Home")) ::
       Author.menus :::
       Book.menus :::
       Menu(Loc("Book Search", List("books", "search"), "Book Search")) ::
       Nil
}
