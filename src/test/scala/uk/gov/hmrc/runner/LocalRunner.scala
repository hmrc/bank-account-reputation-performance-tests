package uk.gov.hmrc.runner

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder
import uk.gov.hmrc.bars.BARSSimulation

object LocalRunner {

  def main(args: Array[String]): Unit = {

    val simClass = classOf[BARSSimulation].getName

    val props = new GatlingPropertiesBuilder
    props.simulationClass(simClass)

    Gatling.fromMap(props.build)
  }

}
