import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api';

const API_URL = import.meta.env.VITE_API_URL;

export function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            await api.post('/users/login', { username, password });

            navigate('/');
        } catch (err: any) {
            console.error('Login error:', err);
            setError(err.response?.data?.message || 'Login failed. Please check your credentials.');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-zinc-950 text-white">
            <div className="bg-zinc-900 p-8 rounded-lg shadow-xl w-full max-w-md border border-zinc-800">
                <h2 className="text-2xl font-bold mb-6 text-center bg-gradient-to-r from-blue-500 to-purple-500 bg-clip-text text-transparent">
                    Login to Jacket
                </h2>

                {error && (
                    <div className="bg-red-500/10 border border-red-500/50 text-red-500 p-3 rounded mb-4 text-sm">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-zinc-400 mb-1">Username</label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500"
                            required
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-zinc-400 mb-1">Password</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500"
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-blue-600 hover:bg-blue-500 text-white font-bold py-2 px-4 rounded transition-colors"
                    >
                        Login
                    </button>
                </form>

                <div className="flex items-center my-6">
                    <div className="flex-grow h-px bg-zinc-700"></div>
                    <span className="px-3 text-zinc-500 text-sm">OR</span>
                    <div className="flex-grow h-px bg-zinc-700"></div>
                </div>

                <div className="space-y-3">
                    {/* Google Login  */}
                    <a
                        href={`${API_URL}/oauth2/authorization/google`}
                        className="w-full bg-white text-zinc-800 border border-zinc-300 hover:bg-gray-50 font-medium rounded flex items-center justify-center h-10 transition-colors no-underline"
                    >
                        <svg className="w-5 h-5 mr-2" viewBox="0 0 48 48">
                            <path fill="#EA4335" d="M24 9.5c3.54 0 6.71 1.22 9.21 3.6l6.85-6.85C35.9 2.38 30.47 0 24 0 14.62 0 6.51 5.38 2.56 13.22l7.98 6.19C12.43 13.72 17.74 9.5 24 9.5z"></path>
                            <path fill="#4285F4" d="M46.98 24.55c0-1.57-.15-3.09-.38-4.55H24v9.02h12.94c-.58 2.96-2.26 5.48-4.78 7.18l7.73 6c4.51-4.18 7.09-10.36 7.09-17.65z"></path>
                            <path fill="#FBBC05" d="M10.53 28.59c-.48-1.45-.76-2.99-.76-4.59s.27-3.14.76-4.59l-7.98-6.19C.92 16.46 0 20.12 0 24c0 3.88.92 7.54 2.56 10.78l7.97-6.19z"></path>
                            <path fill="#34A853" d="M24 48c6.48 0 11.93-2.13 15.89-5.81l-7.73-6c-2.15 1.45-4.92 2.3-8.16 2.3-6.26 0-11.57-4.22-13.47-9.91l-7.98 6.19C6.51 42.62 14.62 48 24 48z"></path>
                        </svg>
                        Google로 로그인
                    </a>



                    {/* Naver Login */}
                    <a
                        href={`${API_URL}/oauth2/authorization/naver`}
                        className="w-full bg-[#03C75A] text-white hover:bg-[#02b351] font-medium rounded flex items-center justify-center h-10 transition-colors no-underline"
                    >
                        <svg className="w-4 h-4 mr-2 fill-current" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                            <path d="M16.273 12.845L7.376 0H0v24h7.726V11.156L16.624 24H24V0h-7.727v12.845z" />
                        </svg>
                        Naver로 로그인
                    </a>

                    {/* Kakao Login */}
                    <a
                        href={`${API_URL}/oauth2/authorization/kakao`}
                        className="w-full bg-[#FEE500] text-[#3C1E1E] hover:bg-[#fdd835] font-medium rounded flex items-center justify-center h-10 transition-colors no-underline"
                    >
                        <svg className="w-5 h-5 mr-2 fill-current" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                            <path d="M12 3C5.373 3 0 6.758 0 11.4c0 2.895 1.91 5.416 4.804 6.933-.215.795-.78 2.872-.892 3.298-.14.516.19.508.4.336.27-.223 3.01-2.05 4.22-2.86.97.14 1.97.215 2.98.215 6.627 0 12-3.757 12-8.4S16.627 3 12 3z" />
                        </svg>
                        Kakao로 로그인
                    </a>
                </div>

                <div className="mt-6 text-center text-sm text-zinc-500 space-x-4">
                    <Link to="/signup" className="text-blue-400 hover:text-blue-300">
                        회원가입
                    </Link>
                    <span>|</span>
                    <Link to="/find-id" className="text-zinc-400 hover:text-zinc-300">
                        아이디 찾기
                    </Link>
                    <span>|</span>
                    <Link to="/reset-password" className="text-zinc-400 hover:text-zinc-300">
                        비밀번호 초기화
                    </Link>
                </div>
            </div>
        </div>
    );
}