package pl.jwr.stat.services.impl;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.jwr.stat.services.StatisticsService;
import pl.jwr.stat.services.dto.DataTable;
import pl.jwr.stat.services.dto.Result;
import pl.jwr.stat.utils.RUtilities;

/**
 * Created by mjawor on 2017-04-21.
 */
@Singleton
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsService.class);
    private static final String KEY = "key";
    private static final String VAL = "val";
    private static final String DATA = "data";
    private static final String TEST_VECTOR_NAME = "test";
    private static final String RESULT_VECTOR_NAME = "returnedResult";
    @Inject
    private RUtilities rUtilities;

    @Override
    public Result getSimpleOperationResult() {
        long start = System.currentTimeMillis();
        RCaller rCaller = RCaller.create();
        RCode rCode = RCode.create();
        rCode.addRCode("a <- 2 + 4");
        rCaller.setRCode(rCode);
        rCaller.runAndReturnResult("a");

        int[] resultArray = rCaller.getParser().getAsIntArray("a");
        LOGGER.info("getSimpleOperationResult time: " + (System.currentTimeMillis() - start));
        return getResult(Integer.toString(resultArray[0]));
    }

    @Override
    public Result getCorrelationResult(DataTable data) {
        long start = System.currentTimeMillis();
        RCaller rCaller = RCaller.create();
        RCode rCode = RCode.create();

        rCode.addRCode(rUtilities.getNewVector(KEY, data.getKeyColumn()));
        rCode.addRCode(rUtilities.getNewVector(VAL, data.getValueColumn()));
        rCode.addRCode(rUtilities.getNewTableVector(DATA, KEY, VAL));
        rCode.addRCode(rUtilities.getChiTest(DATA, TEST_VECTOR_NAME));
        rCode.addRCode(rUtilities.getTestPValue(TEST_VECTOR_NAME, RESULT_VECTOR_NAME));
        rCaller.setRCode(rCode);
        rCaller.runAndReturnResult(RESULT_VECTOR_NAME);

        String[] resultArray = rCaller.getParser().getAsStringArray(RESULT_VECTOR_NAME);
        LOGGER.info("getCorrelationResult time: " + (System.currentTimeMillis() - start));
        return getResult(resultArray[0]);
    }

    @Override
    public Result getScriptSimpleOperationResult() {
        long start = System.currentTimeMillis();
        RCaller rCaller = RCaller.create();
        RCode rCode = RCode.create();

        rCode.addRCode(rUtilities.scriptLoader("src/test/resources/test_script.R"));
        rCaller.setRCode(rCode);
        rCaller.runAndReturnResult("a");

        int[] resultArray = rCaller.getParser().getAsIntArray("a");
        LOGGER.info("getScriptSimpleOperationResult time: " + (System.currentTimeMillis() - start));
        return getResult(Integer.toString(resultArray[0]));
    }

    @Override
    public Result getResultOfCsvDataAnalyze() {
        long start = System.currentTimeMillis();
        RCaller rCaller = RCaller.create();
        RCode rCode = RCode.create();

        rCode.addRCode(rUtilities.scriptLoader("src/test/resources/CSVFileReaderScript.R"));
        rCaller.setRCode(rCode);
        rCaller.runAndReturnResult(RESULT_VECTOR_NAME);

        String[] resultArray = rCaller.getParser().getAsStringArray(RESULT_VECTOR_NAME);
        LOGGER.info("getResultOfCsvDataAnalyze time: " + (System.currentTimeMillis() - start));
        return getResult(resultArray[0]);
    }

    private Result getResult(String result1) {
        Result result = new Result();
        result.setResult(result1);
        return result;
    }


}
