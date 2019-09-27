package uk.gov.hmrc.perftests.example

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.example.ValidateBankDetailsRequests._

class ValidateBankDetailsSimulation extends PerformanceTestRunner {

  setup("validate", "Validate Bank Details") withRequests (navigateToHomePage, validateBankDetails)

  runSimulation()
}