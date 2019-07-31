/**
  * Copyright 2019 bejson.com 
  */
package com.bean.kafka;

/**
 * Auto-generated: 2019-04-01 17:20:45
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Offsets {

    private String group;
    private String topic;
    private int partition;
    private long offset;
    private long logSize;
    private String owner;
    private long creation;
    private long modified;
    public void setGroup(String group) {
         this.group = group;
     }
     public String getGroup() {
         return group;
     }

    public void setTopic(String topic) {
         this.topic = topic;
     }
     public String getTopic() {
         return topic;
     }

    public void setPartition(int partition) {
         this.partition = partition;
     }
     public int getPartition() {
         return partition;
     }

    public void setOffset(long offset) {
         this.offset = offset;
     }
     public long getOffset() {
         return offset;
     }

    public void setLogSize(long logSize) {
         this.logSize = logSize;
     }
     public long getLogSize() {
         return logSize;
     }

    public void setOwner(String owner) {
         this.owner = owner;
     }
     public String getOwner() {
         return owner;
     }

    public void setCreation(long creation) {
         this.creation = creation;
     }
     public long getCreation() {
         return creation;
     }

    public void setModified(long modified) {
         this.modified = modified;
     }
     public long getModified() {
         return modified;
     }

}