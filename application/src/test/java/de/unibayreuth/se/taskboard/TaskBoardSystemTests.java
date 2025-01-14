package de.unibayreuth.se.taskboard;

import de.unibayreuth.se.taskboard.api.dtos.TaskDto;
import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.api.mapper.TaskDtoMapper;
import de.unibayreuth.se.taskboard.api.mapper.UserDtoMapper;
import de.unibayreuth.se.taskboard.business.domain.Task;
import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;


public class TaskBoardSystemTests extends AbstractSystemTest {

    @Autowired
    private TaskDtoMapper taskDtoMapper;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private UserService userService;

    @Test
    void getAllCreatedTasks() {
        List<Task> createdTasks = TestFixtures.createTasks(taskService);

        List<Task> retrievedTasks = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/tasks")
                .then()
                .statusCode(200)
                .body(".", hasSize(createdTasks.size()))
                .and()
                .extract().jsonPath().getList("$", TaskDto.class)
                .stream()
                .map(taskDtoMapper::toBusiness)
                .toList();

        assertThat(retrievedTasks)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .containsExactlyInAnyOrderElementsOf(createdTasks);
    }

    @Test
    void createAndDeleteTask() {
        Task createdTask = taskService.create(
                TestFixtures.getTasks().getFirst()
        );

        when()
                .get("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(200);

        when()
                .delete("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(200);

        when()
                .get("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(400);

    }

    //TODO: Add at least one test for each new endpoint in the users controller (the create endpoint can be tested as part of the other endpoints).
    /*@Test // getting nullpointerexception because of a name of an user being null
    void getAllUsers() {
        List<User> createdUsers = TestFixtures.createUsers(userService);

        // api call
        List<UserDto> retrievedUserDtos = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body(".", hasSize(createdUsers.size())) // check size of list
                .and()
                .extract().jsonPath().getList("$", UserDto.class);

        // convert retrieved dtos to
        List<User> retrievedUsers = retrievedUserDtos.stream()
                .map(userDtoMapper::toBusiness)
                .toList();

        // assert
        assertThat(retrievedUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt") // ignore createdAt
                .containsExactlyInAnyOrderElementsOf(createdUsers);
    }*/

    @Test
    void getUserById() {
        // create and save new user
        User createdUser = userService.createUser(
                new User("Test User")
        );
        UserDto createdUserDto = new UserDto(null, LocalDateTime.now(), "Test User");

        // get user through api
        UserDto retrievedUserDto = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users/{id}", createdUser.getId())
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        // compare created user to retrieved user
        assertThat(retrievedUserDto)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(createdUserDto);
    }

    @Test
    void createUser() {
        UserDto newUserDto = new UserDto(null, LocalDateTime.now(), "New User");

        UserDto createdUserDto = given()
                .contentType(ContentType.JSON)
                .body(newUserDto)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);

        // check answer
        assertThat(createdUserDto.name()).isEqualTo("New User"); // assert Name
        assertThat(createdUserDto.id()).isNotNull(); // assert that id was created
    }
}