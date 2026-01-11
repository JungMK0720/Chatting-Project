import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { LoginPage } from './pages/LoginPage';
import { SignupPage } from './pages/SignupPage';
import { FindIdPage } from './pages/FindIdPage';
import { ResetPasswordPage } from './pages/ResetPasswordPage';
import api from './services/api';

// -----------------------------------------------------------------------
// PrivateRoute: 인증된 사용자만 접근 가능하게 막는 보호막
// -------------------------------------------------------------------------
function PrivateRoute({ children }: { children: React.ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        // 서버에 세션이나 쿠키가 유효한지 확인 (채팅 시작 전 본인 인증)
        await api.get('/users/me');
        setIsAuthenticated(true);
      } catch {
        setIsAuthenticated(false);
      }
    };
    checkAuth();
  }, []);

  if (isAuthenticated === null) {
    return (
      <div className="flex h-screen items-center justify-center bg-zinc-950 text-white">
        인증 정보를 확인 중입니다...
      </div>
    );
  }

  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 공개 페이지: 누구나 접근 가능 */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/find-id" element={<FindIdPage />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />

        {/* 보호된 페이지: 로그인해야만 들어오는 메인(채팅 예정지) */}
        <Route
          path="/"
          element={
            <PrivateRoute>
              <div className="text-white bg-zinc-900 h-screen flex items-center justify-center">
                <h1>로그인 성공! 여기서 이제 채팅을 시작하면 돼.</h1>
              </div>
            </PrivateRoute>
          }
        />

        {/* 잘못된 경로로 오면 로그인으로 리다이렉트 */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}