package voldemort.server.protocol.admin;

import voldemort.annotations.jmx.JmxGetter;
import voldemort.annotations.jmx.JmxOperation;

/**
 * @author afeinberg
 */
public abstract class AsyncOperation implements Runnable {

    protected final AsyncOperationStatus status;

    public AsyncOperation(int id, String description) {
        this.status = new AsyncOperationStatus(id, description);
    }

    @JmxGetter(name = "asyncTaskStatus")
    public AsyncOperationStatus getStatus() {
        return status;
    }

    public void updateStatus(String msg) {
        status.setStatus(msg);
    }

    public void markComplete() {
        status.setComplete(true);

    }

    public void run() {
        updateStatus("started " + getStatus());
        try {
            operate();
        } catch(Exception e) {
            status.setException(e);
        }
        updateStatus("finished " + getStatus());
        markComplete();
    }

    abstract public void operate() throws Exception;

    @JmxOperation
    abstract public void stop();
}
