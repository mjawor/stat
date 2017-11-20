package pl.jwr.stat.utils;

import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by mjawor on 2017-04-28.
 */
@Singleton
public class RUtilities {

    public String getNewVector(String vectorName, List<String> list) {
        String vector = vectorName + " <- c(";
        vector += StringUtils.join(list, ",") + ")";
        return vector;
    }

    public String getNewTableVector(String vectorName, String keyVectorName, String valueColumnName) {
        return vectorName + " <- table(" + keyVectorName + "," + valueColumnName + ")";
    }

    public String getChiTest(String tableVectorName, String testVectorName) {
        return "if(is.table(" + tableVectorName + ")){" + testVectorName + " <- chisq.test(" + tableVectorName + ")}else{print(\"Incorrect operation!\")}";
    }

    public String getTestPValue(String testVectorName, String resultVectorName) {
        return "if(is.element(" + testVectorName + "$p.value," + testVectorName + ")){" + resultVectorName + " <- " + testVectorName + "$p.value}else{print(\"Incorrect operation!\")}";
    }

    public String scriptLoader(String path) {
        return "source(\"" + path + "\"); ";
    }

}
