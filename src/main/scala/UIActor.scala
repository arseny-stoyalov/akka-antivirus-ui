import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import commons.{RequestToService, ServiceAnswer, StoppedScanResponse}
import configs.ServiceConfigs

import scala.swing.ListView

case class SendToServer(s: String)

class UIActor(logsView: ListView[String], configs: ServiceConfigs) extends Actor with LazyLogging {

  private val service =
    context.actorSelection(s"${configs.host}:${configs.port}/${configs.actor}")

  override def receive: Receive = {
    case r: RequestToService =>
      service ! r

    case s: ServiceAnswer =>
      logger.info(s"Got $s from service")
      val list = logsView.listData
      lazy val transformed = list.map(l => if (l.split(" ")(0) == s.path) s.toString else l)
      if (list.exists(_.contains(s.path))) logsView.peer.setListData(transformed.toArray)
      else logsView.peer.setListData(list.toArray :+ s.toString)

    case r: StoppedScanResponse =>
      val filtered = logsView.listData.filterNot(_.contains(r.source))
      logsView.peer.setListData(filtered.toArray)
  }
}
