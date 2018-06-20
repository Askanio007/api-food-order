package utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptingString {

    private static ThreadLocal<BCryptPasswordEncoder> coder = new ThreadLocal<>();

    public static BCryptPasswordEncoder getEncoder() {
        return getEncoder(coder);
    }

    private static BCryptPasswordEncoder getEncoder(ThreadLocal<BCryptPasswordEncoder> thread) {
        BCryptPasswordEncoder coder = thread.get();
        if (coder == null) {
            coder = new BCryptPasswordEncoder();
            thread.set(coder);
        }
        return coder;
    }
}
