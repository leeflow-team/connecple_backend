package com.connecple.connecple_backend.global.common;

import com.connecple.connecple_backend.global.exception.BaseException;
import jakarta.servlet.http.HttpSession;

public class LoginChecker {
    public static void checkAdmin(HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            throw new BaseException(401, "로그인이 필요합니다.");
        }
    }
}
