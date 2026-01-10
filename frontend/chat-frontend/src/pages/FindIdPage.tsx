import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export function FindIdPage() {
    const [email, setEmail] = useState('');
    const [code, setCode] = useState('');
    const [step, setStep] = useState(1);
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    // 1. 인증코드 발송 (/api/email/send)
    const handleSendCode = async () => {
        try {
            await api.post('/email/send', { email, purpose: 'find-id' });
            setStep(2);
            setMessage('인증 코드가 이메일로 발송되었습니다.');
        } catch (err: any) {
            alert('이메일 전송에 실패했습니다.');
        }
    };

    // 2. 인증코드 검증 및 아이디 찾기 요청
    const handleVerifyAndFind = async () => {
        try {
            // 코드 검증 (/api/email/verify)
            await api.post('/email/verify', { email, code, purpose: 'find-id' });

            // 실제 아이디 찾기(이메일로 아이디 정보 발송) 요청 (/api/users/find-id)
            await api.post('/users/find-id', { email });
            setStep(3);
        } catch (err: any) {
            alert('이메일 인증에 실패했습니다.');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-zinc-950 text-white">
            <div className="bg-zinc-900 p-8 rounded-lg shadow-xl w-full max-w-md border border-zinc-800">
                <h2 className="text-2xl font-bold mb-6 text-center text-blue-500">아이디 찾기</h2>

                {step === 1 && (
                    <div className="space-y-4">
                        <p className="text-zinc-400 text-sm">가입하신 이메일 주소를 입력해주세요.</p>
                        <input
                            type="email"
                            placeholder="email@example.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 focus:border-blue-500 outline-none"
                        />
                        <button onClick={handleSendCode} className="w-full bg-blue-600 hover:bg-blue-500 py-2 rounded font-bold transition-colors">인증코드 받기</button>
                    </div>
                )}

                {step === 2 && (
                    <div className="space-y-4">
                        <p className="text-blue-400 text-sm text-center">{message}</p>
                        <input
                            type="text"
                            placeholder="인증코드 6자리"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            className="w-full bg-zinc-800 border border-zinc-700 rounded p-2 focus:border-blue-500 outline-none text-center tracking-widest font-bold text-lg"
                        />
                        <button onClick={handleVerifyAndFind} className="w-full bg-blue-600 hover:bg-blue-500 py-2 rounded font-bold transition-colors">아이디 찾기</button>
                    </div>
                )}

                {step === 3 && (
                    <div className="text-center space-y-6">
                        <div className="bg-blue-500/10 p-4 rounded-lg border border-blue-500/30">
                            <p className="text-zinc-300">입력하신 이메일로 가입된<br />아이디 정보를 발송했습니다.</p>
                        </div>
                        <button onClick={() => navigate('/login')} className="w-full bg-zinc-800 hover:bg-zinc-700 py-2 rounded transition-colors">로그인하러 가기</button>
                    </div>
                )}
            </div>
        </div>
    );
}