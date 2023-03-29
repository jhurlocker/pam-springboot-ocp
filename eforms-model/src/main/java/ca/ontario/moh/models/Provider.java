package ca.ontario.moh.models;

public class Provider {
    
    private String providerName = "";
    private boolean existingAgreement = false;
    private String endReasonCode = null;
    private String classCode = "";
    private boolean providerApproved = false;
    
    public boolean isProviderApproved() {
        return providerApproved;
    }
    public void setProviderApproved(boolean providerApproved) {
        this.providerApproved = providerApproved;
    }
    public String getProviderName() {
        return providerName;
    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    public boolean isExistingAgreement() {
        return existingAgreement;
    }
    public void setExistingAgreement(boolean existingAgreement) {
        this.existingAgreement = existingAgreement;
    }
    public String getEndReasonCode() {
        return endReasonCode;
    }
    public void setEndReasonCode(String endReasonCode) {
        this.endReasonCode = endReasonCode;
    }
    public String getClassCode() {
        return classCode;
    }
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }


}
