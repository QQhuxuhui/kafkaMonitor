package com.bean.hbase;

import java.util.Arrays;

/**
 * Created by Administrator on 2016-12-20.
 */
public class DataRecord {
    private final long time;
    private final Integer cmdId;
    private final byte[] body;

    public DataRecord(long time, Integer cmdId, byte[] body) {
        this.time = time;
        this.cmdId = cmdId;
        this.body = body;
    }

    public long getTime() {
        return this.time;
    }

    public Integer getCmdId() {
        return this.cmdId;
    }

    public byte[] getBody() {
        return this.body;
    }

    public String toString() {
        return "DataRecord [time=" + this.time + ", cmdId=" + this.cmdId + ", body=" + Arrays.toString(this.body) + "]";
    }
}
