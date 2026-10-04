import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 加解密测试类
 *
 * @author summerain0
 */
public class CryptoTest {
    @Test
    public void testCrypto() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword);
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        assert matches;
    }
}
