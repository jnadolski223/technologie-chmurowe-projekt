import { type SubmitEvent, useState } from 'react';
import { type NavigateFunction, useNavigate } from 'react-router-dom';
import { config } from '../config/config.ts';
import type { ApiResponse, UserRequest, UserStorageData } from '../interfaces';

export default function LoginPage() {
  const navigate: NavigateFunction = useNavigate();

  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');

  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const handleSubmit = async (e: SubmitEvent) => {
    e.preventDefault();

    const loginRequest: UserRequest = { email, password };

    const errorMessage: string = 'Błąd: Nie udało się zalogować - błąd po stronie serwera.';

    try {
      setIsSubmitting(true);

      const response: Response = await fetch(`${config.apiBaseUrl}/users/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(loginRequest)
      });

      if (response.status === 401) {
        alert('Błąd: Niepoprawny adres email lub hasło.');
        return;
      }

      if (!response.ok) {
        const apiErrorData: ApiResponse<never> = await response.json().catch((): null => null);
        console.error('Błąd API:', { status: response.status, apiResponse: apiErrorData });
        alert(errorMessage);
        return;
      }

      const result: ApiResponse<UserStorageData> = await response.json();

      if (result.data) {
        localStorage.setItem(config.userStorageKey, JSON.stringify(result.data));
        alert('Zalogowano pomyślnie!');
        navigate('/account/profile');
      } else {
        console.error('Błąd API: Zwrócono status 200, ale brakuje danych:', result);
        alert(errorMessage);
      }
    } catch (err) {
      console.error('Błąd sieciowy / krytyczny aplikacji:', err);
      alert(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div>
      <h2>Logowanie</h2>

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">E-mail:</label>
          <input
            id="email"
            type="text"
            value={email}
            onChange={(e): void => setEmail(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="password">Hasło:</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e): void => setPassword(e.target.value)}
            required
          />
        </div>

        <div>
          <button type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Logowanie...' : 'Zaloguj się'}
          </button>
          <button type="button" onClick={() => navigate('/register')}>
            Nie masz jeszcze konta? Zarejestruj się!
          </button>
        </div>
      </form>
    </div>
  )
}
