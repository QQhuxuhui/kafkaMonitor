package com.bean.hbase;

public class Result {
    private final boolean isSuccess;
    private final int errorCode;
    private final String errormsg;

    protected Result(boolean isSuccess, int errorCode, String errormsg) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.errormsg = errormsg;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrormsg() {
        return this.errormsg;
    }

    public static class Builder {
        protected boolean success;
        protected int errorCode;
        protected String errormsg;

        public Builder() {
        }

        public Builder(boolean success, int errorCode, String errormsg) {
            this.success = success;
            this.errorCode = errorCode;
            this.errormsg = errormsg;
        }

        public Result.Builder setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Result.Builder setErrorCode(int errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Result.Builder setErrormsg(String errormsg) {
            this.errormsg = errormsg;
            return this;
        }

        public Result build() {
            return new Result(this.success, this.errorCode, this.errormsg);
        }
    }
}
