package math;

import core.EntryManager;
import core.ExerciseCategory;
import core.LogEntry.EntryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

public class TestStatistics {

    static int minute = 60;
    static int hour = minute * 60;
    static String date = LocalDate.now().minusYears(1)
            + "-" + LocalDate.now();

    private EntryManager genValidEntryManager() {
        return new EntryManager();
    }

    private EntryBuilder genValidEntryBuilder(
            Duration duration,
            Double distance,
            ExerciseCategory exerciseCategory,
            Integer feeling) {

        String title = "Test";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);

        return new EntryBuilder(
                title, date, duration, exerciseCategory, feeling)
                .comment(comment)
                .distance(distance);
    }

    @Test
    public void testGetCount() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder = genValidEntryBuilder(
                Duration.ofSeconds(hour),
                10.0, ExerciseCategory.RUNNING, 1);

        String id = manager.addEntry(builder.build());
        Assertions.assertEquals(1,
                Statistics.getCount(manager, "RUNNING", date));
        manager.addEntry(builder.build());
        Assertions.assertEquals(2,
                Statistics.getCount(manager, "RUNNING", date));
        manager.removeEntry(id);
        Assertions.assertEquals(1,
                Statistics.getCount(manager, "RUNNING", date));
    }

    @Test
    public void testListFilteredByDates() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 = genValidEntryBuilder(
                Duration.ofSeconds(hour),
                10.0, ExerciseCategory.RUNNING, 1);

        manager.addEntry(builder1.build());
        Assertions.assertEquals(1,
                Statistics.getCount(manager, "RUNNING", date));
        manager.addEntry(builder1.build());

        Assertions.assertEquals(0, Statistics.getCount(
                manager, "RUNNING",
                LocalDate.now() + "-" + LocalDate.now()));

        EntryBuilder builder2 = genValidEntryBuilder(
                Duration.ofSeconds(hour),
                10.0, ExerciseCategory.SWIMMING, 1);
        manager.addEntry(builder2.build());

        Assertions.assertEquals(0, Statistics.getCount(manager, "SWIMMING",
                LocalDate.now() + "-" + LocalDate.now()));

        Assertions.assertEquals(2,
                Statistics.getCount(manager, "RUNNING", date));
    }

    @Test
    public void testGetTotalDuration() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 =
                genValidEntryBuilder(Duration.ofSeconds(hour), 10.0,
                        ExerciseCategory.RUNNING, 1);
        EntryBuilder builder2 =
                genValidEntryBuilder(Duration.ofSeconds(2L * hour), 10.0,
                        ExerciseCategory.RUNNING, 1);
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        //Compares with 3 hours which equals 10800 seconds. 
        Assertions.assertEquals(10800,
                Statistics.getTotalDuration(manager, "RUNNING", date));
    }

    @Test
    public void testGetAverageDuration() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 =
                genValidEntryBuilder(Duration.ofSeconds(hour), 10.0,
                        ExerciseCategory.RUNNING, 1);
        EntryBuilder builder2 =
                genValidEntryBuilder(Duration.ofSeconds(2L * hour), 10.0,
                        ExerciseCategory.RUNNING, 1);
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        //Compares with 1.5 hours which is equal to  
        Assertions.assertEquals(5400,
                Statistics.getAverageDuration(manager, "RUNNING", date));
    }

    @Test
    public void testGetAverageSpeed() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 =
                genValidEntryBuilder(Duration.ofSeconds(hour), 5.0,
                        ExerciseCategory.RUNNING, 1);
        EntryBuilder builder2 =
                genValidEntryBuilder(Duration.ofSeconds(2L * hour), 10.0,
                        ExerciseCategory.RUNNING, 1);
        EntryBuilder builder3 =
                genValidEntryBuilder(Duration.ofSeconds(3L * hour), 15.0,
                        ExerciseCategory.STRENGTH, 1);
        EntryBuilder builder4 =
                genValidEntryBuilder(Duration.ofSeconds(3L * hour), 40.0,
                        ExerciseCategory.CYCLING, 1);
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        manager.addEntry(builder3.build());
        manager.addEntry(builder4.build());

        Assertions.assertEquals(12.0,
                Statistics.getAverageSpeed(manager, "RUNNING", date));
        Assertions.assertEquals(4.5,
                Statistics.getAverageSpeed(manager, "CYCLING", date));
    }

    @Test
    public void testGetAverageFeeling() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 =
                genValidEntryBuilder(Duration.ofSeconds(hour), 10.0,
                        ExerciseCategory.RUNNING, 1);
        EntryBuilder builder2 =
                genValidEntryBuilder(Duration.ofSeconds(hour), 10.0,
                        ExerciseCategory.RUNNING, 10);
        EntryBuilder builder3 =
                genValidEntryBuilder(Duration.ofSeconds(hour), 10.0,
                        ExerciseCategory.RUNNING, 7);

        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        manager.addEntry(builder3.build());

        Assertions.assertEquals(6,
                Statistics.getAverageFeeling(manager, "RUNNING", date));
    }

    @Test
    public void testGetMaximumHr() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 =
                genValidEntryBuilder(Duration.ofSeconds(hour), 10.0,
                        ExerciseCategory.RUNNING, 1);
        EntryBuilder builder2 =
                genValidEntryBuilder(Duration.ofSeconds(hour), 10.0,
                        ExerciseCategory.RUNNING, 10);
        EntryBuilder builder3 =
                genValidEntryBuilder(Duration.ofSeconds(hour), 10.0,
                        ExerciseCategory.RUNNING, 7);

        builder1.maxHeartRate(130);
        builder2.maxHeartRate(200);
        builder3.maxHeartRate(145);

        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        manager.addEntry(builder3.build());

        Assertions.assertEquals(200,
                Statistics.getMaximumHr(manager, "RUNNING", date));
    }
}
