package global.util;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Base64;

public class SecuritySecretKeyTests {

    @Test
    void secretKeyMakeUtile() throws Exception {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("generated secretkey : " + base64Key);

    }
}
