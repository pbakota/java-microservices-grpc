
.PHONY: copy-all copy-orders copy-payments copy-stock copy-delivery copy-gw
all:

copy-all:
	make copy-orders && make copy-payments && make copy-stock && make copy-delivery && make copy-gw

copy-orders:
	rsync -a sources/orders/target/orders-0.0.1-SNAPSHOT.jar root@playbox:/root/java-microservices-grpc-docker/orders/
copy-payments:
	rsync -a sources/payments/target/payments-0.0.1-SNAPSHOT.jar root@playbox:/root/java-microservices-grpc-docker/payments/
copy-stock:
	rsync -a sources/stock/target/stock-0.0.1-SNAPSHOT.jar root@playbox:/root/java-microservices-grpc-docker/stock/
copy-delivery:
	rsync -a sources/delivery/target/delivery-0.0.1-SNAPSHOT.jar root@playbox:/root/java-microservices-grpc-docker/delivery/
copy-gw:
	rsync -a sources/gw/target/gw-0.0.1-SNAPSHOT.jar root@playbox:/root/java-microservices-grpc-docker/gw/
