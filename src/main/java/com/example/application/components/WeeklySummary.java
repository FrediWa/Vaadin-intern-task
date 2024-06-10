package com.example.application.components;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.example.application.data.models.Employee;
import com.example.application.data.services.WorkLogService;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment;

@CssImport("./themes/intern-project/components/weeklySummary.css")
public class WeeklySummary extends VerticalLayout {
    private final H2 welcomeMessage;
    private final Paragraph subtitle;
    private final HorizontalLayout weekDaysSummary;
    private final H4[] hours = new H4[5];
    private final BiFunction<LocalDate, Long, List<Integer>> getTimesForDay;

    public WeeklySummary(Employee userEmployee,
            Function<Long, Employee> getEmployee,
            BiFunction<LocalDate, Long, List<Integer>> getTimesForDay) {
        this.getTimesForDay = getTimesForDay;
        setId("weekly-summary-panel");
        addClassNames(TextAlignment.LEFT);
        setWidth("100%");

        int weekNumber = LocalDate.now()
                .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        weekDaysSummary = new HorizontalLayout();
        welcomeMessage = new H2("Hi " + userEmployee.getFirstname()
                + ", it's week " + weekNumber);
        subtitle = new Paragraph("Good job entering all your hours.");

        for (int i = 0; i < 5; i++) {
            hours[i] = new H4();
        }

        loadWeekly(userEmployee);
    }

    public static String localTimeToString(LocalTime time) {
        return (String.format("%sh%smin", time.getHour(), time.getMinute()));
    }

    public static LocalTime reduceMinutesList(List<Integer> minutesList) {
        int minutesReduced = minutesList.stream().reduce(0, Integer::sum);

        int hours = (minutesReduced / 60);
        int minutes = (minutesReduced % 60);

        return (LocalTime.of(hours, minutes));
    }

    public void loadWeekly(Employee employee) {
        System.out.println("Reload weekly " + employee.getName());
        LocalDate now = LocalDate.now(), currentDate;
        int weekDayNumber = now.getDayOfWeek().getValue();
        String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri" };
        LocalDate localMonday = now.minusDays(weekDayNumber - 1);
        Div weekDayWrapper;
        Paragraph weekDayInfo;

        for (int i = 0; i < 5; i++) {
            weekDayWrapper = new Div();
            currentDate = localMonday.plusDays(i);
            List<Integer> minutesList = getTimesForDay.apply(currentDate,
                    employee.getId());

            hours[i].setText(WeeklySummary.localTimeToString(
                    WeeklySummary.reduceMinutesList(minutesList)));
            weekDayInfo = new Paragraph(weekDays[i] + " "
                    + currentDate.format(DateTimeFormatter.ofPattern("dd.MM")));

            weekDayWrapper.add(weekDayInfo, hours[i]);
            weekDayWrapper.addClassName("weekly-summary-week-day");
            this.weekDaysSummary.add(weekDayWrapper);
        }
        weekDaysSummary.setSpacing(false);
        weekDaysSummary.addClassName("weekly-summary-days-summary");
        this.removeAll();
        this.add(welcomeMessage, subtitle, weekDaysSummary);
    }

    public void updateWeekDaySummary(LocalDate date, String newMinutes) {
        int dayOfTheWeek = date.getDayOfWeek().getValue();

        // As weekdays are modulo week, make sure that the updated entry is in
        // the current week.
        if (areSameWeek(date, LocalDate.now()))
            hours[dayOfTheWeek - 1].setText(newMinutes);
    }

    public boolean areSameWeek(LocalDate d1, LocalDate d2) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int w1 = d1.get(weekFields.weekOfWeekBasedYear());
        int w2 = d2.get(weekFields.weekOfWeekBasedYear());
        System.out.println(w1 == w2);
        return (w1 == w2);
    }

    public void toggleSummary() {
        String className = "weekly-summary-panel-closed";

        if (this.hasClassName(className)) {
            this.removeClassName(className);
        } else {
            this.addClassName(className);
        }
    }
}
