package cyl.mybatis.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App{
    public static void main(String[] args) {
        try {
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;

            String path = App.class.getClassLoader().getResource("generatorConfig.xml").getPath();
            File configFile = new File(path);
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            VerboseProgressCallback verboseProgressCallback = new VerboseProgressCallback();
            myBatisGenerator.generate(verboseProgressCallback);
            System.out.println("生成成功");
        }catch (Exception e){
            System.out.println("生成代码异常:"+e.getMessage());
        }
    }
}
