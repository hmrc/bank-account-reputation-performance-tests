
# bank-account-reputation-performance-tests

Performance test repository for bank-account-reputation service

This repository contains tests for both the backend and frontend service for bank-account-reputation. The default journeys run the backend services.

### Current Volumetrics (JPS config)

#### Bank Account Reputation Frontend
> Last checked/updated: **2026-02-24**
>
> Based on a profile of the service in production over the past year, the max requests/minute is ~200.

The following configuration of journeys per second (JPS) results in around ~500 requests/minute, which is a good level for testing the performance of the service without overwhelming it. It also provides us with more than enough headroom.

- BARS Frontend Simulation: **15 JPS**

#### Bank Account Reputation API
> Last checked/updated: **2026-02-24**
>
> Based on a profile of the service in production over the past year, the max requests/minute is ~300.

The following configuration of journeys per second (JPS) results in around ~1000 requests/minute, which is a good level for testing the performance of the service without overwhelming it. It also provides us with more than enough headroom.

- BARS Business Simulation: **8 JPS**
- BARS Individual Simulation: **5 JPS**,
- BARS Validate Simulation: **15 JPS**

### Smoke test

It might be useful to try the journey with one user to check that everything works fine before running the full performance test.

You can execute `./run_tests.sh` to run the smoke test; or alternatively, you can run the following command:
```bash
  sbt -Dperftest.runSmokeTest=true -Dperftest.runLocal=true gatling:test
```

### Run the performance test
```bash
  sbt gatling:test
```

### Run the performance test for frontend service
```bash
  sbt -DjourneysToRun.0=bars-frontend-simulation gatling:test
```
