import { type SubmitEvent, useEffect, useState } from 'react';
import { type NavigateFunction, useNavigate } from 'react-router-dom';
import { config } from '../../config/config.ts';
import type { ApiResponse, UserData, UserRequest, UserStorageData } from '../../interfaces';

export default function AccountFormPage() {
  const navigate: NavigateFunction = useNavigate();

  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');

  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect((): void => {
    const fetchUserData = async (): Promise<void> => {
      const errorMessage: string = 'Błąd: Nie udało się pobrać danych użytkownika do edycji.';

      try {
        setIsLoading(true);
        setError(null);

        const userDataRaw: string | null = localStorage.getItem(config.userStorageKey);
        if (!userDataRaw) {
          alert('Błąd: Musisz być zalogowany, aby edytować swoje dane.');
          navigate('/login');
          return;
        }

        const loggedInUser: UserStorageData = JSON.parse(userDataRaw);
        const userId: string = loggedInUser.id;

        const response: Response = await fetch(`${config.apiBaseUrl}/users/${userId}`);
        if (!response.ok) {
          const apiErrorData: ApiResponse<never> = await response.json().catch((): null => null);
          console.error('Błąd API:', { status: response.status, apiResponse: apiErrorData });
          setError(errorMessage);
          return;
        }

        const result: ApiResponse<UserData> = await response.json();

        if (!result.data) {
          console.error('Błąd API: Zwrócono status 200, ale brakuje danych:', result);
          setError(errorMessage);
          return;
        }

        setEmail(result.data.email);
      } catch (err) {
        console.error('Błąd sieciowy / krytyczny aplikacji:', err);
        setError(errorMessage);
      } finally {
        setIsLoading(false);
      }
    }

    fetchUserData().catch((err: unknown): void => {
      console.error(`Nieobsłużony błąd w fetchUserData: ${err}`);
    });
  }, [navigate]);

  const handleSubmit = async (e: SubmitEvent) => {
    e.preventDefault();

    const userDataRaw: string | null = localStorage.getItem(config.userStorageKey);
    if (!userDataRaw) return;

    const loggedInUser: UserStorageData = JSON.parse(userDataRaw);
    const userId: string = loggedInUser.id;

    const userRequest: UserRequest = { email, password };

    const errorMessage: string = 'Błąd: Nie udało się zaktualizować danych konta.';

    try {
      setIsSubmitting(true);

      const response: Response = await fetch(`${config.apiBaseUrl}/users/${userId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userRequest)
      });

      if (response.status === 409) {
        alert('Błąd: Wybrany adres email jest już zajęty przez innego użytkownika');
        return;
      }

      if (response.ok) {
        alert('Dane konta zostały pomyślnie zaktualizowane!');
        navigate('/account/profile');
      } else {
        const apiErrorData: ApiResponse<never> = await response.json().catch((): null => null);
        console.error('Błąd API:', { status: response.status, apiResponse: apiErrorData });
        alert(errorMessage);
      }
    } catch (err) {
      console.error('Błąd sieciowy / krytyczny aplikacji:', err);
      alert(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  }

  if (isLoading) {
    return (<p>ładowanie danych profilu...</p>);
  }

  if (error) {
    return (
      <div>
        <h3>Wystąpił problem</h3>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div>
      <h2>Edytuj dane konta</h2>

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">Nowy adres e-mail:</label>
          <input
            id="email"
            type="text"
            value={email}
            onChange={(e): void => setEmail(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="password">Nowe hasło:</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e): void => setPassword(e.target.value)}
            required
            minLength={8}
          />
        </div>

        <div>
          <button type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Zapisywanie...' : 'Zapisz zmiany'}
          </button>
          <button type="button" onClick={() => navigate(-1)}>
            Anuluj
          </button>
        </div>
      </form>
    </div>
  )
}
