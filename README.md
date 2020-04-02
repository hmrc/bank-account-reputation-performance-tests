
# bank-account-reputation-performance-tests

### Smoke test

It might be useful to try the journey with one user to check that everything works fine before running the full performance test
```
sbt -Dperftest.runSmokeTest=true -Dperftest.runLocal=true gatling:test
```

### Run the performance test
```
sbt gatling:test
```