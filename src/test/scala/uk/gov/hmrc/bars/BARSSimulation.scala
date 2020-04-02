package uk.gov.hmrc.bars

import uk.gov.hmrc.bars.AssessBusinessDetailsRequests._
import uk.gov.hmrc.bars.ValidateBankDetailsRequests._
import uk.gov.hmrc.performance.simulation.PerformanceTestRunner

class BARSSimulation extends PerformanceTestRunner {

  setup("validate", "Validate Bank Details") withRequests (navigateToHomePage, validateBankDetails)
  setup("business-assess", "Assess business details") withRequests (assessBusinessBankDetails)

  runSimulation()
}