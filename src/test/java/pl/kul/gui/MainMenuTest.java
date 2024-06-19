package pl.kul.gui;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kul.Application;
import pl.kul.facilities.Storage;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuTest {
    private FrameFixture window;
    private MainMenu mainMenu;

    @BeforeEach
    public void setup() {
        // given
        Application.setStorage(new Storage());
        // and
        mainMenu = GuiActionRunner.execute(MainMenu::new);
        // and
        window = new FrameFixture(mainMenu.frame);
        window.show();
    }

    @Test
    void should_initialize_all_buttons() {
        // then
        assertNotNull(mainMenu.getCashRegisterButton());
        // and
        assertNotNull(mainMenu.getStorageButton());
        // and
        assertNotNull(mainMenu.getExitButton());
    }

    @Test
    void should_initialize_all_panels() {
        // then
        assertNotNull(mainMenu.getCashRegisterPanel());
        // and
        assertNotNull(mainMenu.getStoragePanel());
    }

    @Test
    void all_buttons_should_be_visible() {
        // then
        assertTrue(window.button("storageButton").target().isVisible());
        // and
        assertTrue(window.button("cashRegisterButton").target().isVisible());
        // and
        assertTrue(window.button("exitButton").target().isVisible());
    }


    @Test
    void should_display_main_menu_with_storage_cashRegister_and_exit() {
        // then
        assertTrue(window.button("storageButton").target().isVisible());
        // and
        assertTrue(window.button("cashRegisterButton").target().isVisible());
        // and
        assertTrue(window.button("exitButton").target().isVisible());
    }

    @Test
    void should_switch_panel_to_storage_after_clicking_storage_button() {
        // when
        window.button("storageButton").click();
        // then
        assertTrue(window.panel("storagePanel").target().isVisible());
    }

    @Test
    void should_switch_panel_to_cash_register_after_clicking_cash_register_button() {
        // when
        window.button("cashRegisterButton").click();
        // then
        assertTrue(window.panel("cashRegisterPanel").target().isVisible());
    }

    @Test
    void should_exit_application_after_clicking_exit_button() {
        // when
        window.button("exitButton").click();
        // then
        assertFalse(window.target().isVisible());
    }

    @AfterEach
    public void tearDown() {
        if(window != null)
            window.cleanUp();
    }
}