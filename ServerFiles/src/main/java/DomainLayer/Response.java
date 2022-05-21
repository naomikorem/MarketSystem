package DomainLayer;

public class Response<T> {
    private T object;
    private String errorMessage;

    public Response() {
        this.object = null;
        this.errorMessage = null;
    }

    public Response(T object) {
        this.object = object;
        this.errorMessage = null;
    }

    public Response(String errorMessage) {
        this.object = null;
        this.errorMessage = errorMessage == null ? "Action Failed" : errorMessage;
    }

    public Response(T object, String errorMessage) {
        this.object = object;
        this.errorMessage = errorMessage == null ? "Action Failed" : errorMessage;
    }

    public T getObject() {
        return object;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hadError() {
        return errorMessage != null;
    }
}
