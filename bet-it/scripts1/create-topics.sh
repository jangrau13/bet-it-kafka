echo "Waiting for Kafka to come online..."
cub kafka-ready -b kafka:9092 1 20
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.contract.requested --replication-factor 1 --partitions 1
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.game.valid-for-contract-result --replication-factor 1 --partitions 1
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.transaction-result --replication-factor 1 --partitions 1
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.freeze-result --replication-factor 1 --partitions 1
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.bet.requested --replication-factor 1 --partitions 1
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.add-user --replication-factor 1 --partitions 1
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.game.ended --replication-factor 1 --partitions 1
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.twofa --replication-factor 1 --partitions 1
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.user.check-result --replication-factor 1 --partitions 1
kafka-topics --create --bootstrap-server kafka:9092 --topic camunda.TwoFactorSuccessEvent --replication-factor 1 --partitions 1

sleep infinity