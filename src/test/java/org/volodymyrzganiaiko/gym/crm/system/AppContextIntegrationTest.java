package org.volodymyrzganiaiko.gym.crm.system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.volodymyrzganiaiko.gym.crm.system.config.AppConfig;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import static org.junit.jupiter.api.Assertions.*;

public class AppContextIntegrationTest {
    @Test
    public void testContext() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade gymFacade = ctx.getBean(GymFacade.class);
        assertNotNull(gymFacade);
        assertEquals(10, gymFacade.findAllTrainees().size());
        assertEquals(10, gymFacade.findAllTrainers().size());
        assertEquals(50, gymFacade.findAllTrainings().size());
        ctx.close();
    }
}
