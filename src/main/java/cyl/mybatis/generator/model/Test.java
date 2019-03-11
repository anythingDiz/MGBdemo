package cyl.mybatis.generator.model;

import java.io.Serializable;
import java.util.Date;

public class Test implements Serializable {
    /**
     * 主键
     * @mbg.generated
     */
    private Long id;

    /**
     * 是否可用
     * @mbg.generated
     */
    private Integer isUse;

    /**
     * 创建时间
     * @mbg.generated
     */
    private Date createTime;

    /**
     * 更新时间
     * @mbg.generated
     */
    private Date update;

    /**
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @mbg.generated
     */
    public Integer getIsUse() {
        return isUse;
    }

    /**
     * @mbg.generated
     */
    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    /**
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @mbg.generated
     */
    public Date getUpdate() {
        return update;
    }

    /**
     * @mbg.generated
     */
    public void setUpdate(Date update) {
        this.update = update;
    }
}