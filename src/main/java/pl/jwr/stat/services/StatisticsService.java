package pl.jwr.stat.services;

import com.google.inject.ImplementedBy;
import pl.jwr.stat.services.dto.DataTable;
import pl.jwr.stat.services.dto.Result;
import pl.jwr.stat.services.impl.StatisticsServiceImpl;

/**
 * Created by mjawor on 2017-04-21.
 */
@ImplementedBy(StatisticsServiceImpl.class)
public interface StatisticsService {

    Result getSimpleOperationResult();

    Result getCorrelationResult(DataTable data);

    Result getScriptSimpleOperationResult();

    Result getResultOfCsvDataAnalyze();
}
