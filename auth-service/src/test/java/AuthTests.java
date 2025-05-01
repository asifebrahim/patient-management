import com.example.authservice.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.context.annotation.Configuration;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
class AuthTests {

    @Test
    void firstTest() {
        System.out.println("Hello World");
        Assertions.assertTrue(true);
    }

}