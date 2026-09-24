package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        Assertions.assertEquals(List.of(List.of(singleTrainingSession)),
                new ArrayList<>(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).values()));
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        Assertions.assertEquals(List.of(List.of(mondayChildTrainingSession)),
                new ArrayList<>(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).values()));
        Assertions.assertEquals(List.of(List.of(thursdayChildTrainingSession), List.of(thursdayAdultTrainingSession)),
                new ArrayList<>(timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).values()));
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        Assertions.assertEquals(List.of(singleTrainingSession),
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).isEmpty());
    }

    @Test
    void testSeveralSessionsAtTheSameTime() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach firstCoach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach secondCoach = new Coach("НеВасильев", "НеНиколай", "НеСергеевич");
        TrainingSession first = new TrainingSession(group, firstCoach, DayOfWeek.FRIDAY, new TimeOfDay(9, 30));
        TrainingSession second = new TrainingSession(group, secondCoach, DayOfWeek.FRIDAY, new TimeOfDay(9, 30));
        timetable.addNewTrainingSession(first);
        timetable.addNewTrainingSession(second);

        Assertions.assertEquals(List.of(first, second),
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.FRIDAY, new TimeOfDay(9, 30)));
        Assertions.assertEquals(List.of(List.of(first, second)),
                new ArrayList<>(timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY).values()));
    }

    @Test
    void testAddingEarlierSessionKeepsSorting() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession late = new TrainingSession(group, coach, DayOfWeek.SUNDAY, new TimeOfDay(16, 0));
        TrainingSession early = new TrainingSession(group, coach, DayOfWeek.SUNDAY, new TimeOfDay(8, 45));
        timetable.addNewTrainingSession(late);
        timetable.addNewTrainingSession(early);

        Assertions.assertEquals(List.of(List.of(early), List.of(late)),
                new ArrayList<>(timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY).values()));
    }

    @Test
    void testMissingDayAndTime() {
        Timetable timetable = new Timetable();
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0)).isEmpty());
    }

    @Test
    void testCoachCountsForEmptyTimetable() {
        Assertions.assertTrue(new Timetable().getCountByCoaches().isEmpty());
    }

    @Test
    void testCoachCountsSortedAcrossDays() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach firstCoach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach secondCoach = new Coach("НеВасильев", "НеНиколай", "НеСергеевич");
        timetable.addNewTrainingSession(new TrainingSession(group, firstCoach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, secondCoach, DayOfWeek.MONDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, secondCoach, DayOfWeek.FRIDAY, new TimeOfDay(11, 0)));

        Map<Coach, Integer> counts = timetable.getCountByCoaches();
        Assertions.assertEquals(2, counts.get(secondCoach));
        Assertions.assertEquals(1, counts.get(firstCoach));
        Assertions.assertEquals(List.of(secondCoach, firstCoach), new ArrayList<>(counts.keySet()));
    }

    @Test
    void testSameFullNameHasOneCount() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach first = new Coach("Васильев", "Николай", "Сергеевич");
        Coach sameName = new Coach("Васильев", "Николай", "Сергеевич");
        timetable.addNewTrainingSession(new TrainingSession(group, first, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, sameName, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));

        Assertions.assertEquals(1, timetable.getCountByCoaches().size());
        Assertions.assertEquals(2, timetable.getCountByCoaches().get(first));
    }

    @Test
    void testSameTimeSessionsBothCountForCoach() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));

        Assertions.assertEquals(2, timetable.getCountByCoaches().get(coach));
    }

}
