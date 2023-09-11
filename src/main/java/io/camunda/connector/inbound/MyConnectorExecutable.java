package io.camunda.connector.inbound;

import io.camunda.connector.api.annotation.InboundConnector;
import io.camunda.connector.api.inbound.InboundConnectorContext;
import io.camunda.connector.api.inbound.InboundConnectorExecutable;
import io.camunda.connector.inbound.subscription.MongoDBCollectionWatchSubscription;
import io.camunda.connector.inbound.subscription.MongoDBCollectionWatchSubscriptionEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@InboundConnector(name = "MONGODBINBOUNDCONNECTOR", type = "io.camunda:mongodbinbound:1")
public class MyConnectorExecutable implements InboundConnectorExecutable {

  private MongoDBCollectionWatchSubscription subscription;
  private InboundConnectorContext connectorContext;
  private ExecutorService executorService;
  public CompletableFuture<?> future;


  @Override
  public void activate(InboundConnectorContext connectorContext) {
    MyConnectorProperties props = connectorContext.bindProperties(MyConnectorProperties.class);
    this.connectorContext = connectorContext;
    this.executorService = Executors.newSingleThreadExecutor();

    this.future =
            CompletableFuture.runAsync(
                    () -> {
                      new MongoDBCollectionWatchSubscription(props.getMongoDBUrl(), props.getMongoDBDatabase(), props.getMongoDBCollection(), props.getMongoDBEvent(), this::onEvent);
                    },
                     this.executorService);

  }

  @Override
  public void deactivate() {
    subscription.stop();
  }

  private void onEvent(MongoDBCollectionWatchSubscriptionEvent rawEvent) {
    MyConnectorEvent connectorEvent = new MyConnectorEvent(rawEvent);
    connectorContext.correlate(connectorEvent);
  }
}
