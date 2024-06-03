package com.example.application.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import com.example.application.data.models.Employee;
import com.example.application.data.services.WorkLogService;
import com.vaadin.flow.component.UI;
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
        
        int weekNumber = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        welcomeMessage = new H2("Hi " + name + ", it's week "+ weekNumber);
        subtitle = new Paragraph("Good job entering all your hours.");

        reloadWeekly(service, name);
    }

    private String reduceMinutesListToString(List<Integer> minutesList) { 
        int minutesReduced = minutesList.stream().reduce(0, Integer::sum);

        String hoursString = (minutesReduced / 60)+"h";
        String minutesString = (minutesReduced % 60)+"min";

        return (hoursString + minutesString);
    }

    public void reloadWeekly(WorkLogService service, String name) {
        this.weekDaysSummary = new HorizontalLayout();
        System.out.println("Reload weekly " + name);
        Employee currentEmployee = service.getEmployee(2);
        LocalDate now = LocalDate.now(), currentDate;
        int weekDayNumber = now.getDayOfWeek().getValue();
        String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        LocalDate localMonday = now.minusDays(weekDayNumber-1);
        Div weekDayWrapper;
        H4 hours;
        Paragraph weekDayInfo;
       
        for (int i = 0; i < 5; i++) {
            weekDayWrapper = new Div();
            currentDate = localMonday.plusDays(i);
            List<Integer> minutesList = service.getTimesForDay(currentDate, currentEmployee.getId());

            hours = new H4(this.reduceMinutesListToString(minutesList)); 
            weekDayInfo = new Paragraph(weekDays[i] + " " + currentDate.format(DateTimeFormatter.ofPattern("dd.MM")));
            
            weekDayWrapper.add(hours, weekDayInfo);
            this.weekDaysSummary.add(weekDayWrapper);
        }
        System.out.println("removing");
        this.removeAll();
        System.out.println("Adding back");
        this.add(welcomeMessage, subtitle, weekDaysSummary);
    }
}
