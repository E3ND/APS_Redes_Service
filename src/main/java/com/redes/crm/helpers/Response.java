package com.redes.crm.helpers;

public class Response {
    private boolean error;
    private Object message;

    public Response(boolean error, Object message) {
        this.error = error;
        this.message = message;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}