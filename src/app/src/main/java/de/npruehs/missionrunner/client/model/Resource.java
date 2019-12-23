package de.npruehs.missionrunner.client.model;

public class Resource<T>  {
    private T data;
    private ResourceStatus status;
    private String error;

    public Resource() {
    }

    private Resource(T data, ResourceStatus status, String error) {
        this.data = data;
        this.status = status;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public ResourceStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public static <T> Resource newUnavailableResource(String error) {
        return new Resource<T>(null, ResourceStatus.UNAVAILABLE, error);
    }

    public static <T> Resource newPendingResource() {
        return newPendingResource(null);
    }

    public static <T> Resource newPendingResource(T data) {
        return new Resource<T>(data, ResourceStatus.PENDING, null);
    }

    public static <T> Resource newAvailableResource(T data) {
        return new Resource<T>(data, ResourceStatus.AVAILABLE, null);
    }
}
