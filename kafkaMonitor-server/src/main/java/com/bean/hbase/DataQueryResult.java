package com.bean.hbase;

import java.util.List;

/**
 * Created by Administrator on 2016-12-20.
 */
public class DataQueryResult extends Result{
    private final boolean isEnd;
    private final List<DataRecord> records;

    private DataQueryResult(boolean isSuccess, int errorCode, boolean isEnd, List<DataRecord> records, String errormsg) {
        super(isSuccess, errorCode,errormsg);
        this.isEnd = isEnd;
        this.records = records;
    }

    public boolean isEnd() {
        return this.isEnd;
    }

    public List<DataRecord> getRecords() {
        return this.records;
    }

    public static class Builder extends Result.Builder {
        private boolean isEnd;
        private List<DataRecord> records;

        public Builder() {
        }

        public DataQueryResult.Builder setIsEnd(boolean isEnd) {
            this.isEnd = isEnd;
            return this;
        }

        public DataQueryResult.Builder setRecords(List<DataRecord> records) {
            this.records = records;
            return this;
        }

        public DataQueryResult build() {
            return new DataQueryResult(this.success, this.errorCode, this.isEnd, this.records,this.errormsg);
        }
    }
}