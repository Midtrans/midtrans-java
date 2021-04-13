## CHANGELOG MIDTRANS JAVA LIBRARY

## v3.0.0 (April 13, 2021)

Feature:
- new static method CoreApi, SnapApi, IrisApi, TransactionApi
- can change, TimeUnit for http connection settings
- can set global config via Midtrans class
- can add custom headers with config class
- validation for server-key empty, contain whitespace

Improvement:
- handle okhttp raw error/exception via MidtransError exception class

Notable changes:
- Migrate from retrofit to okhttp as http client
- Migrate repo host from jcenter/bintray to maven central

## v2.1.1 (April 27, 2020)

Feature:

- API Iris disbursement
- Payment BIN API
- Set Connection Pool & Keep alive connection
- Set header X-Idempotency-key for Iris and Idempotency-Key for Payment
- Set `X-Append-Notification`: to add new notification url(s) alongside the settings on dashboard
- Set `X-Override-Notification`: to use new notification url(s) disregarding the settings on dashboard
- Library version on `User-Agent` header

Improvement:

- Change logic for setup headers
- Remove versioning path from base URL, move to `String API_VERSION` in CoreAPI, SnapAPI interface class
- Improve conditional logic for HttpLoggingInterceptors. Now all debugging response will be handled only from HttpLoggingInterceptors Which can show logs request and response information

Bugs fix:
- Failure/error response from Midtrans api result in empty json.

## v1.1.1 (Feb 3, 2020)

Improvement:

HttpLoggingInterceptor: Which can show logs request and response information. This feature This feature is disabled by default on production mode.

## v1.1.0 (Nov 27, 2019)

Feature:

- Allow HTTP Proxy config
- Allow set connectionTimeout
- Allow set readTimeout
- Allow set writeTimeout

## v1.0.0 (Sep 25, 2019) New release

Feature:
- CoreAPI basic functionality
- Snap API basic functionality
- Include Spring project example
- Basic usage sample on Readme
- Release via Bintray maven

