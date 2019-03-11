package cyl.mybatis.generator;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.PropertyRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 */
public class CylServicePlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }


    public CylServicePlugin() {
        super();
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }


    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable) {

        List<GeneratedJavaFile> answer = new ArrayList<>();
        CompilationUnit compilationUnit = buildServiceCompilationUnit(introspectedTable);
        CompilationUnit compilationUnitImpl = buildServiceImplCompilationUnit(introspectedTable);
        GeneratedJavaFile serviceFile = new GeneratedJavaFile(compilationUnit,
                context.getJavaModelGeneratorConfiguration()
                        .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        GeneratedJavaFile serviceImplFile = new GeneratedJavaFile(compilationUnitImpl,
                context.getJavaModelGeneratorConfiguration()
                        .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        answer.add(serviceFile);
        answer.add(serviceImplFile);

        return answer;

    }

    private CompilationUnit buildServiceCompilationUnit(IntrospectedTable introspectedTable){
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType interfaceType = getInterfaceType(introspectedTable);
        Interface service = new Interface(interfaceType);
        service.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(service);

        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        service.addImportedType(entityType);
        service.addImportedType(new FullyQualifiedJavaType(List.class.getName()));

        Method insertMethod = getInsertMethod(entityType);
        service.addMethod(insertMethod);
        Method updateMethod = getUpdateMethod(entityType);
        service.addMethod(updateMethod);
        Method deleteMethod = getDeleteMethod(entityType);
        service.addMethod(deleteMethod);
        Method queryByIdMethod = getQueryByIdMethod(entityType);
        service.addMethod(queryByIdMethod);
        return service;

    }


    private CompilationUnit buildServiceImplCompilationUnit(IntrospectedTable introspectedTable){
        CommentGenerator commentGenerator = context.getCommentGenerator();
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        FullyQualifiedJavaType interfaceType = getInterfaceType(introspectedTable);
        StringBuilder sb = new StringBuilder();
        sb.append(interfaceType.getFullyQualifiedName());
        sb.insert(sb.lastIndexOf("."),".impl");
        sb.append("Impl");

        FullyQualifiedJavaType serviceType = new FullyQualifiedJavaType(sb.toString());
        TopLevelClass service = new TopLevelClass(serviceType);
        service.setVisibility(JavaVisibility.PUBLIC);
        service.addSuperInterface(interfaceType);
        service.addAnnotation("@Service");
        service.addImportedType("org.springframework.stereotype.Service");
        service.addAnnotation("@Slf4j");
        service.addImportedType("lombok.extern.slf4j.Slf4j");

        service.addImportedType(entityType);
        service.addImportedType(new FullyQualifiedJavaType(List.class.getName()));
        service.addImportedType(interfaceType);

        commentGenerator.addJavaFileComment(service);

        //添加 mapper属性
        FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        String mapperAttr = lowerFirstCase(mapperType.getShortName());

        service.addImportedType(mapperType);

        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(mapperType);
        field.setName(mapperAttr);
        field.addAnnotation("@Resource");
        service.addImportedType("javax.annotation.Resource");
        service.addField(field);

        Method insertMethod = getInsertMethod(entityType);
        insertMethod.addAnnotation("@Override");
        insertMethod.setVisibility(JavaVisibility.PUBLIC);
        insertMethod.addBodyLine("return "+mapperAttr+"."+introspectedTable.getInsertSelectiveStatementId()+buildUseMapperMethodParams(insertMethod)+";");
        service.addMethod(insertMethod);
        Method updateMethod = getUpdateMethod(entityType);
        updateMethod.addAnnotation("@Override");
        updateMethod.setVisibility(JavaVisibility.PUBLIC);
        updateMethod.addBodyLine("return "+mapperAttr+"."+introspectedTable.getUpdateByPrimaryKeySelectiveStatementId()+buildUseMapperMethodParams(updateMethod)+";");
        service.addMethod(updateMethod);
        Method deleteMethod = getDeleteMethod(entityType);
        deleteMethod.addAnnotation("@Override");
        deleteMethod.setVisibility(JavaVisibility.PUBLIC);
        deleteMethod.addBodyLine("return "+mapperAttr+"."+introspectedTable.getDeleteByPrimaryKeyStatementId()+buildUseMapperMethodParams(deleteMethod)+";");
        service.addMethod(deleteMethod);
        Method queryByIdMethod = getQueryByIdMethod(entityType);
        queryByIdMethod.addAnnotation("@Override");
        queryByIdMethod.setVisibility(JavaVisibility.PUBLIC);
        queryByIdMethod.addBodyLine("return "+mapperAttr+"."+introspectedTable.getSelectByPrimaryKeyStatementId()+buildUseMapperMethodParams(queryByIdMethod)+";");
        service.addMethod(queryByIdMethod);

        return service;

    }

    private FullyQualifiedJavaType getInterfaceType(IntrospectedTable introspectedTable){
        String servicePackage = introspectedTable.getContext().getProperty("servicePackage");
        String domainObjectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        StringBuilder sb = new StringBuilder();
        sb.append(servicePackage);
        sb.append(".");
        sb.append(domainObjectName);
        sb.append("Service");

        return new FullyQualifiedJavaType(sb.toString());
    }

    private Method getInsertMethod(FullyQualifiedJavaType entityType){
        Method method = new Method();
        method.setName("insert"+entityType.getShortName());
        method.addParameter(new Parameter(entityType,lowerFirstCase(entityType.getShortName())));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        return method;
    }

    private Method getUpdateMethod(FullyQualifiedJavaType entityType){
        Method method = new Method();
        method.setName("update"+entityType.getShortName());
        method.addParameter(new Parameter(entityType,lowerFirstCase(entityType.getShortName())));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        return method;
    }

    private Method getDeleteMethod(FullyQualifiedJavaType entityType){
        Method method = new Method();
        method.setName("delete"+entityType.getShortName());
        method.addParameter(new Parameter(new FullyQualifiedJavaType(Long.class.getName()),"id"));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        return method;
    }


    private Method getQueryByIdMethod(FullyQualifiedJavaType entityType){
        Method method = new Method();
        method.setName("get"+entityType.getShortName()+"byId");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(Long.class.getName()),"id"));
        method.setReturnType(entityType);
        return method;
    }

    //首字母小写
    private String lowerFirstCase(String str){
        if(str != null && str.length() > 0){
            StringBuilder sb = new StringBuilder(str);
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
            str = sb.toString();
        }
        return str;
    }

    private String buildUseMapperMethodParams(Method method){
        List<Parameter> parameters = method.getParameters();
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if(parameters != null && parameters.size()!=0){
            parameters.stream().forEach(s -> sb.append(s.getName()).append(", "));
            sb.deleteCharAt(sb.lastIndexOf(", "));
        }
        sb.append(")");
        return sb.toString();
    }



}
