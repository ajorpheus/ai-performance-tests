import com.typesafe.config.ConfigFactory


object ConfigLoader {
  private lazy val DEFAULT_API_CONFIG_FILE_NAME: String = "hbase-rest-units"

  private lazy val apiSpecificConfigFileName: String = System.getProperty("apiName", DEFAULT_API_CONFIG_FILE_NAME)
  private lazy val defaultConfig = ConfigFactory.parseResources("default.conf")
  private lazy val apiSpecificConfig = ConfigFactory.parseResources(apiSpecificConfigFileName + ".conf")

  private lazy val config = ConfigFactory.systemProperties()
    .withFallback(apiSpecificConfig)
    .withFallback(defaultConfig)
    .resolve()

  def apply(configurationKey: String): String = config.getString(configurationKey)
}