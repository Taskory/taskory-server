package codeartitect.taskflower.security;

import codeartitect.taskflower.user.UserRepository;
import codeartitect.taskflower.user.UserService;
import codeartitect.taskflower.user.model.User;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
import codeartitect.taskflower.user.exception.UsernameAlreadyExistsException;
import codeartitect.taskflower.user.payload.SignupRequest;
import codeartitect.taskflower.user.payload.UserResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
@Deprecated
class AuthControllerTest {

    @Value("${app.url-base}")
    private String urlBase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    private User user;
    private String username;
    private String password;

    @BeforeEach
    void setUp() throws UsernameAlreadyExistsException, InvalidZoneIdException {
        StringBuilder tempUsername;
        do {
            tempUsername = new StringBuilder();
            Random random = new Random();
            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            for (int i = 0; i < 10; i++) {
                tempUsername.append(charsForRandom[random.nextInt(36)]);
            }
        } while (userRepository.existsByUsername(tempUsername.toString()));
        this.username = tempUsername.toString();
        this.password = "1234";
        String zoneId = "Asia/Seoul";
        SignupRequest signupRequest = new SignupRequest(username, password, zoneId);
        UserResponse userResponse = userService.signup(signupRequest);
        user = userRepository.findById(userResponse.getId()).orElseThrow();
    }

    @AfterEach
    void end() {
        userService.deleteById(user.getId());
    }


    /**
     * Login success test
     * @throws Exception Json Exception
     */
    @Test
    @DisplayName("login success")
    void login_success() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", this.username);
        jsonObject.put("password", this.password);
        mockMvc.perform(MockMvcRequestBuilders.post(urlBase + "/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }

    /**
     * Login failed test
     * @throws Exception Json Exception
     */
    @Test
    @DisplayName("login failure")
    void login_failure() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", this.username);
        jsonObject.put("password", "wrong" + this.password);
        mockMvc.perform(MockMvcRequestBuilders.post(urlBase + "/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isUnauthorized());
    }
  
}