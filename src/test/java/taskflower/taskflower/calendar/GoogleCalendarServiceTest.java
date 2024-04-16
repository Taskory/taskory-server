package taskflower.taskflower.calendar;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class GoogleCalendarServiceTest {

    private final GoogleCalendarService calendarService;

    @Autowired
    GoogleCalendarServiceTest(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Test
    @DisplayName("Google Calendar API 연결 테스트")
    void connectText() throws IOException {
        List<Object> result = calendarService.synchronize();
        System.out.println(result);
    }
}