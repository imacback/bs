package cn.aiyuedu.bs.dao.dto;

/**
 * Created by Thinkpad on 2014/12/29.
 */
public class BatchQueryDto {

    private Integer id;
    //标示主键ID是否按"!="在数据库中查询
    private Integer isNEId;
    private String contractId;
    private Integer providerId;
    private Integer isUse;

    private Integer start;
    private Integer limit;
    private Integer isDesc;

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsNEId() {
        return isNEId;
    }

    public void setIsNEId(Integer isNEId) {
        this.isNEId = isNEId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getIsDesc() {
        return isDesc;
    }

    public void setIsDesc(Integer isDesc) {
        this.isDesc = isDesc;
    }
}
