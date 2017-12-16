package com.client.domain.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sdaskaliesku
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    public static final String RESULT_OK = "OK";
    public static final String RESULT_ERROR = "ERROR";

    @JsonProperty("Result")
    private String result;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Record")
    private Object record;

    @JsonProperty("Records")
    private Object records;

    public Response() {
        result = RESULT_OK;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRecord() {
        return record;
    }

    public void setRecord(Object record) {
        this.record = record;
    }

    public Object getRecords() {
        return records;
    }

    public void setRecords(Object records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "Response{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", record=" + record +
                ", records=" + records +
                '}';
    }
}
