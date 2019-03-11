package cyl.mybatis.generator.mapper;

import cyl.mybatis.generator.model.Test;
import java.util.List;

public interface TestMapper {
    /**
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @mbg.generated
     */
    int insert(Test record);

    /**
     * @mbg.generated
     */
    Test selectByPrimaryKey(Long id);

    /**
     * @mbg.generated
     */
    List<Test> selectAll();

    /**
     * @mbg.generated
     */
    int updateByPrimaryKey(Test record);
}