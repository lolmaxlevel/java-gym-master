package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    // День недели -> время начала -> занятия, которые начинаются в это время
    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = timetable.computeIfAbsent(day, k -> new TreeMap<>());

        TimeOfDay time = trainingSession.getTimeOfDay();
        List<TrainingSession> sessionsAtTime = sessionsForDay.computeIfAbsent(time, k -> new ArrayList<>());
        sessionsAtTime.add(trainingSession);
    }


    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        if (sessionsForDay == null) {
            return new TreeMap<>();
        }
        return sessionsForDay;
    }


    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        if (sessionsForDay == null) {
            return Collections.emptyList();
        }
        List<TrainingSession> sessions = sessionsForDay.get(timeOfDay);
        if (sessions == null) {
            return Collections.emptyList();
        }
        return sessions;
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> counts = new HashMap<>();
        for (TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay : timetable.values()) {
            for (List<TrainingSession> sessionsAtTime : sessionsForDay.values()) {
                for (TrainingSession session : sessionsAtTime) {
                    Coach coach = session.getCoach();
                    counts.put(coach, counts.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<Map.Entry<Coach, Integer>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort((first, second) -> Integer.compare(second.getValue(), first.getValue()));
        Map<Coach, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> entry : sorted) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
