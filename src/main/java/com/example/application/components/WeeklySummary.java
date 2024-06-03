package com.example.application.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.example.application.data.services.WorkLogService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment;

public class WeeklySummary extends Div {
    H2 welcomeMessage;
    Paragraph subtitle;
    HorizontalLayout weekDaysSummary;
    public WeeklySummary(String name, WorkLogService service) {
        addClassNames(TextAlignment.LEFT);
        setWidth("100%");
        LocalDate now = LocalDate.now(); 
        weekDaysSummary = new HorizontalLayout();
        int weekNumber = now.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        int weekDayNumber = now.getDayOfWeek().getValue();
        LocalDate localMonday = now.minusDays(weekDayNumber-1);

        String weekDays[] = {"Mon", "Tue", "Wed", "Thu", "Fri"};

        welcomeMessage = new H2("Hi " + name + ", it's week "+ weekNumber);
        subtitle = new Paragraph("Good job entering all your hours.");
        LocalDate currentDate;
        H4 hours;
        Paragraph weekDayInfo;
        Div weekDayWrapper;
        for (int i = 0; i < 5; i++) {
            currentDate = localMonday.plusDays(i);
            List<Integer> minutesList = service.getTimesForDay(currentDate);

            hours = new H4(this.reduceMinutesListToString(minutesList)); // TODO: remove hardcoded value
            weekDayInfo = new Paragraph(weekDays[i] + " " + currentDate.format(DateTimeFormatter.ofPattern("dd.MM")));
            
            weekDayWrapper = new Div();
            weekDayWrapper.setWidth("100%");

            weekDayWrapper.add(hours, weekDayInfo);

            weekDaysSummary.add(weekDayWrapper);
        }

        add(welcomeMessage, subtitle, weekDaysSummary);
    }

    private String reduceMinutesListToString(List<Integer> minutesList) { 
        int minutesReduced = minutesList.stream().reduce(0, Integer::sum);

        String hoursString = (minutesReduced / 60)+"h";
        String minutesString = (minutesReduced % 60)+"min";

        return (hoursString + minutesString);
    }
}
