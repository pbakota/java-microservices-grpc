package rs.lab.rpcdemo.common.utils;

public class RpcException extends RuntimeException {
    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }
}
