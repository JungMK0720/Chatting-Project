import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export function ResetPasswordPage() {
    const [email, setEmail] = useState('');
    const [code, setCode] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [step, setStep] = useState(1);
    const navigate = useNavigate();

    const handleSendCode = async () => {
        try {
            await api.post('/email/send', { email, purpose: 'reset-password' });
            setStep(2);
        } catch (err: any) { alert('이메일 전송에 실패했습니다.'); }
    };

    const handleVerifyCode = async () => {
        try {
            await api.post('/email/verify', { email, code, purpose: 'reset-password' });
            setStep(3);
        } catch (err: any) { alert('인증 코드가 틀렸거나 만료되었습니다.'); }
    };

    const handleReset = async () => {
        try {
            await api.post('/users/reset-password', { email, newPassword });
            alert('비밀번호가 성공적으로 변경되었습니다.');
            navigate('/login');
        } catch (err: any) { alert('비밀번호 변경 중 오류가 발생했습니다.'); }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-zinc-950 text-white">
            <div className="bg-zinc-900 p-8 rounded-lg shadow-xl w-full max-w-md border border-zinc-800">
                <h2 className="text-2xl font-bold mb-6 text-center text-purple-500">비밀번호 재설정</h2>

                {step === 1 && (
                    <div className="space-y-4">
                        <p className="text-zinc-400 text-sm">비밀번호를 재설정할 계정의 이메일을 입력해주세요.</p>
                        <input type="email" placeholder="email@example.com" value={email} onChange={(e) => setEmail(e.target.value)}
                            className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 outline-none focus:border-purple-500" />
                        <button onClick={handleSendCode} className="w-full bg-purple-600 hover:bg-purple-500 py-2 rounded font-bold">인증코드 받기</button>
                    </div>
                )}

                {step === 2 && (
                    <div className="space-y-4">
                        <p className="text-purple-400 text-sm text-center">인증 코드를 입력해주세요.</p>
                        <input type="text" placeholder="6자리 코드" value={code} onChange={(e) => setCode(e.target.value)}
                            className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 outline-none focus:border-purple-500 text-center tracking-widest" />
                        <button onClick={handleVerifyCode} className="w-full bg-purple-600 hover:bg-purple-500 py-2 rounded font-bold">코드 확인</button>
                    </div>
                )}

                {step === 3 && (
                    <div className="space-y-4">
                        <p className="text-zinc-400 text-sm">새로운 비밀번호를 입력해주세요.</p>
                        <input type="password" placeholder="새 비밀번호" value={newPassword} onChange={(e) => setNewPassword(e.target.value)}
                            className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 outline-none focus:border-green-500" />
                        <button onClick={handleReset} className="w-full bg-green-600 hover:bg-green-500 py-2 rounded font-bold">비밀번호 변경하기</button>
                    </div>
                )}
            </div>
        </div>
    );
}