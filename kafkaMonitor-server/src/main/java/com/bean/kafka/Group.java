/**
  * Copyright 2019 bejson.com 
  */
package com.bean.kafka;
import java.util.List;

/**
 * Auto-generated: 2019-04-01 17:20:45
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Group {

    private List<Brokers> brokers;
    private List<Offsets> offsets;
    public void setBrokers(List<Brokers> brokers) {
         this.brokers = brokers;
     }
     public List<Brokers> getBrokers() {
         return brokers;
     }

    public void setOffsets(List<Offsets> offsets) {
         this.offsets = offsets;
     }
     public List<Offsets> getOffsets() {
         return offsets;
     }

}