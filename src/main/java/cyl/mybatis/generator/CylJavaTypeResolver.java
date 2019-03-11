package cyl.mybatis.generator;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public class CylJavaTypeResolver extends JavaTypeResolverDefaultImpl {

    public CylJavaTypeResolver() {
        super();
        super.typeMap.put(-7, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Integer.class.getName())));
    }
}
