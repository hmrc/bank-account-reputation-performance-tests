#!/bin/bash -e

sm2 --stop-all

sm2 --start BANK_ACCOUNT_REPUTATION_FRONTEND_SERVICES BANK_ACCOUNT_REPUTATION_THIRD_PARTIES_STUB --appendArgs '{
  "BANK_ACCOUNT_REPUTATION": [
    "-Dplay.http.router=testOnlyDoNotUseInAppConf.Routes",
    "-Dmicroservice.services.modulr.enabled=true",
    "-Dmicroservice.services.modulr.business.cache.enabled=false",
    "-Dmicroservice.services.modulr.personal.cache.enabled=false",
    "-Dauditing.enabled=false",
    "-Dproxy.proxyRequiredForThisEnvironment=false",
    "-Dmicroservice.services.eiscd.aws.endpoint=http://localhost:4566",
    "-Dmicroservice.services.eiscd.aws.bucket=txm-dev-bacs-eiscd",
    "-Dmicroservice.services.eiscd.cache-schedule.initial-delay=86400",
    "-Dmicroservice.services.modcheck.cache-schedule.initial-delay=86400",
    "-Dmicroservice.services.thirdPartyCache.endpoint=http://localhost:9899/cache",
    "-Dmicroservice.services.access-control.endpoint.verify.enabled=true",
    "-Dmicroservice.services.access-control.endpoint.verify.allow-list.0=bars-performance-tests",
    "-Dmicroservice.services.access-control.endpoint.validate.enabled=true",
    "-Dmicroservice.services.access-control.endpoint.validate.allow-list.0=bars-performance-tests",
    "-Dmicroservice.services.access-control.endpoint.metadata.enabled=true",
    "-Dmicroservice.services.access-control.endpoint.metadata.allow-list.0=bars-performance-tests",
    "-Dmicroservice.services.modcheck.useLocal=true",
    "-Dfeatures.useDownstreamCOPStub=true"
  ],
  "BANK_ACCOUNT_REPUTATION_THIRD_PARTY_CACHE": [
    "-Dcontrollers.confidenceLevel.uk.gov.hmrc.bankaccountreputationthirdpartycache.controllers.CacheController.needsLogging=true"
  ],
  "BANK_ACCOUNT_VERIFICATION_FRONTEND": [
    "-Dmicroservice.hosts.allowList.1=localhost",
    "-Dauditing.enabled=false",
    "-Dmicroservice.services.access-control.enabled=true",
    "-Dmicroservice.services.access-control.allow-list.0=bars-performance-tests"
  ],
  "BANK_ACCOUNT_REPUTATION_FRONTEND": [
    "-Dauditing.enabled=false"
  ]
}'

curl -X POST http://localhost:9871/refresh/cache/eiscd
curl -X POST http://localhost:9871/refresh/cache/modcheck