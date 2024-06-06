package com.example.application.views;

import com.example.application.components.UserInfo;
import com.example.application.data.models.Employee;
import com.example.application.data.models.MyUserDetails;
import com.example.application.data.services.SecurityService;
import com.example.application.data.services.WorkLogService;
import com.example.application.views.empty2.Empty2View;
import com.example.application.views.workhours.WorkHoursView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    private final WorkLogService service;

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon,
                Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM,
                    AlignItems.CENTER, Padding.Horizontal.SMALL,
                    TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM,
                    Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }
    }
    public MainLayout(SecurityService securityService, WorkLogService service) {
        this.securityService = securityService;
        this.service = service;
        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX,
                FlexDirection.COLUMN, Width.FULL);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassNames(Padding.Horizontal.MEDIUM);

        Nav nav = new Nav();
        nav.addClassNames(Display.FLEX, Overflow.AUTO, Padding.Vertical.XSMALL);

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE,
                Margin.NONE, Padding.NONE);
        nav.add(list);
        MyUserDetails userDetails = securityService.getAuthenticatedUser();
        Employee userEmployee = service.getEmployee(userDetails.getEmployeeId());
        UserInfo userInfo = new UserInfo(userEmployee.getName(), userDetails.getRole(), securityService::logout);
        nav.add(userInfo);
    
        for (MenuItemInfo menuItem : createMenuItems()) {
            list.add(menuItem);

        }
        
        layout.add(nav, userInfo);
        header.add(layout);
        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[] { //
                new MenuItemInfo("Work Hours",
                        LineAwesomeIcon.BUSINESS_TIME_SOLID.create(),
                        WorkHoursView.class), //
                new MenuItemInfo("Vacations",
                        LineAwesomeIcon.SUITCASE_ROLLING_SOLID.create(),
                        Empty2View.class), //
                new MenuItemInfo("Sick Leave",
                        LineAwesomeIcon.NOTES_MEDICAL_SOLID.create(),
                        Empty2View.class), //

        };
    }

}
