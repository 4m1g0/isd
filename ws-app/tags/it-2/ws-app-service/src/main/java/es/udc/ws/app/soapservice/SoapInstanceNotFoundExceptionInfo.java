package es.udc.ws.app.soapservice;

public class SoapInstanceNotFoundExceptionInfo {

    private Object instanceId;
    private String instanceType;

    public SoapInstanceNotFoundExceptionInfo() {
    }    
    
    public SoapInstanceNotFoundExceptionInfo(Object instanceId, 
                                             String instanceType) {
        this.instanceId = instanceId;
        this.instanceType = instanceType;
    }

    public Object getInstanceId() {
        return instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceId(Object instanceId) {
        this.instanceId = instanceId;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }
        
}
