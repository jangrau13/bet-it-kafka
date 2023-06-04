# API2Kafka
This application downloads data from an online API and publishes them to the bet-it kafka broker.

The service exposes two routes:

- /start - Sending a request to this endpoint will trigger a scheduled job that will download match fixtures and live match data from the online API and publish them to Kafka.
- /stop - Sending a request to this endpoint will stop the scheduled job.

Note: The online API is https://live-score-api.com/. There are hardcoded credentials to use the api in the client but the service is fairly unreliable, and they get cancelled frequently. You can easily make your own account, start a trial and generate credentials to use the API. The data types also change some times and de-serialization fails.
