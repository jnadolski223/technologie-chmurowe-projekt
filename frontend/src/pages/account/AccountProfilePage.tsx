import { useEffect, useState } from 'react';
import { type NavigateFunction, useNavigate } from 'react-router-dom';
import { config } from '../../config/config.ts';
import type { ApiResponse, UserData, UserStorageData } from '../../interfaces';

export default function AccountProfilePage() {
  const navigate: NavigateFunction = useNavigate();

  const [userData, setUserData] = useState<UserData | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect((): void => {
    const fetchUser = async (): Promise<void> => {
      const errorMessage: string = 'Błąd: nie udało się pobrać danych użytkownika.';

      try {
        setIsLoading(true);
        setError(null);

        const userDataRaw: string | null = localStorage.getItem(config.userStorageKey);
        if (!userDataRaw) {
          alert('Błąd: Musisz być zalogowany, aby zobaczyć tę stronę.');
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
        if (result.data) {
          setUserData(result.data);
        } else {
          console.error('Błąd API: Zwrócono status 200, ale brakuje danych:', result);
          setError(errorMessage);
          return;
        }
      } catch (err) {
        console.error('Błąd sieciowy / krytyczny aplikacji:', err);
        setError(errorMessage);
      } finally {
        setIsLoading(false);
      }
    }

    fetchUser().catch((err: unknown): void => {
      console.error(`Nieobsłużony błąd w fetchUser: ${err}`);
    });
  }, [navigate]);

  const handleEdit = (): void => {
    navigate('/account/form');
  }

  const handleDelete = async (): Promise<void> => {
    const errorMessage: string = 'Błąd: Nie udało się usunąć konta';
    const confirmed: boolean = window.confirm('Czy na pewno chcesz usunąć swoje konto? Wszystkie twoje dane, wydarzenia i zapisy zostaną usunięte.');
    if (!confirmed) return;

    try {
      const userDataRaw: string | null = localStorage.getItem(config.userStorageKey);
      if (!userDataRaw) return;

      const loggedInUser: UserStorageData = JSON.parse(userDataRaw);
      const userId: string = loggedInUser.id;

      const response: Response = await fetch(`${config.apiBaseUrl}/users/${userId}`, { method: 'DELETE' });
      if (response.ok) {
        alert('Konto zostało pomyślnie usunięte.');
        localStorage.removeItem(config.userStorageKey);
        navigate('/');
      } else {
        const apiErrorData: ApiResponse<never> = await response.json().catch((): null => null);
        console.error('Błąd API:', { status: response.status, apiResponse: apiErrorData });
        alert(errorMessage);
      }
    } catch (err) {
      console.error('Błąd sieciowy / krytyczny aplikacji:', err);
      alert(errorMessage);
    }
  }

  if (isLoading) {
    return (<p>Ładowanie profilu użytkownika...</p>);
  }

  if (error || !userData) {
    return (
      <div>
        <h3>Wystąpił problem</h3>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div>
      <h2>Dane konta</h2>
      <p><strong>E-mail:</strong> {userData.email}</p>
      <p><strong>Hasło:</strong> ***************</p>
      <div>
        <button onClick={handleEdit}>Edytuj dane</button>
        <button onClick={handleDelete}>Usuń konto</button>
      </div>
    </div>
  )
}
