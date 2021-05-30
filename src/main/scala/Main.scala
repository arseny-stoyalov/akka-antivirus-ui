import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import commons.{MonitoringRequest, ScheduleRequest, SingleScanRequest, StopMonitoringRequest, StopScheduleRequest, StopServiceRequest, StopSingleScanRequest}
import configs.RootConfigs
import pureconfig.generic.auto._
import pureconfig.module.yaml.YamlConfigSource

import scala.swing._

object Main extends SimpleSwingApplication {

  private val remoteConfig = ConfigFactory.load("akka-remote.conf")
  private val appConfigs = {
    YamlConfigSource
      .file("src/main/resources/application.yaml")
      .load[RootConfigs]
      .fold(f => throw new Exception(f.prettyPrint()), s => s)
  }

  private val system = ActorSystem("antivirus-ui", remoteConfig)

  val btnScan: Button = new Button {
    text = "Scan"
    reactions += {
      case event.ButtonClicked(_) =>
        uiActor ! SingleScanRequest(txtPath.text)
    }
  }

  val btnStopScan: Button = new Button {
    text = "Stop Scan"
    reactions += {
      case event.ButtonClicked(_) =>
        uiActor ! StopSingleScanRequest
    }
  }

  val btnSchedule: Button = new Button {
    text = "Schedule"
    reactions += {
      case event.ButtonClicked(_) =>
        uiActor ! ScheduleRequest(txtPath.text, txtCron.text)
    }
  }

  val btnStopSchedule: Button = new Button {
    text = "Stop Schedule"
    reactions += {
      case event.ButtonClicked(_) =>
        uiActor ! StopScheduleRequest
    }
  }

  val btnMonitor: Button = new Button {
    text = "Monitor"
    reactions += {
      case event.ButtonClicked(_) =>
        uiActor ! MonitoringRequest(txtPath.text)
    }
  }

  val btnStopMonitor: Button = new Button {
    text = "Stop Monitor"
    reactions += {
      case event.ButtonClicked(_) =>
        uiActor ! StopMonitoringRequest
    }
  }

  val btnClear: Button = new Button {
    text = "Clear"
    reactions += {
      case event.ButtonClicked(_) =>
        result.peer.setListData(Array.empty[String])
    }
  }

  val btnStop: Button = new Button{
    text = "Stop Service"
    reactions += {
      case event.ButtonClicked(_) =>
        uiActor ! StopServiceRequest
    }
  }

  val result: ListView[String] = new ListView[String]()

  val txtPath: TextField = new TextField(15)
  val txtCron: TextField = new TextField("30 * * * * ? *", 15)

  val uiActor: ActorRef = system.actorOf(Props(classOf[UIActor], result, appConfigs.server))

  def top = new MainFrame {
    title = "Antivirus"
    contents = new FlowPanel {
      contents +=
        new FlowPanel {
          contents += result
        }
      contents +=
        new FlowPanel {
          contents += new Label("Path")
          contents += txtPath
          contents += new Label("Cron")
          contents += txtCron
        }
      contents += new FlowPanel {
        contents += btnScan
        contents += btnStopScan
        contents += btnSchedule
        contents += btnStopSchedule
        contents += btnMonitor
        contents += btnStopMonitor
        contents += btnClear
        contents += btnStop
      }

    }

    pack()
    centerOnScreen()
    open()
  }

}
