package com.example.application.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

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
    H2 welcomeMessage;
    Paragraph subtitle;
    HorizontalLayout weekDaysSummary;
    H4[] hours = new H4[5];

    public WeeklySummary(String name, WorkLogService service) {
        setId("weekly-summary-panel");
        addClassNames(TextAlignment.LEFT);
        setWidth("100%");

        int weekNumber = LocalDate.now()
                .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        welcomeMessage = new H2("Hi " + name + ", it's week " + weekNumber);
        subtitle = new Paragraph("Good job entering all your hours.");

        for (int i = 0; i < 5; i++) {
            hours[i] = new H4();
        }

        loadWeekly(service, name);
    }

    public static String reduceMinutesListToString(List<Integer> minutesList) {
        int minutesReduced = minutesList.stream().reduce(0, Integer::sum);

        String hoursString = (minutesReduced / 60) + "h";
        String minutesString = (minutesReduced % 60) + "min";

        return (hoursString + minutesString);
    }

    public void loadWeekly(WorkLogService service, String name) {
        this.weekDaysSummary = new HorizontalLayout();
        System.out.println("Reload weekly " + name);
        Employee currentEmployee = service.getEmployee(2);
        LocalDate now = LocalDate.now(), currentDate;
        int weekDayNumber = now.getDayOfWeek().getValue();
        String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri" };
        LocalDate localMonday = now.minusDays(weekDayNumber - 1);
        Div weekDayWrapper;
        Paragraph weekDayInfo;

        for (int i = 0; i < 5; i++) {
            weekDayWrapper = new Div();
            currentDate = localMonday.plusDays(i);
            List<Integer> minutesList = service.getTimesForDay(currentDate,
                    currentEmployee.getId());

            hours[i].setText(this.reduceMinutesListToString(minutesList));
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
        hours[dayOfTheWeek - 1].setText(newMinutes);
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
