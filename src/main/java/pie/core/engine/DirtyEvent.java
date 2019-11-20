/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pie.core.engine;

/**
 *
 * @author DELL
 */
public class DirtyEvent extends Event{
    
    private String message;
    private Throwable throwable;
    private String code;
    
    public DirtyEvent(String name){
        super(name);
    }

    public DirtyEvent(String name,String message, Throwable throwable, String code) {
        super(name);
        this.message = message;
        this.throwable = throwable;
        this.code = code;
    }

    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
    
    
    
    
}
