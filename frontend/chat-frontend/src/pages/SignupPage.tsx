import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api';

export function SignupPage() {
    const [formData, setFormData] = useState({
        userId: '',
        password: '',
        userName: '',
        nickname: '',
        phone: '',
        email: ''
    });

    const [isIdChecked, setIsIdChecked] = useState(false);
    const [verificationCode, setVerificationCode] = useState('');
    const [isCodeSent, setIsCodeSent] = useState(false);
    const [isVerified, setIsVerified] = useState(false);

    const [error, setError] = useState('');
    const [successMsg, setSuccessMsg] = useState('');
    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });

        if (name === 'userId') {
            setIsIdChecked(false);
            setSuccessMsg('');
        }

        // 💡 이메일 주소를 수정하면 인증 상태를 초기화합니다.
        if (name === 'email') {
            setIsVerified(false);
            setIsCodeSent(false); // 새로 발송해야 하므로 발송 상태도 초기화
            setVerificationCode('');
        }
    };

    const handleCheckId = async () => {
        if (!formData.userId.trim()) {
            setError('아이디를 입력해주세요.');
            return;
        }
        try {
            setError('');
            await api.get(`/users/check-id?userId=${formData.userId}`);
            setIsIdChecked(true);
            setSuccessMsg('사용 가능한 아이디입니다.');
        } catch (err: any) {
            setIsIdChecked(false);
            setError(err.response?.data?.message || '이미 사용 중인 아이디입니다.');
        }
    };

    const handleSendCode = async () => {
        if (!formData.email) {
            alert('이메일을 입력해주세요.');
            return;
        }
        try {
            setError('');
            // 💡 새 코드를 보낼 때 이전 인증 상태를 초기화합니다.
            setIsVerified(false);

            await api.post('/email/send', {
                email: formData.email,
                purpose: 'signup'
            });
            setIsCodeSent(true);
            alert('인증 코드가 전송되었습니다. (유효시간을 확인해주세요)');
        } catch (err: any) {
            setError(err.response?.data?.message || '인증 코드 전송에 실패했습니다.');
        }
    };

    const handleVerifyCode = async () => {
        try {
            setError('');
            await api.post('/email/verify', {
                email: formData.email,
                purpose: 'signup',
                code: verificationCode
            });
            setIsVerified(true);
            alert('이메일 인증이 완료되었습니다.');
        } catch (err: any) {
            setIsVerified(false);
            setError(err.response?.data?.message || '인증 코드가 일치하지 않거나 만료되었습니다.');
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (!isIdChecked) {
            setError('아이디 중복 확인을 해주세요.');
            return;
        }

        if (!isVerified) {
            setError('이메일 인증을 완료해주세요.');
            return;
        }

        try {
            await api.post('/users/register', formData);
            alert('회원가입에 성공하였습니다.');
            navigate('/login');
        } catch (err: any) {
            console.error('Registration error:', err);
            setError(err.response?.data?.message || '회원가입에 실패하였습니다.');
        }
    };

    return (
        <div className="min-h-screen w-full bg-zinc-950 text-white overflow-y-auto">
            <div className="flex items-center justify-center py-12 px-4 min-h-full">
                <div className="bg-zinc-900 p-8 rounded-lg shadow-xl w-full max-w-md border border-zinc-800 my-auto">
                    <h2 className="text-2xl font-bold mb-6 text-center bg-gradient-to-r from-blue-500 to-purple-500 bg-clip-text text-transparent">
                        Sign Up for Jacket
                    </h2>

                    {error && (
                        <div className="bg-red-500/10 border border-red-500/50 text-red-500 p-3 rounded mb-4 text-sm">
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="space-y-4">
                        {/* User ID */}
                        <div>
                            <label className="block text-sm font-medium text-zinc-400 mb-1">User ID</label>
                            <div className="flex gap-2">
                                <input name="userId" type="text" value={formData.userId} onChange={handleChange} className="flex-1 bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500" required />
                                <button type="button" onClick={handleCheckId} disabled={isIdChecked && formData.userId !== ''} className="bg-zinc-700 hover:bg-zinc-600 px-3 py-2 rounded text-sm transition-colors whitespace-nowrap disabled:bg-green-600/20 disabled:text-green-500">
                                    {isIdChecked ? '확인됨' : '중복확인'}
                                </button>
                            </div>
                            {successMsg && <p className="mt-1 text-xs text-green-500 animate-fadeIn">{successMsg}</p>}
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-zinc-400 mb-1">Password</label>
                            <input name="password" type="password" value={formData.password} onChange={handleChange} className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500" required />
                        </div>

                        {/* Email 섹션 - 수정됨 (항상 입력 가능) */}
                        <div>
                            <label className="block text-sm font-medium text-zinc-400 mb-1">Email</label>
                            <div className="flex gap-2">
                                <input
                                    name="email"
                                    type="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    placeholder="example@email.com"
                                    className={`flex-1 bg-zinc-800 border rounded p-2 text-white focus:outline-none transition-colors ${isVerified ? 'border-green-500/50' : 'border-zinc-700 focus:border-blue-500'}`}
                                    required
                                />
                                <button
                                    type="button"
                                    onClick={handleSendCode}
                                    className="bg-zinc-700 hover:bg-zinc-600 px-3 py-2 rounded text-sm transition-colors whitespace-nowrap"
                                >
                                    {isCodeSent ? '재전송' : '코드발송'}
                                </button>
                            </div>
                            {isVerified && <p className="mt-1 text-xs text-green-500">이메일 인증이 확인되었습니다.</p>}
                        </div>

                        {isCodeSent && (
                            <div className="animate-fadeIn">
                                <label className="block text-sm font-medium text-zinc-400 mb-1">Verification Code</label>
                                <div className="flex gap-2">
                                    <input
                                        type="text"
                                        placeholder="6자리 코드 입력"
                                        value={verificationCode}
                                        onChange={(e) => setVerificationCode(e.target.value)}
                                        disabled={isVerified}
                                        className="flex-1 bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500 disabled:text-green-500"
                                        maxLength={6}
                                    />
                                    <button
                                        type="button"
                                        onClick={handleVerifyCode}
                                        disabled={isVerified}
                                        className="bg-blue-600 hover:bg-blue-500 px-4 py-2 rounded text-sm transition-colors disabled:bg-green-600/50"
                                    >
                                        {isVerified ? '인증완료' : '확인'}
                                    </button>
                                </div>
                            </div>
                        )}

                        <div>
                            <label className="block text-sm font-medium text-zinc-400 mb-1">Name</label>
                            <input name="userName" type="text" value={formData.userName} onChange={handleChange} className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500" required />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-zinc-400 mb-1">Nickname</label>
                            <input name="nickname" type="text" value={formData.nickname} onChange={handleChange} className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500" required />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-zinc-400 mb-1">Phone (Optional)</label>
                            <input name="phone" type="tel" value={formData.phone} onChange={handleChange} className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 text-white focus:outline-none focus:border-blue-500" />
                        </div>

                        <button
                            type="submit"
                            disabled={!isVerified || !isIdChecked}
                            className="w-full bg-blue-600 hover:bg-blue-500 disabled:bg-zinc-700 disabled:cursor-not-allowed text-white font-bold py-2 px-4 rounded transition-colors mt-4"
                        >
                            Create Account
                        </button>
                    </form>

                    <div className="mt-6 text-center text-sm text-zinc-500">
                        Already have an account?{' '}
                        <Link to="/login" className="text-blue-400 hover:text-blue-300">Login</Link>
                    </div>
                </div>
            </div>
        </div>
    );
}