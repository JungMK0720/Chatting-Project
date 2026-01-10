import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import api from '../services/api';
import axios from 'axios';

export function AdditionalSignupPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    // State from URL params
    const [provider, setProvider] = useState('');
    const [providerId, setProviderId] = useState('');
    const [nickname, setNickname] = useState('');

    // Form State
    const [email, setEmail] = useState('');
    const [verificationCode, setVerificationCode] = useState('');

    // UI State
    const [isEmailVerified, setIsEmailVerified] = useState(false);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        const paramProvider = searchParams.get('provider');
        const paramProviderId = searchParams.get('providerId');
        const paramNickname = searchParams.get('nickname');
        const paramEmail = searchParams.get('email');

        if (paramProvider) setProvider(paramProvider);
        if (paramProviderId) setProviderId(paramProviderId);
        if (paramNickname) setNickname(paramNickname);
        if (paramEmail) setEmail(paramEmail);

        if (!paramProvider || !paramProviderId) {
            setError('Missing provider information. Please try logging in again.');
        }
    }, [searchParams]);

    const isValidEmail = (email: string) => {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    };

    const sendCode = async () => {
        setError('');

        if (!isValidEmail(email)) {
            setError('유효한 이메일 형식이 아닙니다.');
            return;
        }

        setIsLoading(true);
        try {
            await api.post('/email/send', { email, purpose: 'link' });
            alert('인증 코드가 발송되었습니다.');
        } catch (err: unknown) { // any 대신 unknown!
            console.error('Send code error:', err);
            
            if (axios.isAxiosError(err)) {
                // 백엔드에서 보내준 구체적인 에러 메시지가 있다면 그걸 보여주고, 없으면 기본 메시지
                alert(err.response?.data?.message || '이메일 발송 실패. 이메일 주소를 확인해주세요.');
            } else {
                alert('알 수 없는 오류가 발생했습니다.');
            }
        } finally {
            setIsLoading(false);
        }
    };

    const verifyCode = async () => {
        setError('');

        if (!verificationCode) {
            setError('인증 코드를 입력해주세요.');
            return;
        }

        setIsLoading(true);
        try {
            const response = await api.post('/email/verify', {
                email,
                code: verificationCode,
                purpose: 'link'
            });

            if (response.status === 200) {
                setIsEmailVerified(true);
                alert('이메일 인증 완료');
            }
        } catch (err: unknown) { // 1. any 대신 unknown
            console.error('Verify code error:', err);

            // 2. axios 에러인지 확인
            if (axios.isAxiosError(err)) {
                if (err.response && err.response.status === 409) {
                    alert('이미 가입된 이메일입니다. 계정 연동을 확인합니다.');
                    const redirectUrl = `/link-confirm?providerId=${providerId}&provider=${provider}&nickname=${nickname}&existingEmail=${email}`;
                    navigate(redirectUrl);
                } else {
                    setError(err.response?.data?.message || '인증 확인 중 오류가 발생했습니다.');
                }
            } else {
                // 3. axios 에러가 아닌 경우 처리
                setError('알 수 없는 오류가 발생했습니다.');
            }
        } finally {
            setIsLoading(false);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (!isEmailVerified) {
            setError('이메일 인증을 완료해주세요.');
            return;
        }

        setIsLoading(true);
        try {
            const response = await api.post('/users/social-complete', {
                email,
                nickname,
                provider,
                providerId,
                verificationCode
            });

            if (response.status === 200 || response.status === 201) {
                alert('회원가입 성공! 로그인 페이지로 이동합니다.');
                navigate('/login');
            }
        } catch (err: unknown) {
            if (axios.isAxiosError(err)) {
                setError(err.response?.data?.message || '회원가입 실패. 이메일 중복 등을 확인하세요.');
            } else {
                setError('알 수 없는 오류가 발생했습니다.');
            }
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-zinc-950 text-white">
            <div className="bg-zinc-900 p-8 rounded-lg shadow-xl w-full max-w-md border border-zinc-800">
                <div className="text-center mb-6">
                    <h1 className="text-2xl font-bold mb-2">Carmmunity</h1>
                    <h5 className="text-lg font-medium text-white">
                        {nickname ? `${nickname}님, 환영합니다!` : '환영합니다!'}
                    </h5>
                    <p className="text-zinc-400 text-sm mt-2">
                        소셜 로그인을 완료하려면 사용하실 이메일을 입력하고 인증해주세요.
                    </p>
                </div>

                {error && (
                    <div className="bg-red-500/10 border border-red-500/50 text-red-500 p-3 rounded mb-4 text-sm">
                        {error}
                    </div>
                )}

                <div className="space-y-4">
                    {/* Nickname Input */}
                    {!searchParams.get('nickname') && (
                        <div>
                            <label className="block text-sm font-medium text-zinc-400 mb-1">Nickname</label>
                            <input
                                type="text"
                                value={nickname}
                                onChange={(e) => setNickname(e.target.value)}
                                className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500"
                                placeholder="닉네임을 입력하세요"
                            />
                        </div>
                    )}

                    {/* Email Input & Send Code */}
                    <div>
                        <label className="block text-sm font-medium text-zinc-400 mb-1">이메일 주소</label>
                        <div className="flex gap-2">
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                disabled={isEmailVerified}
                                className={`flex-1 bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500 ${isEmailVerified ? 'opacity-50 cursor-not-allowed' : ''}`}
                                placeholder="이메일 주소"
                            />
                            <button
                                type="button"
                                onClick={sendCode}
                                disabled={isEmailVerified || isLoading}
                                className="bg-zinc-700 hover:bg-zinc-600 text-white px-3 py-2 rounded text-sm whitespace-nowrap transition-colors disabled:opacity-50"
                            >
                                코드 발송
                            </button>
                        </div>
                    </div>

                    {/* Verification Code Input & Verify */}
                    <div>
                        <label className="block text-sm font-medium text-zinc-400 mb-1">인증 코드</label>
                        <div className="flex gap-2">
                            <input
                                type="text"
                                value={verificationCode}
                                onChange={(e) => setVerificationCode(e.target.value)}
                                disabled={isEmailVerified}
                                className={`flex-1 bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500 ${isEmailVerified ? 'opacity-50 cursor-not-allowed' : ''}`}
                                placeholder="인증 코드 6자리"
                            />
                            <button
                                type="button"
                                onClick={verifyCode}
                                disabled={isEmailVerified || isLoading}
                                className="bg-green-600 hover:bg-green-500 text-white px-3 py-2 rounded text-sm whitespace-nowrap transition-colors disabled:opacity-50"
                            >
                                인증 확인
                            </button>
                        </div>
                    </div>

                    <button
                        onClick={handleSubmit}
                        disabled={!isEmailVerified || isLoading}
                        className="w-full bg-blue-600 hover:bg-blue-500 text-white font-bold py-2 px-4 rounded transition-colors disabled:opacity-50 disabled:cursor-not-allowed mt-6"
                    >
                        가입 완료
                    </button>
                </div>
            </div>
        </div>
    );
}