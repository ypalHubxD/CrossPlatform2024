package bulletinBoardService;

import javax.swing.*;
import java.lang.reflect.*;

public class EDTInvocationHandler implements InvocationHandler {
    private final Object ui;
    private Object invocationResult = null;
    public EDTInvocationHandler(Object ui) {
        this.ui = ui;
    }
    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        if (SwingUtilities.isEventDispatchThread()) {
            return method.invoke(ui, args);
        } else {
            Runnable task = () -> {
                try {
                    invocationResult = method.invoke(ui, args);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            };
            SwingUtilities.invokeAndWait(task);
            return invocationResult;
        }
    }
}
