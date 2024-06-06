package com.example.application.components;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment;
import com.vaadin.flow.component.html.Paragraph;

public class UserInfo extends HorizontalLayout {
    Avatar userAvatar;
    Paragraph userName;
    Paragraph userRole;

    public UserInfo(String name, String role, Runnable logout) {
        setAlignItems(FlexComponent.Alignment.CENTER);
        userAvatar = new Avatar(name);
        userName = new Paragraph(name);
        userRole = new Paragraph(role);

        userName.addClassNames(Margin.Vertical.NONE, TextAlignment.RIGHT);
        userRole.addClassNames(Margin.Vertical.NONE, FontSize.XSMALL,
                TextAlignment.RIGHT);

        userAvatar.addThemeVariants(AvatarVariant.LUMO_LARGE);

        Div userNameAndRole = new Div();
        Button logoutButton = new Button("Log out", e -> logout.run());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                    
        userNameAndRole.add(userName, userRole);
        add(logoutButton, userNameAndRole, userAvatar);
    }
}
