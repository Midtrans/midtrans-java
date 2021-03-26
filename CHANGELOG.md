## CHANGELOG MIDTRANS JAVA LIBRARY

## v3.1.1 (March 26, 2021)

Feature:
- Release with static method v2 `com.midtrans.v2`
    - Can set a global config on v2
- Can add custom headers
- Can catch response http from MidtransError class

Improvement:
- No longer using Retrofit, but still use OkHttp as http client


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

