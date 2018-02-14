package com.themillhousegroup.react

import play.api.mvc.{ Action, Controller }
import play.api.Logger
import javax.inject.Inject
import java.io.File
import play.api.libs.json.Json

class FrontEndServingController @Inject() (private val assets: controllers.Assets) extends Controller {

  private val logger = Logger("FrontEndServingController")

  private val publicDirectory = "/public/"
  private val physicalPublicDirectory = s".$publicDirectory"

  private lazy val physicalAssets: Set[String] = {
    val startingDirectory = new File(physicalPublicDirectory)
    deepList(startingDirectory)
  }

  private def deepList(f: File): Set[String] = {
    val these = f.listFiles.toSet
    val inHere = these.filter(_.isFile).map { f =>
      f.getPath.replace(physicalPublicDirectory, "")
    }
    val belowHere = these.filter(_.isDirectory).flatMap(deepList)
    inHere ++ belowHere
  }

  val listPhysicalAssets = Action(Ok(Json.toJson(physicalAssets)))

  def frontEndPath(clientDir: String, path: String, indexFile: String = "index.html") = {
    val target = determineTargetFile(clientDir + "/" + path, indexFile)
    assets.at(publicDirectory, target, true)
  }

  private def determineTargetFile(fullPath: String, indexFile: String) = {
    if (physicalAssets.contains(fullPath)) {
      logger.debug(s"Serving physical resource: '$fullPath'")
      fullPath
    } else {
      logger.debug(s"Serving virtual resource: '$fullPath'")
      // It's some kind of "virtual resource" -
      // a front-end "route" most likely
      indexFile
    }
  }
}
