package core;

import core.LogEntry.EntryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;

public class TestLogEntry {

    static int minute = 60;
    static int hour = minute * 60;

    private EntryBuilder genValid() {
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);
        int feeling = 1;
        double distance = 1;
        Integer maxHeartRate = 80;

        ExerciseCategory exerciseCategory = ExerciseCategory.STRENGTH;
        Subcategory subcategory = StrengthSubCategory.PULL;

        return new EntryBuilder(title, date, duration, exerciseCategory,
                feeling).comment(comment)
                .exerciseSubCategory(subcategory).distance(distance)
                .maxHeartRate(maxHeartRate);
    }

    @Test
    // Above test proves it should work, considering it is nearly identical with the
    // test in LogEntry (line 271)
    public void testLogEntry() {
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);

        int feeling = 3;
        double distance = 7.0;
        Integer maxHeartRate = 183;

        ExerciseCategory exerciseCategory = ExerciseCategory.RUNNING;
        Subcategory subcategory = CardioSubCategory.HIGHINTENSITY;

        EntryBuilder builder =
                new EntryBuilder(title, date, duration, exerciseCategory,
                        feeling).comment(comment)
                        .exerciseSubCategory(subcategory).distance(distance)
                        .maxHeartRate(maxHeartRate);
        LogEntry entry = new LogEntry(builder);

        Assertions.assertEquals(title, entry.getTitle());
        Assertions.assertEquals(comment, entry.getComment());
        Assertions.assertEquals(date, entry.getDate());
        Assertions.assertEquals(duration, entry.getDuration());
        Assertions.assertEquals(feeling, entry.getFeeling());
        Assertions.assertEquals(distance, entry.getDistance());
        Assertions.assertEquals(maxHeartRate, entry.getMaxHeartRate());
    }

    @Test
    public void teststringToSubcategory() {
        for (CardioSubCategory subcategory : CardioSubCategory.values()) {
            Assertions.assertEquals(subcategory,
                    LogEntry.stringToSubcategory(subcategory.toString()));
        }
        for (StrengthSubCategory subcategory : StrengthSubCategory.values()) {
            Assertions.assertEquals(subcategory,
                    LogEntry.stringToSubcategory(subcategory.toString()));
        }
    }

    @Test
    public void testToHash() {
        EntryBuilder builder = genValid();
        LogEntry entry = new LogEntry(builder);
        HashMap<String, String> hash = entry.toHash();
        Assertions.assertEquals(entry.getTitle(), hash.get("title"));
        Assertions.assertEquals(entry.getComment(), hash.get("comment"));
        Assertions.assertEquals(entry.getDate().toString(), hash.get("date"));
        Assertions.assertEquals(String.valueOf(entry.getDuration().toSeconds()),
                hash.get("duration"));
        Assertions.assertEquals(entry.getFeeling(),
                Integer.parseInt(hash.get("feeling")));
        Assertions.assertEquals(entry.getDistance(),
                Double.parseDouble(hash.get("distance")));
        Assertions.assertEquals(entry.getMaxHeartRate(),
                Integer.parseInt(hash.get("maxHeartRate")));
        Assertions.assertEquals(entry.getExerciseCategory().toString(),
                hash.get("exerciseCategory"));
        Assertions.assertEquals(entry.getExerciseSubCategory().toString(),
                hash.get("exerciseSubCategory"));
    }

    @Test
    public void testFromHash() {
        EntryBuilder builder = genValid();
        LogEntry entry = new LogEntry(builder);
        HashMap<String, String> hash = entry.toHash();
        LogEntry entry2 = LogEntry.fromHash(hash);
        Assertions.assertEquals(entry.getTitle(), entry2.getTitle());
        Assertions.assertEquals(entry.getComment(), entry2.getComment());
        Assertions.assertEquals(entry.getDate(), entry2.getDate());
        Assertions.assertEquals(entry.getDuration(), entry2.getDuration());
        Assertions.assertEquals(entry.getFeeling(), entry2.getFeeling());
        Assertions.assertEquals(entry.getDistance(), entry2.getDistance());
        Assertions.assertEquals(entry.getMaxHeartRate(),
                entry2.getMaxHeartRate());
        Assertions.assertEquals(entry.getExerciseCategory(),
                entry2.getExerciseCategory());
        Assertions.assertEquals(entry.getExerciseSubCategory(),
                entry2.getExerciseSubCategory());
    }

    @Test
    public void illegalId() {
        LogEntry entry = new EntryBuilder("Test", LocalDate.now().minusDays(1),
                Duration.ofSeconds(hour),
                ExerciseCategory.STRENGTH, 1).build();
        entry.setId("0");
        Assertions.assertThrows(IllegalStateException.class,
                () -> entry.setId("0"));
    }

    @Test
    public void illegalTitle() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new EntryBuilder("", LocalDate.now().minusDays(1),
                        Duration.ofSeconds(hour), ExerciseCategory.STRENGTH,
                        1).build());
    }

    @Test
    public void illegalDate() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new EntryBuilder("Test",
                        LocalDate.now().plusDays(1), Duration.ofSeconds(hour),
                        ExerciseCategory.STRENGTH, 1).build());
    }

    @Test
    public void illegalDuration() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new EntryBuilder("Test",
                        LocalDate.now().minusDays(1), Duration.ofSeconds(-hour),
                        ExerciseCategory.STRENGTH, 1).build());
    }

    @Test
    public void illegalFeeling() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new EntryBuilder("Test",
                        LocalDate.now().minusDays(1), Duration.ofSeconds(hour),
                        ExerciseCategory.STRENGTH, 11).build());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new EntryBuilder("Test",
                        LocalDate.now().minusDays(1), Duration.ofSeconds(hour),
                        ExerciseCategory.STRENGTH, 0).build());
    }

    @Test
    public void illegalDistance() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> genValid().distance(-10.0).build());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> genValid().distance(0.0).build());
    }

    @Test
    public void illegalMaxHeartRate() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> genValid().maxHeartRate(10).build());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> genValid().maxHeartRate(700).build());
    }
}
