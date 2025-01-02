package com.piggymetrics.statistics.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.piggymetrics.statistics.domain.timeseries.DataPoint;
import com.piggymetrics.statistics.domain.timeseries.DataPointId;
import com.piggymetrics.statistics.domain.timeseries.ItemMetric;
import com.piggymetrics.statistics.domain.timeseries.StatisticMetric;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ActiveProfiles("db")
@SpringBootTest(properties = "de.flapdoodle.mongodb.embedded.version=5.0.5")
public class DataPointRepositoryTest {

    @Autowired
    private DataPointRepository repository;

    @Test
    public void shouldSaveDataPoint() {

        ItemMetric salary = new ItemMetric("salary", new BigDecimal(20_000));

        ItemMetric grocery = new ItemMetric("grocery", new BigDecimal(1_000));
        ItemMetric vacation = new ItemMetric("vacation", new BigDecimal(2_000));

        DataPointId pointId = new DataPointId("test-account", new Date(0));

        DataPoint point = DataPoint.builder()
                .id(pointId)
                .incomes(Sets.newHashSet(salary))
                .expenses(Sets.newHashSet(grocery, vacation))
                .statistics(ImmutableMap.of(
                        StatisticMetric.SAVING_AMOUNT, new BigDecimal(400_000),
                        StatisticMetric.INCOMES_AMOUNT, new BigDecimal(20_000),
                        StatisticMetric.EXPENSES_AMOUNT, new BigDecimal(3_000)
                ))
                .build();

        repository.save(point);

        List<DataPoint> points = repository.findByIdAccount(pointId.getAccount());
        assertEquals(1, points.size());
        assertEquals(pointId.getDate(), points.get(0).getId().getDate());
        assertEquals(point.getStatistics().size(), points.get(0).getStatistics().size());
        assertEquals(point.getIncomes().size(), points.get(0).getIncomes().size());
        assertEquals(point.getExpenses().size(), points.get(0).getExpenses().size());
    }

    @Test
    public void shouldRewriteDataPointWithinADay() {

        final BigDecimal earlyAmount = new BigDecimal(100);
        final BigDecimal lateAmount = new BigDecimal(200);

        DataPointId pointId = new DataPointId("test-account", new Date(0));

        DataPoint earlier = DataPoint.builder()
                .id(pointId)
                .statistics(ImmutableMap.of(
                        StatisticMetric.SAVING_AMOUNT, earlyAmount
                ))
                .build();

        repository.save(earlier);

        DataPoint later = DataPoint.builder()
                .id(pointId)
                .statistics(ImmutableMap.of(
                        StatisticMetric.SAVING_AMOUNT, lateAmount
                ))
                .build();

        repository.save(later);

        List<DataPoint> points = repository.findByIdAccount(pointId.getAccount());

        assertEquals(1, points.size());
        assertEquals(lateAmount, points.get(0).getStatistics().get(StatisticMetric.SAVING_AMOUNT));
    }
}
