package io.camunda.connector.inbound.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Data model of an event consumed by inbound Connector (e.g. originating from an external system)
 */
public class MongoDBCollectionWatchSubscriptionEvent {

  private final static Logger LOG = LoggerFactory.getLogger(MongoDBCollectionWatchSubscriptionEvent.class);

  private final String operationType;
  private final Object before;
  private final Object after;


  public MongoDBCollectionWatchSubscriptionEvent(String operationType, Object before, Object after) throws JsonProcessingException {
     this.operationType =operationType;
      ObjectMapper mapper = new ObjectMapper();
      this.before = before;
      this.after = after;
      //this.payload = payload;
  }

  public String getOperationType() {return operationType;};
  public Object getBefore() {
    return before;
  }
  public Object getAfter() {
    return after;
  }

    @Override
  public boolean equals(Object o) {
    LOG.info("checking...");
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    // TODO need to check this
    MongoDBCollectionWatchSubscriptionEvent that = (MongoDBCollectionWatchSubscriptionEvent) o;
    return Objects.equals(before, that.before) && Objects.equals(after, that.after);
  }

  @Override
  public String toString() {
    return "MongoDBCollectionWatchSubscriptionEvent{" +
            "operationType='" + operationType + '\'' +
            "before='" + before + '\'' +
            "after='" + after + '\'' +
        '}';
  }
}
