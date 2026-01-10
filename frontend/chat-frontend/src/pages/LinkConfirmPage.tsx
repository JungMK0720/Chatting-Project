import { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

export function LinkConfirmPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const [existingEmail, setExistingEmail] = useState('');
    const [provider, setProvider] = useState('');
    const [providerId, setProviderId] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        const paramEmail = searchParams.get('existingEmail') || searchParams.get('email');
        const paramProvider = searchParams.get('provider');
        const paramProviderId = searchParams.get('providerId');

        if (paramEmail) setExistingEmail(paramEmail);
        if (paramProvider) setProvider(paramProvider);
        if (paramProviderId) setProviderId(paramProviderId);
    }, [searchParams]);

    const handleLink = async () => {
        setError('');

        const data = {
            email: existingEmail,
            providerId: providerId,
            provider: provider
        };
        console.log('Linking account with data:', data);

        try {
            const response = await api.post('/users/link-account', data);

            if (response.status === 200) {
                const { accessToken } = response.data;
                if (accessToken) {
                    localStorage.setItem('accessToken', accessToken);
                    alert('계정 연동 및 로그인 성공!');
                    navigate('/'); // Redirect to main page
                } else {
                    // Handle case where token might be missing but status is 200 (unlikely based on logic but good for safety)
                    alert('계정 연동 성공! 로그인 페이지로 이동합니다.');
                    navigate('/login');
                }
            }
        } catch (err: any) {
            console.error('Link account error:', err);
            setError(err.response?.data?.message || '연동 중 알 수 없는 오류가 발생했습니다.');
        }
    };

    const handleCancel = () => {
        alert('계정 연동을 취소합니다. 로그인 페이지로 돌아갑니다.');
        navigate('/login');
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-zinc-50">
            <div className="bg-white p-10 rounded-xl shadow-lg w-full max-w-md border border-zinc-200 text-center">
                <div className="text-2xl font-bold mb-2 text-zinc-800">
                    {provider} 연동
                </div>
                <h3 className="text-xl font-semibold mt-4 mb-4 text-zinc-700">기존 계정 연동 확인</h3>

                <p className="mt-3 text-zinc-500 text-sm">인증된 계정 정보</p>
                <div className="bg-zinc-100 p-3 rounded-md mb-6 font-semibold text-zinc-800">
                    {existingEmail}
                </div>

                <p className="text-zinc-600 mb-6">
                    이 <span className="font-bold text-zinc-800">{provider}</span> 계정을 위 이메일 계정 (기존 계정)과 <span className="font-bold text-zinc-800">연동</span>하시겠습니까?
                    <br />
                    <small className="text-zinc-400 block mt-2">(연동 시, 해당 소셜 계정으로도 로그인할 수 있게 됩니다.)</small>
                </p>

                {error && (
                    <div className="bg-red-50 text-red-500 p-3 rounded mb-4 text-sm border border-red-100">
                        {error}
                    </div>
                )}

                <div className="flex gap-3 justify-between mt-4">
                    <button
                        onClick={handleCancel}
                        className="flex-1 bg-white hover:bg-zinc-50 text-zinc-600 border border-zinc-300 py-2 px-4 rounded transition-colors"
                    >
                        취소
                    </button>
                    <button
                        onClick={handleLink}
                        className="flex-1 bg-green-600 hover:bg-green-500 text-white py-2 px-4 rounded transition-colors font-bold"
                    >
                        계정 연동 및 로그인
                    </button>
                </div>
            </div>
        </div>
    );
}
