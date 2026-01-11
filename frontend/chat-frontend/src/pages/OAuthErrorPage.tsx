import { useSearchParams, Link } from 'react-router-dom';

export function OAuthErrorPage() {
    const [searchParams] = useSearchParams();
    const message = searchParams.get('message') || 'Login failed';

    return (
        <div className="min-h-screen flex items-center justify-center bg-zinc-950 text-white">
            <div className="bg-zinc-900 p-8 rounded-lg shadow-xl max-w-md w-full border border-zinc-800 text-center">
                <h2 className="text-xl font-bold text-red-500 mb-4">Login Error</h2>
                <p className="text-zinc-300 mb-6">{message}</p>
                <Link to="/login" className="bg-blue-600 hover:bg-blue-500 text-white px-4 py-2 rounded transition-colors">
                    Back to Login
                </Link>
            </div>
        </div>
    );
}
