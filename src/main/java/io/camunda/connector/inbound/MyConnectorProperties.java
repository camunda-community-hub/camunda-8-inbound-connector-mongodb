package io.camunda.connector.inbound;

/**
 * Configuration properties for inbound Connector
 */
public class MyConnectorProperties {

  private String mongoDBUrl;
  private String mongoDBDatabase;
  private String mongoDBCollection;
  private String mongoDBEvent;

  public String getMongoDBUrl() {
    return mongoDBUrl;
  }
  public String getMongoDBDatabase() {
    return mongoDBDatabase;
  }
  public String getMongoDBCollection() {
    return mongoDBCollection;
  }
  public String getMongoDBEvent() {
    return mongoDBEvent;
  }

  public void setMongoDBUrl(String mongoDBUrl) { this.mongoDBUrl = mongoDBUrl; }
  public void setMongoDBDatabase(String mongoDBDatabase) { this.mongoDBDatabase = mongoDBDatabase; }
  public void setMongoDBCollection(String mongoDBCollection) { this.mongoDBCollection = mongoDBCollection; }
  public void setMongoDBEvent(String mongoDBEvent) { this.mongoDBEvent = mongoDBEvent; }

  @Override
  public String toString() {
    return "MyConnectorProperties{" +
            "mongoDB URL='" + mongoDBUrl + '\'' +
            "mongoDB Database='" + mongoDBDatabase + '\'' +
            "mongoDB Collection='" + mongoDBCollection + '\'' +
            "mongoDB Event='" + mongoDBEvent + '\'' +
        '}';
  }
}
