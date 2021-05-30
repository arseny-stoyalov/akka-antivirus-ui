package configs

case class RootConfigs (server: ServiceConfigs)

case class ServiceConfigs(host: String, port: Int, actor: String)