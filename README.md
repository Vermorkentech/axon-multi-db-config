# axon-multi-db-config

A small sample application configured to use two different PostgreSQL instances for two different projections. The main goal of this example is to illustrate the required configuration to have a tokenstore for each database.

## Running

A docker-compose.yml file contains the required services needed by this application; Axon Server and two PostgreSQL instances. After running `docker compose up`, this apllication can be started normally.
