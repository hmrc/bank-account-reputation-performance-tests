
# bank-account-reputation-performance-tests

Performance test repository for bank-account-reputation service

This repository contains tests for both the backend and frontend service for bank-account-reputation. The default journeys run the backend services.

### Smoke test

It might be useful to try the journey with one user to check that everything works fine before running the full performance test
```
sbt -Dperftest.runSmokeTest=true -Dperftest.runLocal=true gatling:test
```

### Run the performance test
```
sbt gatling:test
```

### Run the performance test for frontend service
```
sbt -DjourneysToRun.0=bars-frontend-simulation gatling:test
```